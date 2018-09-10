package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.SharesSplit;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.SharesSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/23.
 */
@Controller
@RequestMapping("/admin/sharesSplit")
public class SharesSplitController {

    @Autowired
    private SharesSplitService sharesSplitService;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    LogService logService;

    @RequestMapping("/list")
    public String list(SharesSplit query, Page page, Model model){

        List<SharesSplit> list = sharesSplitService.queryListByExample(query, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        return "admin/shares_split/list";
    }

    @RequestMapping("/split")
    @Transactional
    public String split(HttpServletRequest request){

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));

        Log log = new Log();
        log.setLogType(15);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("拆分");
        log.setIp(ip);
        log.setCdate(new Date());

        logService.insert(log);

        functionMapper.callP_SplitShares();
        return "redirect:/admin/sharesSplit/list";
    }
}
