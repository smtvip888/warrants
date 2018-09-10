package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Log;
import com.sparetime.domian.LogType;
import com.sparetime.service.LogService;
import com.sparetime.service.LogTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by muye on 17/10/15.
 */
@Controller
@RequestMapping("/admin/sys/log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private LogTypeService logTypeService;

    @RequestMapping("/list")
    public String list(Log query, Page page, Model model){

        List<Log> list = logService.queryListByExample(query, page);
        List<LogType> types = logTypeService.queryAll();

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("types", types);

        return "admin/sys/log/list";
    }
}
