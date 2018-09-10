package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.SharesPrice;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.SharesPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/23.
 */
@Controller
@RequestMapping("/admin/sharesPrice")
public class SharesPriceController {

    @Autowired
    private SharesPriceService sharesPriceService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @RequestMapping("/list")
    public String list(SharesPrice query, Page page, Model model){

        List<SharesPrice> list = sharesPriceService.queryListByExample(query, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        return "admin/shares_price/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(long id, Model model){

        SharesPrice sharesPrice = sharesPriceService.queryByKey(id);
        model.addAttribute("sharesPrice", sharesPrice);
        return "admin/shares_price/update";
    }

    @RequestMapping("/update")
    public String update(@Valid SharesPrice sharesPrice, BindingResult result, Model model){

        model.addAttribute("sharesPrice", sharesPrice);
        if (sharesPrice.getShares() == null){
            FieldError fieldError = new FieldError("sharesPrice","shares", "权证数量不能为空");
            result.addError(fieldError);
        }

        if (result.hasFieldErrors("shares")){
            return "admin/shares_price/update";
        }

        try {
            sharesPriceService.updateShares(sharesPrice.getShares(), sharesPrice.getId());
        }catch (Exception e){
            FieldError fieldError = new FieldError("sharesPrice", "shares", e.getMessage());
            result.addError(fieldError);
            return "admin/shares_price/update";
        }

        return "redirect:/admin/sharesPrice/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(SharesPrice sharesPrice, Model model){


        return "admin/shares_price/add";
    }

    @RequestMapping("/add")
    public String add(@Valid SharesPrice sharesPrice, BindingResult result, HttpServletRequest request, Model model){

        if (result.hasErrors()){
            return "admin/shares_price/add";
        }

        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        sharesPrice.setShares(BigDecimal.ZERO);
        sharesPrice.setSuccesShares(BigDecimal.ZERO);
        sharesPrice.setUserShares(BigDecimal.ZERO);
        sharesPrice.setCdate(new Date());
        sharesPrice.setIp(ip);
        sharesPrice.setAdminUserName(manager.getName());
        sharesPriceService.insert(sharesPrice);

        Log log = new Log();
        log.setLogType(20);
        log.setUserName(manager.getName());
        log.setUserType(2);
        log.setRemark("新增权证价格" + sharesPrice.getPrice());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return "redirect:/admin/sharesPrice/list";
    }
}
