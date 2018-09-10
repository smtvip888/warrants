package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareSell;
import com.sparetime.domian.User;
import com.sparetime.service.ShareSellService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Controller
@RequestMapping("/admin/shareSell")
public class ShareSellController {

    @Autowired
    private ShareSellService shareSellService;

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public String list(ShareSell query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }

        List<ShareSell> shareSellList = shareSellService.queryListByExample(query, page);
        List<String> userNameList = new ArrayList<>();

        shareSellList.forEach(sell -> {
            User user = userService.queryByKey(sell.getUserId());
            userNameList.add(user == null ? null : user.getUserName());
        });

        model.addAttribute("shareSellList", shareSellList);
        model.addAttribute("userName", userName);
        model.addAttribute("userNameList", userNameList);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        return "admin/share_sell/list";
    }
}
