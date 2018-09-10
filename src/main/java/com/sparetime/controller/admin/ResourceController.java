package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.domian.Authority;
import com.sparetime.domian.Resource;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
@Controller
@RequestMapping("/admin/sys/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping("/list")
    public String list(Resource query, Page page, Model model){

        FieldUtil.spaceToNull(query);

        List<Resource> resourceList = resourceService.queryListByExample(query, page);

        model.addAttribute("resourceList", resourceList);
        model.addAttribute("query", query);
        model.addAttribute("page", page);
        return "admin/sys/resource/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Resource resource, Model model){

        List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);
        List<Resource> allResource = resourceService.queryListByExample(new Resource(), null);

        model.addAttribute("allAuth", allAuth);
        model.addAttribute("allResource", allResource);
        return "admin/sys/resource/add";
    }

    @RequestMapping("/add")
    public String add(@Valid Resource resource, BindingResult bindingResult, Long[] authIds, Long[] writes,  Model model){

        if (bindingResult.hasFieldErrors()){

            List<Resource> allResource = resourceService.queryListByExample(new Resource(), null);
            List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);

            model.addAttribute("allAuth", allAuth);
            model.addAttribute("allResource", allResource);
            return "admin/sys/resource/add";
        }

        if (writes == null){
            writes = new Long[0];
        }
        resourceService.addResource(resource, authIds, writes);
        return "redirect:/admin/sys/resource/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Resource query, Model model){

        Resource resource = resourceService.queryByKey(query.getId());
        List<Resource> allResource = resourceService.queryListByExample(new Resource(), null);
        List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);
        List<Authority> ownedAuth = authorityService.queryListByResourceId(query.getId());

        List<Authority> writesAuth = new ArrayList<>();
        ownedAuth.forEach(auth -> {
            if (authorityService.isWrite(resource.getId(), auth.getId())) writesAuth.add(auth);
        });

        model.addAttribute("resource", resource);
        model.addAttribute("allResource", allResource);
        model.addAttribute("allAuth", allAuth);
        model.addAttribute("ownedAuth", ownedAuth);
        model.addAttribute("writesAuth", writesAuth);
        return "admin/sys/resource/update";
    }

    @RequestMapping("/update")
    public String update(@Valid Resource resource, BindingResult bindingResult, Long[] authIds, Long[] writes, Model model){

        if (bindingResult.hasFieldErrors()){
            List<Resource> allResource = resourceService.queryListByExample(new Resource(), null);
            List<Authority> allAuth = authorityService.queryListByExample(new Authority(), null);
            List<Authority> ownedAuth = authorityService.queryListByResourceId(resource.getId());

            model.addAttribute("resource", resource);
            model.addAttribute("allResource", allResource);
            model.addAttribute("allAuth", allAuth);
            model.addAttribute("ownedAuth", ownedAuth);
            return "admin/sys/resource/update";
        }

        resourceService.update(resource, authIds, writes == null ? new Long[]{} : writes);
        return "redirect:/admin/sys/resource/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){

        resourceService.delete(id);
        return "/admin/sys/resource/list";
    }
}
