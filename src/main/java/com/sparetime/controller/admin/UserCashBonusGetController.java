package com.sparetime.controller.admin;

import com.sparetime.common.cons.UserCashBonusGetEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.User;
import com.sparetime.domian.UserCashBonusGet;
import com.sparetime.service.UserCashBonusGetService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
@Controller
@RequestMapping("/admin/userCashBonusGet")
public class UserCashBonusGetController {

    @Autowired
    private UserCashBonusGetService userCashBonusGetService;

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public String list(UserCashBonusGet query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }
        List<UserCashBonusGet> userCashBonusGetList = userCashBonusGetService.queryListByExample(query, page);
        UserCashBonusGetEnum[] userCashBonusGetEnums = UserCashBonusGetEnum.values();

        List<String> userNameList = new ArrayList<>();

        userCashBonusGetList.forEach(userCashBonusGet -> {
            User user = userService.queryByKey(userCashBonusGet.getUserId());
            userNameList.add(user == null ? null : user.getUserName());
        });

        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        model.addAttribute("page", page);
        model.addAttribute("userCashBonusGetList", userCashBonusGetList);
        model.addAttribute("userCashBonusGetEnums", userCashBonusGetEnums);
        model.addAttribute("userNameList", userNameList);
        return "admin/user_cash_bonus_get/list";
    }

    @RequestMapping("/changeStatus")
    @ResponseBody
    public String changeStatus(Integer status, BigDecimal id){

        boolean isSuccessful = userCashBonusGetService.changeStatus(status, id);
        return isSuccessful ? "successful" : "failed";
    }
}
