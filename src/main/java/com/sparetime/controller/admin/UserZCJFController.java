package com.sparetime.controller.admin;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.DateUtil;
import com.sparetime.domian.User;
import com.sparetime.domian.UserZCJF;
import com.sparetime.service.UserAssetService;
import com.sparetime.service.UserService;
import com.sparetime.service.UserZCJFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/8/14.
 */
@Controller
@RequestMapping("/admin/userZCJF")
public class UserZCJFController {

    @Autowired
    private UserZCJFService userZCJFService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAssetService userAssetService;

    @RequestMapping("/list")
    public String list(UserZCJF query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }

        List<UserZCJF> list = userZCJFService.queryListByExample(query, page);

        CashBonusInOrOutEnum[] cashBonusInOrOutEnums = CashBonusInOrOutEnum.values();
        List<String> fromUserNameList = new ArrayList<>();
        List<String> userNameList = new ArrayList<>();

        list.forEach(userZCJF -> {
            User user = userService.queryByKey(userZCJF.getUserId());
            User fromUser = userService.queryByKey(userZCJF.getFromUserId());
            userNameList.add(user != null ? user.getUserName(): null);
            fromUserNameList.add(fromUser == null ? null : fromUser.getUserName());

        });

        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        model.addAttribute("page", page);
        model.addAttribute("userNameList", userNameList);
        model.addAttribute("cashBonusInOrOutEnums", cashBonusInOrOutEnums);
        model.addAttribute("list", list);
        model.addAttribute("fromUserNameList", fromUserNameList);
        return "admin/user_ZCJF/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(UserZCJF userZCJF){

        return "admin/user_ZCJF/add";
    }

    @RequestMapping("/add")
    public String add(@Valid UserZCJF userZCJF, BindingResult bindingResult, String userName, Model model){

        User user = null;

        if (StringUtils.isEmpty(userName)){
            FieldError fieldError = new FieldError("userZCJF", "userId", "用户不能为空");
            bindingResult.addError(fieldError);
        }else if ((user = userService.queryByName(userName)) == null){
            FieldError fieldError = new FieldError("userZCJF", "userId", "用户不存在");
            bindingResult.addError(fieldError);
        }

        if (userZCJF.getAmount() != null && userZCJF.getAmount().compareTo(new BigDecimal(9999999)) == 1){
            FieldError fieldError = new FieldError("userZCJF", "amount", "最大充值金额9999999");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasFieldErrors()){
            return "admin/user_ZCJF/add";
        }

        userAssetService.addZCJF(user, BigDecimal.ZERO, "系统充值", userZCJF.getAmount());

        return "redirect:/admin/userZCJF/list";
    }

    @RequestMapping("/del")
    public String del(Long id) {

        UserZCJF query = new UserZCJF();
        query.setId(id);
        UserZCJF userZCJF = userZCJFService.queryListByExample(query, null).get(0);
        if (userZCJF.getIsdel() == 1)
            throw new RuntimeException("改记录已撤销");
        if (!"系统充值".equals(userZCJF.getRemark()))
            throw new RuntimeException("非系统充值的记录不能撤销");
        if (LocalDateTime.now().minusHours(12).isAfter(DateUtil.dateToLocalDateTime(userZCJF.getCdate()))){
            throw new RuntimeException("已过撤销时间");
        }

        userAssetService.delZCJF(userZCJF);
        return "redirect:/admin/userZCJF/list";
    }
}
