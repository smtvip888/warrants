package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.domian.Authority;
import com.sparetime.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by muye on 17/7/21.
 */
@Controller
@RequestMapping("/admin/sys/auth")
public class AuthController {

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping("/list")
    public String list(Authority query, Page page, Model model){

        FieldUtil.spaceToNull(query);
        List<Authority> authorityList = authorityService.queryListByExample(query, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("authorityList", authorityList);
        return "admin/sys/auth/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Authority authority){

        return "admin/sys/auth/add";
    }

    @RequestMapping("/add")
    public String add(@Valid Authority authority, BindingResult bindingResult){

        if (bindingResult.hasFieldErrors()){
            return "admin/sys/auth/add";
        }

        authorityService.insert(authority);
        return "redirect:/admin/sys/auth/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Authority query, Model model){

        Authority authority = authorityService.queryByKey(query.getId());

        model.addAttribute("authority", authority);
        return "admin/sys/auth/update";
    }

    @RequestMapping("/update")
    public String update(@Valid Authority authority, BindingResult bindingResult, Model model){

        if (bindingResult.hasFieldErrors()){
            model.addAttribute("authority", authority);
            return "admin/sys/auth/update";
        }

        authorityService.update(authority);
        return "redirect:/admin/sys/auth/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){

        authorityService.delete(id);
        return "/admin/sys/auth/list";
    }
}
