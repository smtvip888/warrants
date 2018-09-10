package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareTrade;
import com.sparetime.domian.User;
import com.sparetime.service.ShareTradeService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/shareTrade")
public class ShareTradeController {

    @Autowired
    ShareTradeService shareTradeService;

    @Autowired
    UserService userService;

    @RequestMapping("/list")
    public String list(ShareTrade query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }

        List<ShareTrade> shareTradeList = shareTradeService.queryListByExample(query, page);
        List<String> userNameList = new ArrayList<>();

        shareTradeList.forEach(shareTrade -> {
            User user = userService.queryByKey(shareTrade.getUserId());
            userNameList.add(user == null ? null : user.getUserName());
        });

        model.addAttribute("shareTradeList", shareTradeList);
        model.addAttribute("userName", userName);
        model.addAttribute("userNameList", userNameList);
        model.addAttribute("page", page);
        return "admin/share_trade/list";
    }
}
