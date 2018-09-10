package com.sparetime.controller.admin;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.cons.TradeTypeEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.BonusType;
import com.sparetime.domian.User;
import com.sparetime.domian.UserAsset;
import com.sparetime.domian.UserBonus;
import com.sparetime.service.BonusTypeService;
import com.sparetime.service.UserAssetService;
import com.sparetime.service.UserBonusService;
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
 * Created by muye on 17/8/22.
 */
@Controller
@RequestMapping("/admin/userBonus")
public class UserBonusController {

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private UserService userService;

    @Autowired
    private BonusTypeService bonusTypeService;

    @Autowired
    private UserAssetService userAssetService;

    @RequestMapping("/list")
    public String list(UserBonus query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }

        List<UserBonus> list = userBonusService.queryListByExample(query, page);

        List<String> fromUserNameList = new ArrayList<>();
        List<String> userNameList = new ArrayList<>();
        list.forEach(userCashBonus -> {
            User fromUser = userService.queryByKey(userCashBonus.getFromUserId());
            User user = userService.queryByKey(userCashBonus.getUserId());
            fromUserNameList.add(fromUser == null ? null : fromUser.getUserName());
            userNameList.add(user == null ? null : user.getUserName());
        });

        TradeTypeEnum[] tradeTypeEnums = TradeTypeEnum.values();
        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<BonusType> bonusTypeList = bonusTypeService.queryAll();

        model.addAttribute("query", query);
        model.addAttribute("list", list);
        model.addAttribute("userName", userName);
        model.addAttribute("page", page);
        model.addAttribute("fromUserNameList", fromUserNameList);
        model.addAttribute("tradeTypeEnums", tradeTypeEnums);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("bonusTypeList", bonusTypeList);
        model.addAttribute("userNameList", userNameList);
        return "admin/user_bonus/list";
    }

    @RequestMapping("/getLYJFYE")
    @ResponseBody
    public BigDecimal getLYJFYE(String userName){
        if (StringUtils.isEmpty(userName))
            return null;

        User user = userService.queryByName(userName);
        if (user == null)
            return null;

        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
        if (userAsset == null)
            return null;

        return userAsset.getLYJFYE();
    }

    @RequestMapping("/toDelete")
    public String toDelete(){

        return "admin/user_bonus/delete";
    }

    @RequestMapping("/delete")
    public String delete(String userName, BigDecimal amount, String remark, Model model){

        String tip = "";
        try {
            userAssetService.deductLYJFYE(userName, amount, remark);
        }catch (Exception e){
            tip = e.getMessage();
        }

        if (!StringUtils.isEmpty(tip)){
            model.addAttribute("tip", tip);
            model.addAttribute("userName", userName);
            model.addAttribute("amount", amount);
            model.addAttribute("remark", remark);
            return "admin/user_bonus/delete";
        }

        return "redirect:/admin/userBonus/list";
    }
}
