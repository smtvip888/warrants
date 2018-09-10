package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SysConfig;
import com.sparetime.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by muye on 17/8/28.
 */
@Controller
@RequestMapping("/admin/sys/sysConfig")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping("/list")
    public String list(SysConfig query, Page page, Model model){

        List<SysConfig> list = sysConfigService.queryListByExample(query, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        return "admin/sys/sys_config/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(SysConfig sysConfig){

        return "admin/sys/sys_config/add";
    }

    @RequestMapping("/add")
    public String add(@Valid SysConfig sysConfig, BindingResult result, Model model){

        if (result.hasFieldErrors()){
            return "admin/sys/sys_config/add";
        }

        sysConfigService.add(sysConfig);
        return "redirect:/admin/sys/sysConfig/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String key, Model model){


        SysConfig sysConfig = sysConfigService.queryByKey(key);
        model.addAttribute("sysConfig", sysConfig);
        return "admin/sys/sys_config/update";
    }

    @RequestMapping("/update")
    public String update(@Valid SysConfig sysConfig, BindingResult result){

        if (result.hasFieldErrors()){
            return "admin/sys/sys_config/update";
        }

        sysConfigService.updateValue(sysConfig.getValue(), sysConfig.getKey());
        return "redirect:/admin/sys/sysConfig/list";
    }
}
