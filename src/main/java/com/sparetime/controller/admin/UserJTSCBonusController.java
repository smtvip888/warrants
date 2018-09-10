package com.sparetime.controller.admin;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.BonusType;
import com.sparetime.domian.User;
import com.sparetime.domian.UserJTSCBonus;
import com.sparetime.service.BonusTypeService;
import com.sparetime.service.UserJTSCBonusService;
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
@RequestMapping("/admin/userJTSCBonus")
public class UserJTSCBonusController {

    @Autowired
    private UserJTSCBonusService userJTSCBonusService;

    @Autowired
    private UserService userService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @RequestMapping("/list")
    public String list(UserJTSCBonus query, String userSN, Page page, Model model){

        if (!StringUtils.isEmpty(userSN)){
            query.setUserId(userService.getUserIdByUserSN(userSN));
        }
        List<UserJTSCBonus> userJTSCBonusList = userJTSCBonusService.queryListByExample(query, page);

        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<BonusType> bonusTypeList = bonusTypeService.queryAll();
        List<String> fromUserSNList = new ArrayList<>();
        TradeTypeEnum[] tradeTypeEnums = TradeTypeEnum.values();
        List<String> userSNList = new ArrayList<>();

        userJTSCBonusList.forEach(userJTSCBonus -> {
            User fromUser = userService.queryByKey(userJTSCBonus.getFromUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
            User user = userService.queryByKey(userJTSCBonus.getUserId());
            userSNList.add(user == null ? null : user.getUserSN());
        });

        model.addAttribute("query", query);
        model.addAttribute("userSN", userSN);
        model.addAttribute("page", page);
        model.addAttribute("userJTSCBonusList", userJTSCBonusList);
        model.addAttribute("userSNList", userSNList);
        model.addAttribute("tradeTypeEnums", tradeTypeEnums);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("bonusTypeList", bonusTypeList);
        model.addAttribute("fromUserSNList", fromUserSNList);
        return "admin/user_JTSC_bonus/list";
    }
}
