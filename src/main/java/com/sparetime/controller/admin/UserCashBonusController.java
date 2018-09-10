package com.sparetime.controller.admin;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.BonusType;
import com.sparetime.domian.User;
import com.sparetime.domian.UserCashBonus;
import com.sparetime.service.BonusTypeService;
import com.sparetime.service.UserCashBonusService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Controller
@RequestMapping("/admin/userCashBonus")
public class UserCashBonusController {

    @Autowired
    private UserCashBonusService userCashBonusService;

    @Autowired
    private UserService userService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @RequestMapping("/list")
    public String list(UserCashBonus query, String userSN, Page page, Model model){

        if (!StringUtils.isEmpty(userSN)){
            query.setUserId(userService.getUserIdByUserSN(userSN));
        }

        List<UserCashBonus> userCashBonusList = userCashBonusService.queryListByExample(query, page);
        TradeTypeEnum[] tradeTypeEnums = TradeTypeEnum.values();
        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<BonusType> bonusTypeList = bonusTypeService.queryAll();

        List<String> userSNList = new ArrayList<>();
        List<String> fromUserSNList = new ArrayList<>();

        userCashBonusList.forEach(userCashBonus -> {
            User user = userService.queryByKey(userCashBonus.getUserId());
            User fromUser = userService.queryByKey(userCashBonus.getUserId());
            userSNList.add(user == null ? null : user.getUserSN());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
        });

        model.addAttribute("query", query);
        model.addAttribute("userSN", userSN);
        model.addAttribute("page", page);
        model.addAttribute("userCashBonusList", userCashBonusList);
        model.addAttribute("userSNList", userSNList);
        model.addAttribute("tradeTypeEnums", tradeTypeEnums);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("bonusTypeList", bonusTypeList);
        model.addAttribute("fromUserSNList", fromUserSNList);
        return "admin/user_cash_bonus/list";
    }
}
