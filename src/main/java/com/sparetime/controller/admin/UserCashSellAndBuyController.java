package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.*;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserCashSellAndBuyController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCashSellService userCashSellService;

    @Autowired
    private UserCashBuyService userCashBuyService;

    @Autowired
    private UserCashImageService userCashImageService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @RequestMapping("/admin/userCashSell/list")
    public String sellList(UserCashSell query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user == null){
                query.setUserId(new BigDecimal("-1"));
            }else {
                query.setUserId(user.getUserId());
            }
        }

        List<UserCashSell> list = userCashSellService.queryListByExample(query, null, null, page);
        List<User> sellUsers = new ArrayList<>();
        List<User> buyUsers = new ArrayList<>();

        list.forEach(s -> {
            User sellUser = userService.queryByKey(s.getUserId());
            User buyUser = userService.queryByKey(s.getBuyUserId());
            sellUsers.add(sellUser == null ? new User() : sellUser);
            buyUsers.add(buyUser == null ? new User() : buyUser);

        });

        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("sellUsers", sellUsers);
        model.addAttribute("buyUsers", buyUsers);

        return "admin/user_cash_sell/list";
    }

    @RequestMapping("/admin/userCashSell/info")
    public String sellInfo(BigDecimal sellId, Model model){

        UserCashSell sell = userCashSellService.queryByKey(sellId);
        UserCashBuy buy = userCashBuyService.queryByKey(sell.getBuyId());

        List<UserCashBuyImage> images = new ArrayList<>();
        User buyUser = new User();

        if (buy != null){
            images = userCashImageService.queryListByBuyId(buy.getBuyId());
            buyUser = userService.queryByKey(buy.getUserId());
        }else {
            buy = new UserCashBuy();
        }

        model.addAttribute("sell", sell);
        model.addAttribute("buy", buy);
        model.addAttribute("images", images);
        model.addAttribute("buyUser", buyUser);

        return "admin/user_cash_sell/info";
    }

    @RequestMapping("/admin/userCashBuy/list")
    public String buyList(UserCashBuy query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user == null){
                query.setUserId(new BigDecimal("-1"));
            }else {
                query.setUserId(user.getUserId());
            }
        }

        List<UserCashBuy> list = userCashBuyService.queryListByExample(query, page);

        List<User> sellUsers = new ArrayList<>();
        List<User> buyUsers = new ArrayList<>();

        list.forEach(b -> {
            User sellUser = userService.queryByKey(b.getSellUserId());
            User buyUser = userService.queryByKey(b.getUserId());
            sellUsers.add(sellUser == null ? new User() : sellUser);
            buyUsers.add(buyUser == null ? new User() : buyUser);

        });

        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("sellUsers", sellUsers);
        model.addAttribute("buyUsers", buyUsers);
        return "admin/user_cash_buy/list";
    }

    @RequestMapping("/admin/userCashBuy/toUpdate")
    public String toUpdate(BigDecimal id, Model model){

        UserCashBuy buy = userCashBuyService.queryByKey(id);
        model.addAttribute("buy", buy);

        return "admin/user_cash_buy/update";
    }

    @RequestMapping("/admin/userCashBuy/update")
    @Transactional
    public String update(UserCashBuy buy, String remark, HttpServletRequest request, Model model){

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        UserCashBuy old = userCashBuyService.queryByKey(buy.getBuyId());

        Log log = new Log();
        log.setIp(ip);
        log.setLogType(18);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改积分买入状态 编号" + old.getTradeSN() + " | 状态 " + old.getStatus() + "->" + buy.getStatus() + " | " + remark);
        log.setCdate(new Date());
        logService.insert(log);

        old.setStatus(buy.getStatus());
        old.setPayDate(null);
        userCashBuyService.update(old);
        return "redirect:/admin/userCashBuy/list";
    }

    @RequestMapping("/admin/userCashSell/toUpdate")
    public String sellToUpdate(BigDecimal id, Model model){

        UserCashSell sell = userCashSellService.queryByKey(id);
        model.addAttribute("sell", sell);
        return "admin/user_cash_sell/update";
    }

    @RequestMapping("/admin/userCashSell/update")
    public String sellUpdate(UserCashSell sell, String remark, HttpServletRequest request, Model model){
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        UserCashSell old = userCashSellService.queryByKey(sell.getSellId());

        Log log = new Log();
        log.setIp(ip);
        log.setLogType(18);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改积分卖出状态 编号" + old.getTradeSN() + " | 状态 " + old.getStatus() + "->" + sell.getStatus() + "|" + remark);
        log.setCdate(new Date());
        logService.insert(log);

        old.setStatus(sell.getStatus());
        old.setConfirmDate(null);
        userCashSellService.update(old);
        return "redirect:/admin/userCashSell/list";
    }
}
