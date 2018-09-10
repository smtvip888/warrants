package com.sparetime.controller.admin;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.BonusType;
import com.sparetime.domian.User;
import com.sparetime.domian.UserDTFTBonus;
import com.sparetime.service.BonusTypeService;
import com.sparetime.service.UserDTFTBonusService;
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
@RequestMapping("/admin/userDTFTBonus")
public class UserDTFTBonusController {

    @Autowired
    private UserDTFTBonusService userDTFTBonusService;

    @Autowired
    private UserService userService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @RequestMapping("/list")
    public String list(UserDTFTBonus query, String userSN, Page page, Model model){

        if (!StringUtils.isEmpty(userSN)){
            query.setUserId(userService.getUserIdByUserSN(userSN));
        }
        List<UserDTFTBonus> userDTFTBonusList = userDTFTBonusService.queryListByExample(query, page);
        TradeTypeEnum[] tradeTypeEnums = TradeTypeEnum.values();
        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<BonusType> bonusTypeList = bonusTypeService.queryAll();

        List<String> userSNList = new ArrayList<>();
        List<String> fromUserSNList = new ArrayList<>();

        userDTFTBonusList.forEach(userDTFTBonus -> {
            User user = userService.queryByKey(userDTFTBonus.getUserId());
            User fromUser = userService.queryByKey(userDTFTBonus.getFromUserId());
            userSNList.add(user == null ? null : user.getUserSN());
            fromUserSNList.add(fromUser == null ? null : fromUser.getUserSN());
        });

        model.addAttribute("query", query);
        model.addAttribute("userSN", userSN);
        model.addAttribute("page", page);
        model.addAttribute("userDTFTBonusList", userDTFTBonusList);
        model.addAttribute("userSNList", userSNList);
        model.addAttribute("tradeTypeEnums", tradeTypeEnums);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("bonusTypeList", bonusTypeList);
        model.addAttribute("fromUserSNList", fromUserSNList);
        return "admin/user_DTFT_bonus/list";
    }
}
