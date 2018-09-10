package com.sparetime.controller.fore;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.DateUtil;
import com.sparetime.common.util.FileUtil;
import com.sparetime.domian.*;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/fore/integral")
public class ForeIntegralController {

    @Autowired
    private UserCashSellService userCashSellService;

    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserCashBuyService userCashBuyService;

    @Autowired
    private UserCashImageService userCashImageService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SharesConfigService sharesConfigService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping("/hall")
    public String hall(UserCashSell query, BigDecimal start, BigDecimal end, Page page, HttpServletRequest request, Model model){

        User login = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        Country country = countryService.queryByKey(new BigDecimal(login.getRegCountry()));

        query.setCurrencyName(country.getCurrencyName());
        query.setStatus(1);
        List<UserCashSell> list = userCashSellService.queryListByExample(query, start, end, page);
        List<String> userNames = new ArrayList<>();

        list.forEach(sell -> {
            User user = userService.queryByKey(sell.getUserId());
            int length = user.getUserName().length();
            userNames.add(user.getUserName().substring(0, 2) + "**" + user.getUserName().substring(length - 2, length));
        });

        Country countryQuery = new Country();
        countryQuery.setIsEnable(1);

        List<Country> countryList = countryService.queryListByExample(countryQuery, null);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("userNames", userNames);
        model.addAttribute("countryList", countryList);
        return "fore/integral/hall";
    }

    @RequestMapping("/sellInfo")
    public String sellInfo(BigDecimal sellId, HttpServletRequest request, Model model){

        UserCashSell sell = userCashSellService.queryByKey(sellId);
        if (!StringUtils.isEmpty(sell.getMobile())){
            sell.setMobile(sell.getMobile().substring(0, 3) + "****" + sell.getMobile().substring(7, sell.getMobile().length()));
        }
        User user = userService.queryByKey(sell.getUserId());
        int length = user.getUserName().length();
        model.addAttribute("sell", sell);
        model.addAttribute("tip", request.getAttribute("tip"));
        model.addAttribute("userName", user.getUserName().substring(0, 2) + "**" + user.getUserName().substring(length - 2, length));
        return "fore/integral/sell_info";
    }

    @RequestMapping("/buy")
    public String buy(BigDecimal sellId, String password, HttpServletRequest request, Model model){
        String tip = "";
        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            tip = "交易密码不正确";
        }

        UserCashSell sell = userCashSellService.queryByKey(sellId);
        if (sell.getBuyId() != null){
            tip = "已经有人认购";
        }

        UserCashBuy query = new UserCashBuy();
        query.setUserId(user.getUserId());
        query.setStatus(1);
        List<UserCashBuy> buyList = userCashBuyService.queryListByExample(query, null);
        if (buyList.size() >= 3){
            tip = "每个账号最多只能同时认购3笔交易";
        }
        if (!StringUtils.isEmpty(tip)){
            model.addAttribute("tip", tip);
            return "forward:/fore/integral/sellInfo?sellId=" + sellId;
        }
        UserCashBuy buy = userCashBuyService.buy(user, sell, request.getHeader("x-forwarded-for") == null ?
                request.getRemoteAddr() :
                request.getHeader("x-forwarded-for"));

        model.addAttribute("buy", buy);
        return "redirect:/fore/integral/remitDetail?buyId=" + buy.getBuyId();
    }

    @RequestMapping("/buyList")
    public String buyList(HttpServletRequest request, UserCashBuy query, Page page, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");
        query.setUserId(userId);
        List<UserCashBuy> list = userCashBuyService.queryListByExample(query, page);

        model.addAttribute("list", list);
        return "fore/integral/buy_list";
    }

    @RequestMapping("/remitDetail")
    public String remitDetail(HttpServletRequest request, BigDecimal buyId, Model model) throws Exception{

        UserCashBuy buy = userCashBuyService.queryByKey(buyId);
        UserCashSell sell = userCashSellService.queryByKey(buy.getSellId());
        User sellUser = userService.queryByKey(sell.getUserId());

        SharesConfig config = sharesConfigService.getConfig();
        int HKQRSJ = config.getHKQRSJ();
        Date dateHK = Date.from(DateUtil.dateToLocalDateTime(buy.getCdate()).plusHours(HKQRSJ).atZone(ZoneId.systemDefault()).toInstant());

        List<UserCashBuyImage> images = userCashImageService.queryListByBuyId(buyId);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        model.addAttribute("buy", buy);
        model.addAttribute("sell", sell);
        model.addAttribute("sellUser", sellUser);
        model.addAttribute("images", images);
        model.addAttribute("dateHK", dateHK);
        model.addAttribute("tip", request.getAttribute("tip"));
        model.addAttribute("now",sdf.parse(ZonedDateTime.now(ZoneId.of("GMT")).format(dtf)));
        return "fore/integral/remit_detail";
    }

    @RequestMapping("/buyImgCommit")
    public String buyImgCommit(HttpServletRequest request, BigDecimal buyId, String password, MultipartFile[] images, Model model) throws Exception{

        String tip = "";
        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            tip = "交易密码不正确";
        }

        if (!StringUtils.isEmpty(tip)){
            model.addAttribute("tip", tip);
            return "forward:/fore/integral/remitDetail?buyId=" + buyId;
        }

        List<String> imgPaths = request.getParameterValues("imgPaths") == null ?
                new ArrayList<>() :
                new ArrayList<>(Arrays.asList(request.getParameterValues("imgPaths")));

        SysConfig sysConfig = sysConfigService.queryByKey("file_path");
        if (sysConfig == null || StringUtils.isEmpty(sysConfig.getValue()))
            throw new RuntimeException("路径配置不能为空");


        List<String> paths = FileUtil.upload(images, sysConfig.getValue(), request.getServerPort());
        imgPaths.addAll(paths);

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        userCashBuyService.buyImgCommit(buyId, imgPaths, ip);

        return "forward:/fore/integral/remitDetail?buyId=" + buyId;
    }

    @RequestMapping("/toSell")
    public String toSell(HttpServletRequest request, UserCashSell sell, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
        Country country = countryService.queryByKey(new BigDecimal(user.getRegCountry()));
        SharesConfig sharesConfig = sharesConfigService.getConfig();
        UserProfile profile = userProfileService.queryByUserId(user.getUserId());

        model.addAttribute("userAsset", userAsset);
        model.addAttribute("country", country);
        model.addAttribute("sharesConfig", sharesConfig);
        model.addAttribute("sell", sell);
        model.addAttribute("profile", profile);
        return "fore/integral/sell";
    }

    @RequestMapping("/sell")
    public String sell(@Valid UserCashSell sell, BindingResult result,String password, HttpServletRequest request, Model model){

        String tip = "";

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            tip = "交易密码不正确";
        }

        if (result.hasErrors()){
            tip = result.getFieldError().getDefaultMessage();
        }else if (!sell.getAmount().divideAndRemainder(new BigDecimal("50"))[1].equals(BigDecimal.ZERO)){
            tip = "数量必须是50的倍数";
        }

        if (sell.getMobile() != null && sell.getMobile().length() < 8){
            tip = "手机号码不能少于8位";
        }

        if (!StringUtils.isEmpty(tip)){
            model.addAttribute("tip", tip);
            return "forward:/fore/integral/toSell";
        }

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        try {
            SharesConfig config = sharesConfigService.getConfig();
            sell.setBrokerage(sell.getAmount().multiply(config.getTXSXF()).divide(new BigDecimal("100")));
            userCashSellService.sell(user, sell, ip);
        }catch (Exception e){
            tip = e.getMessage();
        }

        if (!StringUtils.isEmpty(tip)){
            model.addAttribute("tip", tip);
            return "forward:/fore/integral/toSell";
        }

        return "redirect:/fore/integral/hall";
    }

    @RequestMapping("/sellList")
    public String sellList(UserCashSell query, Page page, HttpServletRequest request, Model model){

        query.setUserId((BigDecimal) request.getSession().getAttribute("userId"));
        List<UserCashSell> list = userCashSellService.queryListByExample(query,null, null, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        return "fore/integral/sell_list";
    }

    @RequestMapping("/remitConfirm")
    public String remitConfirm(HttpServletRequest request, BigDecimal sellId, Model model)throws Exception{

        UserCashSell sell = userCashSellService.queryByKey(sellId);
        User sellUser = userService.queryByKey(sell.getUserId());
        User buyUser = new User();
        UserProfile profile = new UserProfile();
        if (sell.getBuyUserId() != null){
            buyUser = userService.queryByKey(sell.getBuyUserId());
            profile = userProfileService.queryByUserId(sell.getBuyUserId());
        }

        SharesConfig config = sharesConfigService.getConfig();
        UserCashBuy buy = null;
        Date dateSK = null;
        List<UserCashBuyImage> images = new ArrayList<>();
        if (sell.getBuyId() != null){
            buy = userCashBuyService.queryByKey(sell.getBuyId());
            if (buy.getPayDate() != null)
                dateSK = Date.from(DateUtil.dateToLocalDateTime(buy.getPayDate()).plusHours(config.getSKQRSJ()).atZone(ZoneId.systemDefault()).toInstant());
            images = userCashImageService.queryListByBuyId(sell.getBuyId());
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        model.addAttribute("sell", sell);
        model.addAttribute("buyUser", buyUser);
        model.addAttribute("sellUser", sellUser);
        model.addAttribute("dateSK", dateSK);
        model.addAttribute("buy", buy);
        model.addAttribute("images", images);
        model.addAttribute("profile", profile);
        model.addAttribute("now",sdf.parse(ZonedDateTime.now(ZoneId.of("GMT")).format(dtf)));
        return "fore/integral/remit_confirm";
    }

    @RequestMapping("/gathering")
    public String gathering(BigDecimal sellId, String password, HttpServletRequest request, Model model){

        String tip = "";

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword1())){
            tip = "交易密码不正确";
            model.addAttribute("tip", tip);
            return "forward:/fore/integral/remitConfirm";
        }

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        synchronized (this){
            UserCashSell sell = userCashSellService.queryByKey(sellId);
            if (sell.getStatus() != 4)
                userCashSellService.gathering(sell, ip);
        }

        return "forward:/fore/integral/remitConfirm";
    }
}
