package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Country;
import com.sparetime.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/10/15.
 */
@Controller
@RequestMapping("/admin/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @RequestMapping("/list")
    public String list(Country query, Page page, Model model){

        List<Country> list = countryService.queryListByExample(query, page);

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        return "admin/country/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Country country){

        return "admin/country/add";
    }

    @RequestMapping("/add")
    public String add(@Valid Country country, BindingResult result, Model model){

        if (result.hasErrors()){
            return "admin/country/add";
        }

        countryService.insert(country);

        return "redirect:/admin/country/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(BigDecimal countryId, Model model){

        Country country = countryService.queryByKey(countryId);
        model.addAttribute("country", country);
        return "admin/country/update";
    }

    @RequestMapping("/update")
    public String update(@Valid Country country, BindingResult result, Model model){

        if (result.hasErrors()){
            return "admin/country/update";
        }

        countryService.update(country);
        return "redirect:/admin/country/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){
        countryService.delete(id);
        return "/admin/country/list";
    }
}
