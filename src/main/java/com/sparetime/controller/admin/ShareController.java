package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Share;
import com.sparetime.domian.User;
import com.sparetime.service.ShareService;
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
@RequestMapping("/admin/share")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @Autowired
    private UserService userService;
    private List<String> userNameList;

    @RequestMapping("/list")
    public String list(Share query, Page page, String userName, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }

        List<Share> shareList = shareService.queryListByExample(query, page);
        List<String> userNameList = new ArrayList<>();

        shareList.forEach(share -> {
            User user = userService.queryByKey(share.getUserId());
            userNameList.add(user == null ? null : user.getUserName());
        });

        model.addAttribute("shareList", shareList);
        model.addAttribute("page", page);
        model.addAttribute("userName", userName);
        model.addAttribute("userNameList", userNameList);
        return "admin/share/list";
    }
}
