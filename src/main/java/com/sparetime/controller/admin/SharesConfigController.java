package com.sparetime.controller.admin;

import com.sparetime.domian.SharesConfig;
import com.sparetime.service.SharesConfigService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created by muye on 17/8/8.
 */
@Controller
@RequestMapping("/admin/sharesConfig")
public class SharesConfigController {

    @Autowired
    private SharesConfigService sharesConfigService;

    @Autowired
    private UserService userService;

    @RequestMapping("/toChange")
    public String toChange(Model model){

        SharesConfig sharesConfig = sharesConfigService.getConfig();
        Long maxSubId = userService.queryMaxSubId();
        model.addAttribute("sharesConfig", sharesConfig);
        model.addAttribute("maxSubId", maxSubId);

        return "admin/shares_config/change";
    }

    @RequestMapping("/change")
    @Transactional
    public String change(@Valid SharesConfig sharesConfig, BindingResult bindingResult, Model model){

        if (bindingResult.hasFieldErrors()){
            Long maxSubId = userService.queryMaxSubId();
            model.addAttribute("maxSubId", maxSubId);
            return "admin/shares_config/change";
        }

        sharesConfigService.change(sharesConfig);

        return "redirect:/admin/main";
    }

}
