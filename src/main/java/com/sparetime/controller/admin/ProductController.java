package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Product;
import com.sparetime.service.ProductService;
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
 * Created by muye on 17/8/8.
 */
@Controller
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/list")
    public String list(Product query, Page page, Model model){

        List<Product> productList = productService.queryListByExample(query, page);

        model.addAttribute("productList", productList);
        model.addAttribute("page", page);

        return "admin/product/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Product product){

        return "admin/product/add";
    }

    @RequestMapping("/add")
    public String add(@Valid Product product, BindingResult bindingResult){

        if (bindingResult.hasFieldErrors()){
            return "admin/product/add";
        }
        productService.insert(product);

        return "redirect:/admin/product/list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Product query, Model model){

        Product product = productService.queryByKey(query.getProductId());
        model.addAttribute("product", product);

        return "admin/product/update";
    }

    @RequestMapping("/update")
    public String update(@Valid Product product, BindingResult bindingResult){

        if (bindingResult.hasFieldErrors()){
            return "admin/product/update";
        }

        productService.update(product);
        return "redirect:/admin/product/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String del(BigDecimal id){

        productService.delete(id);
        return "/admin/product/list";
    }
}
