package com.sparetime.controller.admin;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.BonusType;
import com.sparetime.domian.User;
import com.sparetime.domian.UserJTFTBonus;
import com.sparetime.service.BonusTypeService;
import com.sparetime.service.UserJTFTBonusService;
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
@RequestMapping("/admin/userJTFTBonus")
public class UserJTFTBonusController {

    @Autowired
    private UserJTFTBonusService userJTFTBonusService;

    @Autowired
    private UserService userService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @RequestMapping("/list")
    public String list(UserJTFTBonus query, String userSN, Page page, Model model){

        if (!StringUtils.isEmpty(userSN)){
            query.setUserId(userService.getUserIdByUserSN(userSN));
        }
        List<UserJTFTBonus> userJTFTBonusList = userJTFTBonusService.queryListByExample(query, page);

        TradeTypeEnum[] tradeTypeEnums = TradeTypeEnum.values();
        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<BonusType> bonusTypeList = bonusTypeService.queryAll();
        List<String> fromUserSNList = new ArrayList<>();

        List<String> userSNList = new ArrayList<>();
        userJTFTBonusList.forEach(userJTFTBonus -> {
            User fromUser = userService.queryByKey(userJTFTBonus.getFromUserId());
            User user = userService.queryByKey(userJTFTBonus.getUserId());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
            userSNList.add(user == null ? null : user.getUserSN());
        });

        model.addAttribute("query", query);
        model.addAttribute("userSN", userSN);
        model.addAttribute("page", page);
        model.addAttribute("userJTFTBonusList", userJTFTBonusList);
        model.addAttribute("userSNList", userSNList);
        model.addAttribute("tradeTypeEnums", tradeTypeEnums);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("bonusTypeList", bonusTypeList);
        model.addAttribute("fromUserSNList", fromUserSNList);
        return "admin/user_JTFT_bonus/list";
    }
}
