package com.sparetime.controller.fore;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.DateUtil;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.domian.*;
import com.sparetime.domian.extend.SharesNum;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
@Controller
@RequestMapping("/fore/trade")
public class ForeTradeController {

    @Autowired
    private SharesPriceService sharesPriceService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ShareBuyService shareBuyService;

    @Autowired
    private ShareSellService shareSellService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @Autowired
    private ShareService shareService;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        SharesPrice sharesPrice = sharesPriceService.queryByMaxId();
        UserAsset userAsset = userAssetService.queryByUserId(userId);

        BigDecimal canSellShares = shareSellService.getUserCanSellShares(userId);

        Page page = new Page();
        page.setPageSize(5);

        ShareSell sellQuery = new ShareSell();
        List<ShareSell> userSellList = shareSellService.queryListByExample(sellQuery, page);

        sellQuery.setUserId(userId);
        List<ShareSell> mySellList = shareSellService.queryListByExample(sellQuery, page);

        page.setPageSize(5);
        ShareBuy buyQuery = new ShareBuy();
        List<ShareBuy> userBuyList = shareBuyService.queryListByExample(buyQuery, page);

        buyQuery.setUserId(userId);
        List<ShareBuy> myBuyList = shareBuyService.queryListByExample(buyQuery, page);

        List<TodayPrice> todayPrice = functionMapper.callP_GetTodaySharePrice();
        BigDecimal prePrice = sharesPriceService.queryByMaxId().getPrice();

        BigDecimal[] trend = sharesPriceService.priceTrend(20);
        String[] trendDate = DateUtil.dateToStr(20, "MM-dd");

        SharesNum dtSharesNum = functionMapper.callP_GetUserCanSellShares_DT(userId);
        SharesNum jtSharesNum = functionMapper.callP_GetUserCanSellShares_JT(userId);

        model.addAttribute("sharesPrice", sharesPrice);
        model.addAttribute("userAsset", userAsset);
        model.addAttribute("shareBuy", new ShareBuy());
        model.addAttribute("shareSell", new ShareSell());
        model.addAttribute("canSellShares", canSellShares);
        model.addAttribute("filed", request.getAttribute("filed"));
        model.addAttribute("userSellList", userSellList);
        model.addAttribute("mySellList", mySellList);
        model.addAttribute("userBuyList", userBuyList);
        model.addAttribute("myBuyList", myBuyList);
        model.addAttribute("todayPrice", todayPrice);
        model.addAttribute("prePrice", prePrice);
        model.addAttribute("trend", trend);
        model.addAttribute("trendDate", trendDate);
        model.addAttribute("dtSharesNum", dtSharesNum);
        model.addAttribute("jtSharesNum", jtSharesNum);
        return "fore/trade/trade";
    }

    @RequestMapping("/buy")
    public String buy(ShareBuy shareBuy, String password, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        User user =  userService.queryByKey(userId);
        NetUser netUser = netUserService.queryByUserId(userId);

        shareBuy.setUserId(userId);
        shareBuy.setNetUserId(netUser.getNetUserId());

        shareBuy.setSuccesPrice(BigDecimal.ZERO);
        shareBuy.setShares(BigDecimal.ZERO);
        shareBuy.setSuccesShares(BigDecimal.ZERO);
        shareBuy.setStatus(1);
        shareBuy.setBuyPrice(sharesPriceService.queryByMaxId().getPrice());

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        shareBuy.setIp(ip);

        String filed = "";

        if (shareBuy.getPrice() == null){
            filed = "买入金额不能为空";
        }else if (shareBuy.getPrice().compareTo(new BigDecimal(20)) == -1)
            filed = "买入金额不能小于20";

        if (StringUtils.isEmpty(password)){
            filed = "交易密码不能为空";
        }else if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            filed = "交易密码不正确";
        }

        if (netUser.getIsOut() == 1){
            filed = "你的账户现在禁止交易";
        }

        if(StringUtils.isEmpty(filed)){
            try {
                shareBuyService.buy(shareBuy);
            }catch (Exception e){
                filed = e.getMessage();
            }
        }

        if (StringUtils.isEmpty(filed))
            filed = "买入成功";

        model.addAttribute("filed", filed);
        model.addAttribute("price", shareBuy.getPrice());
        return "forward:/fore/trade/main";
    }

    @RequestMapping("/sell")
    public String sell(ShareSell shareSell, int sellType, String password, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        User user =  userService.queryByKey(userId);
        NetUser netUser = netUserService.queryByUserId(userId);

        shareSell.setUserId(userId);
        shareSell.setNetUserId(netUser.getNetUserId());

        shareSell.setSuccesShares(BigDecimal.ZERO);
        shareSell.setSplitCount(BigDecimal.ZERO);
        shareSell.setStatus(1);

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        shareSell.setIp(ip);

        String filed = "";

        if (shareSell.getShares() == null){
            filed = "挂出数量不能为空";
        }else if (shareSell.getShares().compareTo(new BigDecimal(20)) == -1)
            filed = "挂卖数量不能小于20";

        if (StringUtils.isEmpty(password)){
            filed = "交易密码不能为空";
        }else if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            filed = "交易密码不正确";
        }

        if (netUser.getIsOut() == 1){
            filed = "你的账户现在禁止交易";
        }

        if(StringUtils.isEmpty(filed)){

            try {
                shareSellService.sell(shareSell, sellType);
            }catch (Exception e){
                filed = e.getMessage();
            }
        }

        if(StringUtils.isEmpty(filed)){
            filed = "挂卖成功";
        }

        model.addAttribute("filed", filed);
        model.addAttribute("shares", shareSell.getShares());
        return "forward:/fore/trade/main";
    }

    @RequestMapping("/buyList")
    public String buyList(ShareBuy query, HttpServletRequest request, Page page, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        query.setTradeSN(query.getTradeSN() == null ? "" : query.getTradeSN());
        List<ShareBuy> list = shareBuyService.querySelfListLikeTradeSN(query.getTradeSN(), userId, page);

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        return "fore/trade/buy_list";
    }

    @RequestMapping("/sellList")
    public String sellList(ShareSell query, HttpServletRequest request, Page page, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        query.setTradeSN(query.getTradeSN() == null ? "" : query.getTradeSN());
        List<ShareSell> list = shareSellService.querySelfListLikeTradeSN(query.getTradeSN(), userId, page);

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        return "fore/trade/sell_list";
    }

    @RequestMapping("/tradeList")
    public String tradeList(UserBonus query, String start, String end, Page page, HttpServletRequest request, Model model) throws Exception{

        query.setTradeType(TradeTypeEnum.静态.getCode());
        query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));
        //DateUtil.timeHandle(query, daterange);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isEmpty(start))
            query.setStartTime(sdf.parse(start));
        if (!StringUtils.isEmpty(end))
            query.setEndTime(sdf.parse(end));
        List<UserBonus> list = userBonusService.queryListByExample(query, page);
        List<String> fromUserNameList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getFromUserId());
            fromUserNameList.add(fromUser == null ? null : fromUser.getUserName());
        });

        TradeTypeEnum[] tradeTypeEnums = TradeTypeEnum.values();
        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<BonusType> bonusTypeList = bonusTypeService.queryAll();

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        model.addAttribute("tradeTypeEnums", tradeTypeEnums);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("bonusTypeList", bonusTypeList);
        model.addAttribute("fromUserNameList", fromUserNameList);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "fore/trade/trade_list";
    }

    @RequestMapping("/sharesList")
    public String sharesList(Share query, Page page, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        query.setUserId(userId);

        List<Share> list = shareService.queryListByExample(query, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);

        return "fore/trade/shares_list";
    }
}
