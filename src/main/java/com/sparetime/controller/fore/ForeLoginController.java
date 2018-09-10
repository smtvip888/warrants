package com.sparetime.controller.fore;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.cons.PlaceAreaEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.DateUtil;
import com.sparetime.config.InternationalConfig;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.domian.*;
import com.sparetime.domian.extend.Notice;
import com.sparetime.domian.extend.SharesNum;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by muye on 17/8/12.
 */
@Controller
public class ForeLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private ProductService productService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private ShareBuyService shareBuyService;

    @Autowired
    private ShareSellService shareSellService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private SharesPriceService sharesPriceService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ShareTradeService shareTradeService;

    @Autowired
    private BannersService bannersService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private InternationalConfig internationalConfig;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SharesConfigService sharesConfigService;

    @RequestMapping("/fore/toReg")
    public String toReg(User user, HttpServletRequest request, Model model){

        String lang = (String) request.getSession().getAttribute("lang");

        PlaceAreaEnum[] placeAreaEnums = PlaceAreaEnum.values();
        List<Product> productList = productService.queryAll();
        UserAsset userAsset = userAssetService.queryByUserId((BigDecimal) request.getSession().getAttribute("userId"));

        List<Country> countryList = countryService.queryListByExample(new Country(), null);

        if (user.getParentUserId() != null){
            User parent = userService.queryByKey(user.getParentUserId());
            if (parent != null){
                model.addAttribute("parent", parent.getUserName());
            }
        }

        Arrays.asList(placeAreaEnums).forEach(e -> e.setName(StringUtils.isEmpty(internationalConfig.get(lang + e.getName())) ? e.getName() : internationalConfig.get(lang + e.getName())));

        model.addAttribute("productList", productList);
        model.addAttribute("placeAreaEnums", placeAreaEnums);
        model.addAttribute("userAsset", userAsset);
        model.addAttribute("countryList", countryList);
        return "fore/reg";
    }

    @RequestMapping("/fore/reg")
    public String reg(@Valid User user, BindingResult bindingResult,String bankAccountName, String recommend, String parent,
                      BigDecimal productId, HttpServletRequest request, String tradePassword,String idCard, BigDecimal DTFTJJYE,
                      BigDecimal ZCJFYYE, Model model){

        BigDecimal loginUserId = (BigDecimal) request.getSession().getAttribute("userId");

        User loginUser = userService.queryByKey(loginUserId);

        String tip = "";

        if (ZCJFYYE == null) ZCJFYYE = BigDecimal.ZERO;
        if (DTFTJJYE == null) DTFTJJYE = BigDecimal.ZERO;

        UserAsset userAsset = userAssetService.queryByUserId((BigDecimal) request.getSession().getAttribute("userId"));
        List<Product> productList = productService.queryAll();
        PlaceAreaEnum[] placeAreaEnums = PlaceAreaEnum.values();
        List<Country> countryList = countryService.queryListByExample(new Country(), null);
        model.addAttribute("productList", productList);
        model.addAttribute("placeAreaEnums", placeAreaEnums);
        model.addAttribute("userAsset", userAsset);
        model.addAttribute("bankAccountName", bankAccountName);
        model.addAttribute("recommend", recommend);
        model.addAttribute("parent", parent);
        model.addAttribute("productId", productId);
        model.addAttribute("idCard", idCard);
        model.addAttribute("ZCJFYYE", ZCJFYYE);
        model.addAttribute("DTFTJJYE", DTFTJJYE);
        model.addAttribute("countryList", countryList);

        if (productService.queryByKey(productId) == null){
            tip = "选择的等级不存在";
        }

        if (StringUtils.isEmpty(tradePassword))
            tip = "交易密码不能为空";
        else if (!bCryptPasswordEncoder.matches(tradePassword, loginUser.getPassword1())){
            tip = "交易密码不正确";
        }

        if (StringUtils.isEmpty(idCard))
            tip = "身份证号不能为空";

        if (StringUtils.isEmpty(bankAccountName))
            tip = "真实姓名不能为空";
        else if (bankAccountName.length() > 30)
            tip = "真实姓名长度不能超过30";

        if (StringUtils.isEmpty(user.getMobile()))
            tip = "手机号码不能为空";

        if (ZCJFYYE == null){
            tip = "注册积分不能为空";
        }else if(BigDecimal.ZERO.compareTo(ZCJFYYE) == 1){
            tip = "注册积分不能小于0";
        }

        if (DTFTJJYE == null){
            tip = "报单积分不能为空";
        }else if (BigDecimal.ZERO.compareTo(DTFTJJYE) == 1){
            tip = "报单积分不能小于0";
        }

        if (ZCJFYYE.add(DTFTJJYE).compareTo(productService.queryByKey(productId).getInvested()) != 0){
            tip = "注册积分加报单积分必须等于申请级别所需金额";
        }

        if (userAsset.getZCJFYYE().compareTo(ZCJFYYE) == -1){
            tip = "注册积分余额不足";
        }

        if(userAsset.getDTFTJJYE().compareTo(DTFTJJYE) == -1){
            tip = "报单积分余额不足";
        }

        if (!StringUtils.isEmpty(user.getPassword()) && !StringUtils.isEmpty(user.getPassword1()) && user.getPassword().equals(user.getPassword1())){
            FieldError fieldError = new FieldError("user", "password1", "登陆密码和安全密码不能相同");
            bindingResult.addError(fieldError);
        }

        if (StringUtils.isEmpty(recommend)){
            FieldError fieldError = new FieldError("user", "recommendUserId", "推荐人不能为空");
            bindingResult.addError(fieldError);
        }else {
            User recommendUser = userService.queryByName(recommend);
            if (recommendUser == null){
                FieldError fieldError = new FieldError("user", "recommendUserId", "推荐人不存在");
                bindingResult.addError(fieldError);
            }else {
                user.setRecommendUserId(recommendUser.getUserId());
            }
        }

        if (StringUtils.isEmpty(parent)){
            FieldError fieldError = new FieldError("user", "parentUserId", "安置人不能为空");
            bindingResult.addError(fieldError);
        }else {
            User parentUser = userService.queryByName(parent);
            if (parentUser == null){
                FieldError fieldError = new FieldError("user", "parentUserId", "安置人不存在");
                bindingResult.addError(fieldError);
            }else {
                user.setParentUserId(parentUser.getUserId());
                if (userService.existByParentUserIdAndPlaceArea(parentUser.getUserId(), user.getPlaceArea())){
                    FieldError fieldError = new FieldError("user", "parentUserId", "安排区域已存在会员，请重新选择");
                    bindingResult.addError(fieldError);
                }
            }
        }

        if (bindingResult.hasFieldErrors())
            tip = bindingResult.getAllErrors().get(0).getDefaultMessage();

        if (!StringUtils.isEmpty(tip)){
            model.addAttribute("tip", tip);
            return "fore/reg";
        }

        try {
            String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
            user.setIp(ip);
            userService.addUser(user, productId, loginUserId, bankAccountName, idCard, ZCJFYYE, DTFTJJYE);
        }catch (Exception e){
            model.addAttribute("tip", e.getMessage());
            return "fore/reg";
        }

        model.addAttribute("tip", "注册成功");
        return "fore/reg";
    }

    @RequestMapping("/fore/login")
    public String login(HttpServletRequest request, Model model){

        if (request.getSession().getAttribute("lang") == null)
            request.getSession().setAttribute("lang", "en_");

        //Notice notice = JSONObject.parseObject(sysConfigService.queryByKey(request.getSession().getAttribute("lang") + "notice").getValue(), Notice.class);
        Notice notice = JSONObject.parseObject(sysConfigService.queryByKey("notice").getValue(), Notice.class);
        SharesConfig sharesConfig = sharesConfigService.getConfig();
        model.addAttribute("notice", notice);
        model.addAttribute("enabledLogin", sharesConfig.getEnabledLogin());

        return "fore/login";
    }

    @RequestMapping("/fore/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @RequestMapping("/fore/index")
    public String index(Model model, HttpServletRequest request){

        String lang = (String) request.getSession().getAttribute("lang");

        String username = ((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        User user = userService.queryByName(username);
        NetUser netUser = netUserService.queryByUserId(user.getUserId());

        User recommendUser = null;
        if (user.getRecommendUserId() != null)
            recommendUser = userService.queryByKey(user.getRecommendUserId());

        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());

        Page page = new Page();
        page.setPageSize(5);
        ShareBuy buyQuery = new ShareBuy();
        List<ShareBuy> userBuyList = shareBuyService.queryListByExample(buyQuery, page);

        ShareSell sellQuery = new ShareSell();
        List<ShareSell> userSellList = shareSellService.queryListByExample(sellQuery, page);

        List<TodayPrice> todayPrice = functionMapper.callP_GetTodaySharePrice();
        todayPrice.forEach(p -> p.setName(StringUtils.isEmpty(internationalConfig.get(lang + p.getName())) ? p.getName() : internationalConfig.get(lang + p.getName())));

        BigDecimal prePrice = sharesPriceService.queryByMaxId().getPrice();

        BigDecimal[] trend = sharesPriceService.priceTrend(20);
        String[] trendDate = DateUtil.dateToStr(20, "MM-dd");

        News news = new News();
        news.setIsDelete(0);
        news.setLang(StringUtils.isEmpty(lang) ? "cn_" : lang);
        List<News> newsList = newsService.queryListByExample(news, new Page());

        SharesNum dtSharesNum = functionMapper.callP_GetUserCanSellShares_DT(user.getUserId());
        SharesNum jtSharesNum = functionMapper.callP_GetUserCanSellShares_JT(user.getUserId());

        BigDecimal sellSurplus = shareTradeService.sellSurplus(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("recommendUser", recommendUser == null? new User() : recommendUser);
        model.addAttribute("userAsset", userAsset == null ? new UserAsset() : userAsset);
        model.addAttribute("netUser", netUser == null ? new NetUser() : netUser);
        model.addAttribute("userBuyList", userBuyList);
        model.addAttribute("userSellList", userSellList);
        model.addAttribute("todayPrice", todayPrice);
        model.addAttribute("prePrice", prePrice);
        model.addAttribute("trend", trend);
        model.addAttribute("trendDate", trendDate);
        model.addAttribute("newsList", newsList);
        model.addAttribute("dtSharesNum", dtSharesNum);
        model.addAttribute("jtSharesNum", jtSharesNum);
        model.addAttribute("sellSurplus", sellSurplus);
        return "fore/index";
    }

    @RequestMapping("/fore/index/getBanners")
    @ResponseBody
    public List<Banners> getBanners(HttpServletRequest request){

        List<Banners> bannersList = bannersService.queryListByExample(new Banners(), null);
        NetUser netUser = netUserService.queryByUserId((BigDecimal)request.getSession().getAttribute("userId"));

        return bannersList;
    }

    @RequestMapping("/fore/index/getChild")
    @ResponseBody
    public Set<User> getChild(HttpServletRequest request){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");

        List<UserAccount> parents = userAccountService.queryListByChild(userId);
        List<UserAccount> accounts = userAccountService.queryListByUserId(userId);

        Set<User> users = new HashSet<>();

        parents.forEach(account -> accounts.addAll(userAccountService.queryListByUserId(account.getUserId())));

        accounts.forEach(account -> {
            User user = userService.queryByKey(account.getClientUserId());
            User puser = userService.queryByKey(account.getUserId());
            if (user != null){
                users.add(user);
                users.add(puser);
            }
        });

        users.remove(userService.queryByKey(userId));

        return users;
    }
}
