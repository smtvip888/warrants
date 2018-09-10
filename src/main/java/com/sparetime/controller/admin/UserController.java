package com.sparetime.controller.admin;

import com.sparetime.common.cons.PlaceAreaEnum;
import com.sparetime.common.cons.UserStatusEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.domian.*;
import com.sparetime.domian.extend.UserGuide;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ProductService productService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private CountryService countryService;


    @RequestMapping("/list")
    public String list(User query, Page page, Model model){

        List<User> userList = userService.queryListByExample(query, page);
        List<UserAsset> userAssetList = new ArrayList<>(userList.size());
        List<String> recommendUserNameList = new ArrayList<>(userList.size());
        List<String> parentUserNameList = new ArrayList<>(userAssetList.size());

        if (!CollectionUtils.isEmpty(userList)){
            userList.forEach(user -> {
                UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
                userAssetList.add(userAsset == null ? new UserAsset() : userAsset);
                user.setStatus(UserStatusEnum.getStatusByCode(user.getStatus()).name());

                User recommendUser = userService.queryByKey(user.getRecommendUserId());
                User parentUser = userService.queryByKey(user.getParentUserId());
                recommendUserNameList.add(recommendUser == null ? null : recommendUser.getUserName());
                parentUserNameList.add(parentUser == null ? null : parentUser.getUserName());
            });
        }

        model.addAttribute("userList", userList);
        model.addAttribute("userAssetList", userAssetList);
        model.addAttribute("query", query);
        model.addAttribute("recommendUserNameList", recommendUserNameList);
        model.addAttribute("parentUserNameList", parentUserNameList);
        model.addAttribute("page", page);
        return "admin/user/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(User query, Model model){

        User user = userService.queryByKey(query.getUserId());
        UserStatusEnum[] statusList = UserStatusEnum.values();
        List<Country> countryList = countryService.queryListByExample(new Country(), null);
        UserProfile profile = userProfileService.queryByUserId(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("statusList", statusList);
        model.addAttribute("countryList", countryList);
        model.addAttribute("profile", profile);

        return "admin/user/update";
    }

    @RequestMapping("/update")
    public String update(User user, String idCard, String bankAccountName,  Model model){

        UserProfile profile = userProfileService.queryByUserId(user.getUserId());
        if (!StringUtils.isEmpty(idCard)) profile.setIdCard(idCard);
        if (!StringUtils.isEmpty(bankAccountName)) profile.setBankAccountName(bankAccountName);

        userService.updateSelective(user, profile);
        return "redirect:/admin/user/list";
    }

    @RequestMapping("/info")
    public String info(BigDecimal userId, Model model){

        User user = userService.queryByKey(userId);
        UserAsset userAsset = userAssetService.queryByUserId(userId);
        UserProfile userProfile = userProfileService.queryByUserId(userId);
        NetUser netUser = netUserService.queryByUserId(userId);

        User recommendUser = null;
        User parentUser = null;

        if (user != null){
            recommendUser = userService.queryByKey(user.getRecommendUserId());
            parentUser = userService.queryByKey(user.getParentUserId());
        }

        UserStatusEnum status = UserStatusEnum.getStatusByCode(user.getStatus());
        PlaceAreaEnum placeArea = PlaceAreaEnum.getPlaceAreaEnumByCode(user.getPlaceArea());

        model.addAttribute("user", user);
        model.addAttribute("userAsset", userAsset == null ? new UserAsset() : userAsset);
        model.addAttribute("userProfile", userProfile == null ? new UserProfile() : userProfile);
        model.addAttribute("status", status);
        model.addAttribute("placeArea", placeArea);
        model.addAttribute("recommendUser", recommendUser == null ? new User() : recommendUser);
        model.addAttribute("parentUser", parentUser == null ? new User() : parentUser);
        model.addAttribute("netUser", netUser == null ? new NetUser() : netUser);
        return "admin/user/info";
    }

    @RequestMapping("/toAdd")
    public String toAdd(User user, Model model){

        UserStatusEnum[] statusEnums = UserStatusEnum.values();
        PlaceAreaEnum[] placeAreaEnums = PlaceAreaEnum.values();
        List<Product> productList = productService.queryAll();
        List<Country> countryList = countryService.queryListByExample(new Country(), null);

        model.addAttribute("statusEnums", statusEnums);
        model.addAttribute("placeAreaEnums", placeAreaEnums);
        model.addAttribute("productList", productList);
        model.addAttribute("countryList", countryList);
        return "admin/user/add";
    }

    @RequestMapping("/add")
    public String add(@Valid User user, BindingResult bindingResult, String recommendUserSN,
                      String parentUserSN, BigDecimal productId, Model model){

        UserStatusEnum[] statusEnums = UserStatusEnum.values();
        PlaceAreaEnum[] placeAreaEnums = PlaceAreaEnum.values();
        List<Product> productList = productService.queryAll();
        List<Country> countryList = countryService.queryListByExample(new Country(), null);

        model.addAttribute("statusEnums", statusEnums);
        model.addAttribute("placeAreaEnums", placeAreaEnums);
        model.addAttribute("productList", productList);
        model.addAttribute("recommendUserSN", recommendUserSN);
        model.addAttribute("parentUserSN", parentUserSN);
        model.addAttribute("countryList", countryList);

        if (bindingResult.hasFieldErrors()){
            return "admin/user/add";
        }

        String filed = "";
//        if (StringUtils.isEmpty(recommendUserSN)){
//            filed = filed + " 推荐人会员编号不能为空";
//        }else {
//            BigDecimal recommendUserId = userService.getUserIdByUserSN(recommendUserSN);
//            if (recommendUserId.intValue() == -1){
//                filed = filed + " 推荐人会员不存在";
//            }
//            user.setRecommendUserId(recommendUserId);
//        }
//        if (StringUtils.isEmpty(parentUserSN)){
//            filed = filed + " 安排会员编号不能为空";
//        }else {
//            BigDecimal parentUserId = userService.getUserIdByUserSN(parentUserSN);
//            if (parentUserId.intValue() == -1){
//                filed = filed + " 安排会员不存在";
//            } else if (userService.existByParentUserIdAndPlaceArea(parentUserId, user.getPlaceArea())){
//                filed = filed + " 安排会员编号:" + parentUserSN +
//                        "区域：" + (user.getPlaceArea() == '0' ? "左区" : "右区") + "已存在，请重新选择";
//            }
//
//            user.setParentUserId(parentUserId);
//        }

        if (!StringUtils.isEmpty(filed)){
            model.addAttribute("filed", filed);
            return "admin/user/add";
        }
        try {
            userService.addUser(user, productId, null, null, null, null, null);
        }catch (Exception e){
            model.addAttribute("filed", e.getMessage());
            return "admin/user/add";
        }
        return "redirect:/admin/user/list";
    }
}