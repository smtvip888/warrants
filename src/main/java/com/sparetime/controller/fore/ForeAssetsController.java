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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fore/assets")
public class ForeAssetsController {

    @Autowired
    private UserCashBonusService userCashBonusService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDTFTBonusService userDTFTBonusService;

    @Autowired
    private UserJTFTBonusService userJTFTBonusService;

    @Autowired
    private UserDTLYBonusService userDTLYBonusService;

    @Autowired
    private UserJTSCBonusService userJTSCBonusService;

    @Autowired
    private UserZCJFService userZCJFService;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SharesConfigService sharesConfigService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private UserCashBonusGetService userCashBonusGetService;

    @RequestMapping("/userBonusList")
    public String userBonusList(UserBonus query, String start, String end, Page page, HttpServletRequest request, Model model) throws Exception{

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

        return "fore/assets/user_bonus_list";
    }

    @RequestMapping("/userCashBonusList")
    public String userCashBonusList(UserCashBonus query, String daterange, Page page, HttpServletRequest request, Model model) throws Exception{

        if (query.getUserId() == null)
            query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));
        DateUtil.timeHandle(query, daterange);
        List<UserCashBonus> list = userCashBonusService.queryListByExample(query, page);
        List<String> fromUserSNList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
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
        model.addAttribute("fromUserSNList", fromUserSNList);
        model.addAttribute("daterange", daterange);

        return "fore/assets/user_cash_bonus_list";
    }

    @RequestMapping("/userDTFTBonusList")
    public String userDTFTBonusList(UserDTFTBonus query, String daterange, Page page, HttpServletRequest request, Model model){

        if (query.getUserId() == null)
            query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));

        DateUtil.timeHandle(query, daterange);

        List<UserDTFTBonus> list = userDTFTBonusService.queryListByExample(query, page);

        List<String> fromUserSNList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
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
        model.addAttribute("fromUserSNList", fromUserSNList);
        model.addAttribute("daterange", daterange);

        return "fore/assets/user_DTFT_bonus_list";
    }

    @RequestMapping("/userJTFTBonusList")
    public String userJTFTBonusList(UserJTFTBonus query, String daterange, Page page, HttpServletRequest request, Model model){

        if (query.getUserId() == null)
            query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));

        DateUtil.timeHandle(query, daterange);
        List<UserJTFTBonus> list = userJTFTBonusService.queryListByExample(query, page);

        List<String> fromUserSNList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
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
        model.addAttribute("fromUserSNList", fromUserSNList);
        model.addAttribute("daterange", daterange);

        return "fore/assets/user_JTFT_bonus_list";
    }

    @RequestMapping("/userDTLYBonusList")
    public String userDTLYBonusList(UserDTLYBonus query, String daterange, Page page, HttpServletRequest request, Model model){

        if (query.getUserId() == null)
            query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));

        DateUtil.timeHandle(query, daterange);

        List<UserDTLYBonus> list = userDTLYBonusService.queryListByExample(query, page);
        List<String> fromUserSNList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
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
        model.addAttribute("fromUserSNList", fromUserSNList);
        model.addAttribute("daterange", daterange);

        return "fore/assets/user_DTLY_bonus_list";
    }

    @RequestMapping("/userJTSCBonusList")
    public String userJTSCBonusList(UserJTSCBonus query, String daterange, Page page, HttpServletRequest request, Model model){

        if (query.getUserId() == null)
            query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));

        DateUtil.timeHandle(query, daterange);
        List<UserJTSCBonus> list = userJTSCBonusService.queryListByExample(query, page);
        List<String> fromUserSNList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
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
        model.addAttribute("fromUserSNList", fromUserSNList);
        model.addAttribute("daterange", daterange);

        return "fore/assets/user_JTSC_bonus_list";
    }

    @RequestMapping("/userZCJFList")
    public String userZCJFList(UserZCJF query, String start, String end, Page page, HttpServletRequest request, Model model) throws Exception{

        if (query.getUserId() == null)
            query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));
        query.setIsdel(0);

        //DateUtil.timeHandle(query, daterange);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isEmpty(start))
            query.setStartTime(sdf.parse(start));
        if (!StringUtils.isEmpty(end))
            query.setEndTime(sdf.parse(end));

        List<UserZCJF> list = userZCJFService.queryListByExample(query, page);

        List<String> fromUserNameList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getFromUserId());
            fromUserNameList.add(fromUser == null ? "系统" : fromUser.getUserName());
        });
        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("fromUserNameList", fromUserNameList);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "fore/assets/user_ZCJF_list";
    }

    @RequestMapping("/toTransferZCJF")
    public String toTransferZCJF(UserZCJF userZCJF, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        UserAsset userAsset = userAssetService.queryByUserId(userId);

        model.addAttribute("userAsset", userAsset);
        return "fore/assets/transfer_ZCJF";
    }

    @RequestMapping("/transferZCJF")
    public String transferZCJF(@Valid UserZCJF userZCJF, BindingResult bindingResult, String inUserName, String password, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        UserAsset userAsset = userAssetService.queryByUserId(userId);

        model.addAttribute("userAsset", userAsset);

        User out = userService.queryByKey(userId);
        User in = null;

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        userZCJF.setIp(ip);

        String tip = "";
        if (StringUtils.isEmpty(password))
            tip = "交易密码不能为空";
        else if (!bCryptPasswordEncoder.matches(password, out.getPassword1()))
            tip = "交易密码不正确";

        if (StringUtils.isEmpty(inUserName)){
            FieldError fieldError = new FieldError("userZCJF", "fromUserId", "转入账户用户名不能为空");
            bindingResult.addError(fieldError);
        }else if ((in = userService.queryByName(inUserName)) == null){
            FieldError fieldError = new FieldError("userZCJF", "fromUserId", "转入账户不存在");
            bindingResult.addError(fieldError);
        }

        if (userZCJF.getAmount() != null && userAsset.getZCJFYYE().compareTo(userZCJF.getAmount()) == -1){
            FieldError fieldError = new FieldError("userZCJF", "amount", "余额不足");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasFieldErrors())
            tip = bindingResult.getAllErrors().get(0).getDefaultMessage();

        if (StringUtils.isEmpty(tip)){
            try {
                userAssetService.transferZCJF(out, in, userZCJF.getAmount(), ip);
                tip = "转账成功";
            }catch (Exception e){
                tip = e.getMessage();
            }
        }

        model.addAttribute("tip", tip);
        model.addAttribute("inUserName", inUserName);
        return "fore/assets/transfer_ZCJF";
    }

    @RequestMapping("/toConvert")
    public String toConvert(HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        UserAsset userAsset = userAssetService.queryByUserId(userId);

        model.addAttribute("userAsset", userAsset);
        model.addAttribute("filed", null);
        return "fore/assets/convert";
    }

    @RequestMapping("/convert")
    public String convert(HttpServletRequest request, BigDecimal surplus, String password, Model model){

        model.addAttribute("surplus", surplus);

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        UserAsset userAsset = userAssetService.queryByUserId(userId);

        String filed = "";
        if (surplus == null)
            filed = "转出金额不能为空";
        else if (userAsset.getBonusSurplus().compareTo(surplus) == -1)
            filed = "余额不足";
        else if (surplus.compareTo(BigDecimal.ZERO) == -1)
            filed = "转出金额不能为负";

        if (password == null)
            filed = "交易密码不能为空";
        else if (!bCryptPasswordEncoder.matches(password, userService.queryByKey(userId).getPassword1()))
            filed = "交易密码不正确";


        if (!StringUtils.isEmpty(filed)){
            model.addAttribute("userAsset", userAsset);
            model.addAttribute("filed", filed);
            return "fore/assets/convert";
        }

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        userAssetService.convert(userId, surplus, ip);

        return "redirect:/fore/assets/userZCJFList";
    }

    @RequestMapping("/toGetCash")
    public String toGetCash(UserCashBonusGet userCashBonusGet, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");

        UserAsset userAsset = userAssetService.queryByUserId(userId);
        SharesConfig sharesConfig = sharesConfigService.getConfig();
        UserProfile profile = userProfileService.queryByUserId(userId);
        String tip = "";
        if (StringUtils.isEmpty(profile.getBankAccountName()) ||
                StringUtils.isEmpty(profile.getBankName()) ||
                StringUtils.isEmpty(profile.getBankCountry()) ||
                StringUtils.isEmpty(profile.getBankAccountNumber()))
            tip = "银行信息不全";

        userCashBonusGet.setBankName(profile.getBankName());
        userCashBonusGet.setBankCode(profile.getBankCode());
        userCashBonusGet.setAccountName(profile.getBankAccountName());
        userCashBonusGet.setAccountNumber(profile.getBankAccountNumber());
        userCashBonusGet.setBankAddress(profile.getBankCountry());

        UserCashBonusGet getQuery = new UserCashBonusGet();
        getQuery.setUserId(userId);
        List<UserCashBonusGet> getList = userCashBonusGetService.queryListByExample(getQuery, null);
        SharesConfig config = sharesConfigService.getConfig();

        model.addAttribute("userAsset", userAsset);
        model.addAttribute("sharesConfig", sharesConfig);
        model.addAttribute("userCashBonusGet", userCashBonusGet);
        model.addAttribute("getList", getList);
        model.addAttribute("config", config);
        model.addAttribute("tip", tip);
        return "fore/assets/get_cash";
    }

    @RequestMapping("/getCash")
    public String getCash(@Valid UserCashBonusGet userCashBonusGet, BindingResult bindingResult, String password, HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        UserAsset userAsset = userAssetService.queryByUserId(userId);
        SharesConfig sharesConfig = sharesConfigService.getConfig();
        User user = userService.queryByKey(userId);
        UserCashBonusGet getQuery = new UserCashBonusGet();
        getQuery.setUserId(userId);
        List<UserCashBonusGet> getList = userCashBonusGetService.queryListByExample(getQuery, null);
        SharesConfig config = sharesConfigService.getConfig();

        model.addAttribute("userAsset", userAsset);
        model.addAttribute("sharesConfig", sharesConfig);
        model.addAttribute("userCashBonusGet", userCashBonusGet);
        model.addAttribute("getList", getList);
        model.addAttribute("config", config);

        if (StringUtils.isEmpty(password)){
            FieldError fieldError = new FieldError("cashBonusGet", "remark", "交易密码不能为空");
            bindingResult.addError(fieldError);
        }else if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            FieldError fieldError = new FieldError("cashBonusGet", "remark", "交易密码不正确");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasFieldErrors()){
            model.addAttribute("tip", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "fore/assets/get_cash";
        }

        userAssetService.getCash(userId, userCashBonusGet);

        model.addAttribute("tip", "提取成功");

        return "fore/assets/get_cash";
    }

    @RequestMapping("/totalProfit")
    public String totalProfit(HttpServletRequest request, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");

        NetUser netUser = netUserService.queryByUserId(userId);
        Product product = productService.queryByKey(netUser.getProductId());
        UserAsset userAsset = userAssetService.queryByUserId(userId);

        SharesNum dtSharesNum = functionMapper.callP_GetUserCanSellShares_DT(userId);
        SharesNum jtSharesNum = functionMapper.callP_GetUserCanSellShares_JT(userId);

        model.addAttribute("netUser", netUser);
        model.addAttribute("product", product);
        model.addAttribute("userAsset", userAsset);
        model.addAttribute("dtSharesNum", dtSharesNum);
        model.addAttribute("jtSharesNum", jtSharesNum);
        return "fore/assets/total_profit";
    }
}
