package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.FunctionMapper;
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

/**
 * Created by muye on 17/7/24.
 */
@Controller
@RequestMapping("/admin/shareBuy")
public class ShareBuyController {

    @Autowired
    private ShareBuyService shareBuyService;

    @Autowired
    private UserService userService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Autowired
    private SharesPriceService sharesPriceService;

    @RequestMapping("/list")
    @Transactional
    public String list(ShareBuy query, String userName, Page page, HttpServletRequest request, Model model){

        if (!StringUtils.isEmpty(userName)){
            User user = userService.queryByName(userName);
            if (user != null)
                query.setUserId(user.getUserId());
            else
                query.setUserId(BigDecimal.ZERO);
        }

        if (query.getStatus() == null) query.setStatus(1);

        List<ShareBuy> shareBuyList = shareBuyService.queryListByExample(query, page);
        List<String> userNameList = new ArrayList<>();

        shareBuyList.forEach(shareBuy -> {
            User user = userService.queryByKey(shareBuy.getUserId());
            userNameList.add(user == null ? null : user.getUserName());
        });

        BigDecimal total = shareBuyService.sumBuyPrice(query);
        BigDecimal yzqz = total == null ? BigDecimal.ZERO : total.divide(sharesPriceService.queryListByExample(new SharesPrice(), null).get(0).getPrice(), 2);


        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        model.addAttribute("shareBuyList", shareBuyList);
        model.addAttribute("userNameList", userNameList);
        model.addAttribute("page", page);
        model.addAttribute("tip", request.getAttribute("tip"));
        model.addAttribute("total", total);
        model.addAttribute("yzqz", yzqz);

        return "admin/share_buy/list";
    }

    @RequestMapping("/updateOfBuy")
    public String buy(ShareBuy query, String userName, Integer num, HttpServletRequest request, Model model){

        String tip = "";

        if (num == null){
            tip = "购买条数不能为空";
        }

        if (num != null && num < 1){
            tip = "条数必须大于0";
        }

        if (StringUtils.isEmpty(tip)){
            functionMapper.callP_Shares_Auto_Buy(num);


            String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
            Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));

            Log log = new Log();
            log.setLogType(16);
            log.setUserType(2);
            log.setUserName(manager.getName());
            log.setRemark("权证买入 " + num);
            log.setIp(ip);
            log.setCdate(new Date());

            logService.insert(log);

            tip = "执行成功";
        }

        model.addAttribute("tip", tip);
        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        return "forward:/admin/shareBuy/list";
    }
}
