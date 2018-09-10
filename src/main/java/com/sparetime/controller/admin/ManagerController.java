package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.domian.Authority;
import com.sparetime.domian.Manager;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by muye on 17/7/21.
 */
@Controller
@RequestMapping("/admin/sys/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping("/list")
    public String list(Manager query, Page page, Model model){

        FieldUtil.spaceToNull(query);
        List<Manager> managerList = managerService.queryByExample(query, page);

        model.addAttribute("managerList", managerList);
        model.addAttribute("query", query);
        model.addAttribute("page", page);
        return "admin/sys/manager/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Manager manager, Model model){

        List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);

        model.addAttribute("allAuth", allAuth);
        return "admin/sys/manager/add";
    }

    @RequestMapping("/add")
    public String add(@Valid Manager manager, BindingResult bindingResult, Long[] authIds, Model model){

        if (managerService.existByName(manager.getName())){
            FieldError fieldError = new FieldError("manager", "name", "name already exists");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasFieldErrors()){
            List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);

            model.addAttribute("allAuth", allAuth);
            return "admin/sys/manager/add";
        }

        managerService.addManager(manager, authIds);
        return "redirect:/admin/sys/manager/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Manager query, Model model){

        Manager manager = managerService.queryByKey(query.getId());

        List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);
        List<Authority> ownedAuth = authorityService.queryAuthByManagerId(query.getId());

        model.addAttribute("allAuth", allAuth);
        model.addAttribute("manager", manager);
        model.addAttribute("ownedAuth", ownedAuth);
        return "admin/sys/manager/update";
    }

    @RequestMapping("/update")
    public String update(@Valid Manager manager, BindingResult bindingResult, Long[] authIds, Model model){

        Manager old = managerService.queryByKey(manager.getId());

        if (!old.getName().equals(manager.getName()) && managerService.existByName(manager.getName())){
            FieldError fieldError = new FieldError("manager", "name", "name already exists");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasFieldErrors("name")){
            List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);
            List<Authority> ownedAuth = authorityService.queryAuthByManagerId(manager.getId());
            model.addAttribute("allAuth", allAuth);
            model.addAttribute("ownedAuth", ownedAuth);
            return "admin/sys/manager/update";
        }

        managerService.update(manager, authIds);
        return "redirect:/admin/sys/manager/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){

        managerService.delete(id);
        return "/admin/sys/manager/list";
    }
}