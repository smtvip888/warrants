package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.domian.Product;
import com.sparetime.domian.extend.UserGuide;
import com.sparetime.service.ProductService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/11/20.
 */
@Controller
public class GuideController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/admin/guide/guideUser")
    public String guideUser(UserGuide query, Page page, String start, String end, HttpServletRequest request, Model model) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isEmpty(start))
            query.setStartTime(sdf.parse(start));
        if (!StringUtils.isEmpty(end))
            query.setEndTime(sdf.parse(end));

        FieldUtil.spaceToNull(query);

        List<UserGuide> list = new ArrayList<>();
        if (!StringUtils.isEmpty(query.getProductName())) list =  userService.queryGuideList(query, page);

        List<Product> productList = productService.queryAll();

        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("list", list);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("productList", productList);


        model.addAttribute("tip", request.getAttribute("tip"));

        return "admin/user/guide";
    }

    @RequestMapping("/admin/guide/updateOfGuide")
    public String updateOfGuide(UserGuide query, Page page, String start, String end, Model model) throws Exception{

        String tip = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isEmpty(start))
            query.setStartTime(sdf.parse(start));
        if (!StringUtils.isEmpty(end))
            query.setEndTime(sdf.parse(end));

        FieldUtil.spaceToNull(query);

        if (query.getNum() == null){
            tip = "可持股数为空";
        }

        if (StringUtils.isEmpty(query.getProductName())){
            tip = "会员等级为空";
        }

        if (query.getEndTime() == null){
            tip = "结束时间为空";
        }

        if (query.getStartTime() == null){
            tip = "开始时间为空";
        }

        if (StringUtils.isEmpty(tip)){
            Product pQuery = new Product();
            pQuery.setName(query.getProductName());
            Product product = productService.queryListByExample(pQuery, null).get(0);
            userService.guide(query.getStartTime(), query.getEndTime(), product.getProductId().intValue(), query.getNum());
            tip = "执行完成";
        }

        model.addAttribute("tip", tip);
        model.addAttribute("query", query);
        model.addAttribute("page", page);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "forward:/admin/guide/guideUser";
    }
}
