package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.*;
import com.sparetime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/17.
 */
@Controller
public class LoginController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private SharesStatisticsService sharesStatisticsService;

    @RequestMapping("/admin/login")
    public String login(){

        //Pattern pattern = Pattern.compile("^http(s?)://([\\w.]+)((:([0-9]+))?)/admin(\\S*)$");

        return "admin/login";
    }

    @RequestMapping("/admin/main")
    public String main(Model model){

        List<Authority> authList = (List<Authority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Resource resourceTree = resourceService.getResourceTree(authList);

        model.addAttribute("resourceTree", resourceTree);
        return "admin/main";
    }

    @RequestMapping("/admin/panel")
    public String panel(Model model){

        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0 ,0));

        Date todayB = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        Date todayE = Date.from(time.plusHours(24).atZone(ZoneId.systemDefault()).toInstant());

        Date yesterdayB = Date.from(time.plusDays(-1).atZone(ZoneId.systemDefault()).toInstant());
        Date yesterdayE = Date.from(time.plusDays(-1).plusHours(24).atZone(ZoneId.systemDefault()).toInstant());

        Date beforeYesterdayB = Date.from(time.plusDays(-2).atZone(ZoneId.systemDefault()).toInstant());
        Date beforeYesterdayE = Date.from(time.plusDays(-2).plusHours(24).atZone(ZoneId.systemDefault()).toInstant());

        long userNum = userService.countRegInfo(null, null, null);
        long todayUserNum = userService.countRegInfo(todayB, todayE, null);
        long yesterdayUserNum = userService.countRegInfo(yesterdayB, yesterdayE, null);
        long beforeYesterdayUserNum = userService.countRegInfo(beforeYesterdayB, beforeYesterdayE, null);

        Performance query = new Performance();
        BigDecimal performanceSum = performanceService.sum(query);

        query.setStartTime(todayB);
        query.setEndTime(todayE);
        BigDecimal todayPerformanceSum = performanceService.sum(query);

        query.setStartTime(yesterdayB);
        query.setEndTime(yesterdayE);
        BigDecimal yesterdayPerformanceSum = performanceService.sum(query);

        query.setStartTime(beforeYesterdayB);
        query.setEndTime(beforeYesterdayE);
        BigDecimal beforeYesterdayPerformanceSum = performanceService.sum(query);

        List<Product> products = productService.queryAll();
        long[][] regConunts = new long[4][products.size()];

        BigDecimal[][] performanceSums = new BigDecimal[4][products.size()];

        for (int i = 0; i < products.size(); i++){
            regConunts[0][i] = userService.countRegInfo(null, null, products.get(i).getName());
            regConunts[1][i] = userService.countRegInfo(todayB, todayE, products.get(i).getName());
            regConunts[2][i] = userService.countRegInfo(yesterdayB, yesterdayE, products.get(i).getName());
            regConunts[3][i] = userService.countRegInfo(beforeYesterdayB, beforeYesterdayE, products.get(i).getName());

            query = new Performance();
            query.setProductName(products.get(i).getName());
            performanceSums[0][i] = performanceService.sum(query);

            query.setStartTime(todayB);
            query.setEndTime(todayE);
            performanceSums[1][i] = performanceService.sum(query);

            query.setStartTime(yesterdayB);
            query.setEndTime(yesterdayE);
            performanceSums[2][i] = performanceService.sum(query);

            query.setStartTime(beforeYesterdayB);
            query.setEndTime(beforeYesterdayE);
            performanceSums[3][i] = performanceService.sum(query);

        }

        SharesStatistics todaySharesStatistics = sharesStatisticsService.getTodaySharesStatistics();
        SharesStatistics sumSharesStatistics = sharesStatisticsService.sumSharesStatistics();

        model.addAttribute("products", products);
        model.addAttribute("userNum", userNum);
        model.addAttribute("todayUserNum", todayUserNum);
        model.addAttribute("yesterdayUserNum", yesterdayUserNum);
        model.addAttribute("beforeYesterdayUserNum", beforeYesterdayUserNum);
        model.addAttribute("regConunts", regConunts);
        model.addAttribute("performanceSums", performanceSums);
        model.addAttribute("performanceSum", performanceSum);
        model.addAttribute("todayPerformanceSum", todayPerformanceSum);
        model.addAttribute("yesterdayPerformanceSum", yesterdayPerformanceSum);
        model.addAttribute("beforeYesterdayPerformanceSum", beforeYesterdayPerformanceSum);
        model.addAttribute("todaySharesStatistics", todaySharesStatistics);
        model.addAttribute("sumSharesStatistics", sumSharesStatistics);
        return "admin/panel";
    }

    @RequestMapping("/admin/sys/loginLog/list")
    public String loginLoginList(LoginLog query, String userName, Page page, Model model){

        if (!StringUtils.isEmpty(userName)){
            if (query.getType() == 0){
                User user = userService.queryByName(userName);
                query.setUserId(user == null ? new BigDecimal("-1") : user.getUserId());
            }else {
                Manager manager = managerService.queryManagerByName(userName);
                query.setUserId(manager == null ? new BigDecimal("-1") : new BigDecimal(manager.getId()));
            }
        }

        List<LoginLog> list = loginLogService.queryListByExample(query, page);
        List<String> userNameList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)){
            list.forEach(loginLog -> {
                if (loginLog.getType() == 0){
                    User user = userService.queryByKey(loginLog.getUserId());
                    userNameList.add(user.getUserName());
                }else {
                    Manager manager = managerService.queryByKey(loginLog.getUserId().longValue());
                    userNameList.add(manager.getName());
                }
            });
        }

        model.addAttribute("query", query);
        model.addAttribute("userName", userName);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("userNameList", userNameList);

        return "admin/sys/login_log/list";
    }

    @RequestMapping("/admin/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/admin/main";
    }
}
