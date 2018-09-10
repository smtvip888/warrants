package com.sparetime.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.domian.Manager;
import com.sparetime.domian.SysConfig;
import com.sparetime.domian.extend.Notice;
import com.sparetime.service.ManagerService;
import com.sparetime.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/2/12.
 */
@Controller
@RequestMapping("/admin/notice")
public class NoticeController {

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ManagerService managerService;

    @RequestMapping("/list")
    public String list(Model model){

        List<Notice> list = new ArrayList<>();

        //SysConfig cnNotice = sysConfigService.queryByKey("cn_notice");
        //SysConfig enNotice = sysConfigService.queryByKey("en_notice");
        SysConfig noticeConfig = sysConfigService.queryByKey("notice");

        //list.add(JSONObject.parseObject(cnNotice.getValue(), Notice.class));
        //list.add(JSONObject.parseObject(cnNotice.getValue(), Notice.class));
        list.add(JSONObject.parseObject(noticeConfig.getValue(), Notice.class));
        List<String> managerNameList = new ArrayList<>();
        list.forEach(notice -> {
            if (notice.getAdminUserId() != null){
                Manager manager = managerService.queryByKey(notice.getAdminUserId());
                managerNameList.add(manager == null ? null : manager.getName());
            }else {
                managerNameList.add(null);
            }
        });

        model.addAttribute("list", list);
        model.addAttribute("managerNameList", managerNameList);
        return "admin/notice/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String lang, Model model){
        SysConfig config = sysConfigService.queryByKey("notice");
        Notice notice = JSONObject.parseObject(config.getValue(), Notice.class);

        model.addAttribute("notice", notice);
        return "admin/notice/update";
    }

    @RequestMapping("/update")
    public String update(@Valid Notice notice, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "forward: /admin/notice/toUpdate?lang="+notice.getLang();
        }

        String managerName = ((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);
        if (manager != null)
            notice.setAdminUserId(manager.getId());

        sysConfigService.updateValue(JSONObject.toJSONString(notice), "notice");

        return "redirect:/admin/notice/list";
    }
}
