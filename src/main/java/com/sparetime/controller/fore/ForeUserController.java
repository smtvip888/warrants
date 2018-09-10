package com.sparetime.controller.fore;

import com.sparetime.common.cons.PlaceAreaEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.CommonUtil;
import com.sparetime.common.util.DateUtil;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.domian.*;
import com.sparetime.domian.extend.RemmendUser;
import com.sparetime.domian.extend.UserTree;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
@Controller
@RequestMapping("/fore/user")
public class ForeUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SharesConfigService sharesConfigService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserCashBonusGetService userCashBonusGetService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CountryService countryService;


    @RequestMapping("/search")
    public String search(HttpServletRequest request, String userName, Model model){

        User user = null;

        if (!StringUtils.isEmpty(userName)){
            user = userService.queryByName(userName);
            if (user != null){
                String ids = functionMapper.callP_GetParentUserIds(user.getUserId());
                boolean isInParant = false;
                if (!StringUtils.isEmpty(ids)){
                    for (String id : ids.split(",")){
                        if (new BigDecimal(id).compareTo((BigDecimal) request.getSession().getAttribute("userId")) == 0){
                            isInParant = true;
                            break;
                        }
                    }
                }
                if (!isInParant){
                    model.addAttribute("tip", "查询用户不在你的团队！");
                    return "fore/user/search";
                }
            }
        }
        else
            user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));


        UserTree tree = userService.getParentUser(user, 3);

        model.addAttribute("tree", tree);
        model.addAttribute("userName", userName);
        return "fore/user/search";
    }

    @RequestMapping("/toUpgrade")
    public String toUpgrade(HttpServletRequest request, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
        NetUser netUser = netUserService.queryByUserId(user.getUserId());
        Product product = productService.queryByKey(netUser.getProductId());
        List<Product>  productList = productService.queryAll();

        model.addAttribute("user", user);
        model.addAttribute("userAsset", userAsset);
        model.addAttribute("netUser", netUser);
        model.addAttribute("product", product);
        model.addAttribute("productList", productList);
        return "fore/user/upgrade";
    }

    @RequestMapping("/upgrade")
    public String upgrade(HttpServletRequest request, BigDecimal productId, String password, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
        NetUser netUser = netUserService.queryByUserId(user.getUserId());
        Product product = productService.queryByKey(netUser.getProductId());
        List<Product>  productList = productService.queryAll();

        String filed = "";

        SharesConfig sharesConfig = sharesConfigService.getConfig();
        long days = Duration.between(DateUtil.dateToLocalDateTime(user.getRegDate()), LocalDateTime.now()).toDays();
        if (sharesConfig.getHYSJQX() < days)
            filed = "已超出升级期限";

        if (user.getUpgraded() == 1){
            filed = "你已经升级过了";
        }

        if (password == null)
            filed = "交易密码不能为空";
        else if (!bCryptPasswordEncoder.matches(password, user.getPassword1()))
            filed = "交易密码不正确";

        if (StringUtils.isEmpty(filed)){
            try {
                userService.upgrade(user.getUserId(), productId);
                filed = "升级成功";
            }catch (Exception e){
                filed = e.getMessage();
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("userAsset", userAsset);
        model.addAttribute("netUser", netUser);
        model.addAttribute("product", product);
        model.addAttribute("productList", productList);
        model.addAttribute("filed", filed);

        return "fore/user/upgrade";
    }

    @RequestMapping("/msgList")
    public String msgList(Message query, int queryType, HttpServletRequest request, Page page, Model model){

        BigDecimal userId = (BigDecimal) request.getSession().getAttribute("userId");

        if (queryType == 0)
            query.setToUserId(userId);
        else
            query.setSendUserId(userId);

        List<Message> list = messageService.queryListByExample(query, page);

        model.addAttribute("list", list);
        model.addAttribute("queryType", queryType);
        model.addAttribute("page", page);
        model.addAttribute("query", query);

        return "fore/user/msg_list";
    }

    @RequestMapping("/toSendMsg")
    public String toSendMsg(Message message, Model model){
        return "fore/user/send_msg";
    }

    @RequestMapping("/sendMsg")
    public String sendMsg(@Valid Message message, BindingResult bindingResult, HttpServletRequest request, Model model){

        if ("<p><br></p>".equals(message.getBody())){
            FieldError error = new FieldError("message", "body", "内容不能为空");
            bindingResult.addError(error);
        }

        if (bindingResult.hasFieldErrors()){
            model.addAttribute("tip", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "fore/user/send_msg";
        }

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        message.setIp(ip);
        message.setSendUserId((BigDecimal) request.getSession().getAttribute("userId"));
        message.setToUserId(BigDecimal.ZERO);
        messageService.insert(message);

        return "redirect:/fore/user/msgList?queryType=1";
    }

    @RequestMapping("/toChangeProfile")
    public String toChangeProfile(HttpServletRequest request, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        UserProfile userProfile = userProfileService.queryByUserId(user.getUserId());

        Country country = countryService.queryByKey(new BigDecimal(user.getRegCountry()));

        model.addAttribute("user", user);
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("country", country);
        return "fore/user/change_profile";
    }

    @RequestMapping("/changeProfile")
    public String changeProfile(@Valid UserProfile userProfile, BindingResult proBindingResult, @Valid User user, BindingResult bindingResult, HttpServletRequest request, String birthdayStr, Model model){
        String tip = "";

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        User oldUser = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));

        user.setUserName(oldUser.getUserName());
        user.setRegDate(oldUser.getRegDate());

        if (StringUtils.isEmpty(user.getPassword1()))
            tip = "交易密码不能为空";
        else if (!bCryptPasswordEncoder.matches(user.getPassword1(), oldUser.getPassword1()))
            tip = "交易密码不正确";

        if (user.getMobile() == null)
            tip = "手机号码不能为空";

        if (user.getEmail() == null)
            tip = "email不能为空";

        if (!StringUtils.isEmpty(birthdayStr)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                userProfile.setBirthday(sdf.parse(birthdayStr));
            }catch (Exception e){
            }
        }

        user.setUserId(oldUser.getUserId());
        userProfile.setUserId(oldUser.getUserId());

        if (proBindingResult.hasFieldErrors())
            tip = proBindingResult.getAllErrors().get(0).getDefaultMessage();

        if (bindingResult.hasFieldErrors("email"))
            tip = bindingResult.getFieldError("email").getDefaultMessage();

        if (bindingResult.hasFieldErrors("mobile"))
            tip = bindingResult.getFieldError("mobile").getDefaultMessage();

        if (StringUtils.isEmpty(tip)){
            userService.changeProfile(user, userProfile, ip);
            tip = "修改成功";
        }

        Country country = countryService.queryByKey(new BigDecimal(oldUser.getRegCountry()));

        model.addAttribute("country", country);
        model.addAttribute("tip", tip);
        model.addAttribute("birthdayStr", birthdayStr);
        return "fore/user/change_profile";
    }

    @RequestMapping("/toChangeLoginPassword")
    public String toChangeLoginPassword(Model model){
        return "fore/user/change_login_password";
    }

    @RequestMapping("/changeLoginPassword")
    public String changeLoginPassword(String oldPassword, String password, String affirmPassword, HttpServletRequest request, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        String tip = "";
        if (!password.equals(affirmPassword))
            tip = "两次输入的密码不一致";

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
            tip = "旧密码不正确";

        if (bCryptPasswordEncoder.matches(password, user.getPassword1()))
            tip = "登录密码不能和交易密码一致";

        if (password.length() < 6 || password.length() > 20)
            tip = "密码长度必须在6-20之间";

        if (StringUtils.isEmpty(tip)){
            userService.changePassword(password, user.getUserId(), ip);
            tip = "修改成功";
        }

        model.addAttribute("tip", tip);
        return "fore/user/change_login_password";
    }

    @RequestMapping("/toChangeTradePassword")
    public String toChangeTradePassword(Model model){
        return "fore/user/change_trade_password";
    }

    @RequestMapping("/changeTradePassword")
    public String changeTradePassword(String oldPassword, String password, String affirmPassword, HttpServletRequest request, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        String tip = "";
        if (!password.equals(affirmPassword))
            tip = "两次输入的密码不一致";

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword1()))
            tip = "旧密码不正确";

        if (bCryptPasswordEncoder.matches(password, user.getPassword()))
            tip = "登录密码不能和交易密码一致";

        if (password.length() < 6 || password.length() > 20)
            tip = "密码长度必须在6-20之间";

        if (StringUtils.isEmpty(tip)){
            userService.changePassword1(password, user.getUserId(), ip);
            tip = "修改成功";
        }

        model.addAttribute("tip", tip);
        return "fore/user/change_trade_password";
    }

    @RequestMapping("/toActive")
    public String toActive(HttpServletRequest request, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));
        NetUser netUser = netUserService.queryByUserId(user.getUserId());
        List<Product> productList = productService.queryAll();

        User parentUser = userService.queryByKey(user.getParentUserId());
        User recommendUser = userService.queryByKey(user.getRecommendUserId());
        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());

        PlaceAreaEnum placeArea = PlaceAreaEnum.getPlaceAreaEnumByCode(user.getPlaceArea());

        model.addAttribute("netUser", netUser);
        model.addAttribute("productList", productList);
        model.addAttribute("parentUser", parentUser == null ? new User() : parentUser);
        model.addAttribute("recommendUser", recommendUser == null ? new User() : recommendUser);
        model.addAttribute("placeArea", placeArea);
        model.addAttribute("userAsset", userAsset);
        return "fore/user/active";
    }

    @RequestMapping("/active")
    public String active(HttpServletRequest request, BigDecimal productId, String password, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));

        NetUser netUser = netUserService.queryByUserId(user.getUserId());
        List<Product> productList = productService.queryAll();
        User parentUser = userService.queryByKey(user.getParentUserId());
        User recommendUser = userService.queryByKey(user.getRecommendUserId());
        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
        PlaceAreaEnum placeArea = PlaceAreaEnum.getPlaceAreaEnumByCode(user.getPlaceArea());

        String tip = "";
        if (StringUtils.isEmpty(password))
            tip = "交易密码不能为空";
        else if (!bCryptPasswordEncoder.matches(password, user.getPassword1()))
            tip = "交易密码不正确";

        if (StringUtils.isEmpty(tip)){
            try {
                userService.active(user.getUserId(), productId);
                tip = "激活成功";
            }catch (Exception e){
                tip = e.getMessage();
            }
        }

        model.addAttribute("netUser", netUser);
        model.addAttribute("productList", productList);
        model.addAttribute("parentUser", parentUser == null ? new User() : parentUser);
        model.addAttribute("recommendUser", recommendUser == null ? new User() : recommendUser);
        model.addAttribute("placeArea", placeArea);
        model.addAttribute("userAsset", userAsset);

        model.addAttribute("tip", tip);
        return "fore/user/active";
    }

    @RequestMapping("/getPhone")
    @ResponseBody
    public String getPhone(String userName){

        String phone = "";

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                phone = user.getMobile();
        }

        return phone;
    }

    @RequestMapping("/myRemmend")
    public String myRemmend(Model model){
        return "fore/user/my_remmend";
    }

    @RequestMapping("/getRecommendUsers")
    @ResponseBody
    public List<RemmendUser> getRecommendUsers(BigDecimal userId, HttpServletRequest request){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (userId == null)
            userId = (BigDecimal) request.getSession().getAttribute("userId");

        User query = new User();
        query.setRecommendUserId(userId);
        List<User> users = userService.queryListByExample(query, null);
        List<RemmendUser> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)){
            users.forEach(user -> {
                RemmendUser remmendUser = new RemmendUser();
                remmendUser.setUserId(user.getUserId());
                remmendUser.setUserName(user.getUserName());
                remmendUser.setStatus(user.getStatus());
                remmendUser.setRegDate(user.getRegDate());
                remmendUser.setRegTime(sdf.format(user.getRegDate()));
                NetUser netUser = netUserService.queryByUserId(user.getUserId());
                if (netUser != null){
                    Product product = productService.queryByKey(netUser.getProductId());
                    remmendUser.setLevel(product.getLevel());
                    remmendUser.setNetPrice(product.getInvested());
                }
                list.add(remmendUser);
            });
        }

        return list;
    }

    @RequestMapping("/toUsersAccounts")
    public String toUsersAccounts(HttpServletRequest request, Model model){

        List<UserAccount> userAccountList = userAccountService.queryListByUserId((BigDecimal) request.getSession().getAttribute("userId"));
        List<User> userList = new ArrayList<>();
        userAccountList.forEach(v -> {
            User user = userService.queryByKey(v.getClientUserId());
            userList.add(user == null ? new User() : user);
        });

        model.addAttribute("userList", userList);
        model.addAttribute("userAccountList", userAccountList);
        model.addAttribute("tip", request.getAttribute("tip"));
        return "fore/user/user_accounts";
    }

    @RequestMapping("/addUserAccount")
    public String addUserAccount(String userName, String password, HttpServletRequest request, Model model){

        String tip = "";
        User user = userService.queryByName(userName);
        if (user == null)
            tip = "子用户不存在";
        else if (password == null || !bCryptPasswordEncoder.matches(password, user.getPassword()))
            tip = "子用户登录密码不正确";

        if (StringUtils.isEmpty(tip)){
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId((BigDecimal) request.getSession().getAttribute("userId"));
            userAccount.setClientUserId(user.getUserId());
            userAccount.setStatus(0);
            try {
                userAccountService.add(userAccount);
            }catch (Exception e){
                tip = e.getMessage();
            }
        }

        if (StringUtils.isEmpty(tip)){
            return "redirect:/fore/user/toUsersAccounts";
        }

        model.addAttribute("tip", tip);
        model.addAttribute("userName", userName);
        model.addAttribute("password", password);
        return "forward:/fore/user/toUsersAccounts";
    }

    @RequestMapping("delUserAccount")
    public String delUserAccount(Long id, String password1, HttpServletRequest request, Model model){

        User user = userService.queryByKey((BigDecimal) request.getSession().getAttribute("userId"));

        String tip = "";
        if (StringUtils.isEmpty(password1))
            tip = "安全密码不能为空";
        else if (!bCryptPasswordEncoder.matches(password1, user.getPassword1()))
            tip = "安全密码错误";

        if (StringUtils.isEmpty(tip))
            userAccountService.delete(id);

        model.addAttribute("tip", tip);
        return "forward:/fore/user/toUsersAccounts";
    }
}
