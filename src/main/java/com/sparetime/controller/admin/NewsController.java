package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Manager;
import com.sparetime.domian.News;
import com.sparetime.service.ManagerService;
import com.sparetime.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/26.
 */
@Controller
@RequestMapping("/admin/news")
public class NewsController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private NewsService newsService;

    @RequestMapping("/list")
    public String list(News query, String managerName, Page page, Model model){

        if (!StringUtils.isEmpty(managerName)){
            Manager manager = managerService.queryManagerByName(managerName);
            if (manager != null ){
                query.setAdminUserId(manager.getId());
            }
        }

        List<News> newsList = newsService.queryListByExample(query, page);
        List<String> managerNameList = new ArrayList<>();

        newsList.forEach(news -> {
            Manager manager = managerService.queryByKey(news.getAdminUserId());
            managerNameList.add(manager == null ? null : manager.getName());
        });

        model.addAttribute("query", query);
        model.addAttribute("managerName", managerName);
        model.addAttribute("page", page);
        model.addAttribute("newsList", newsList);
        model.addAttribute("managerNameList", managerNameList);
        return "admin/news/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(News news){

        return "admin/news/add";
    }

    @RequestMapping("/add")
    public String add(@Valid News news, BindingResult bindingResult, Model model){

        if (bindingResult.hasFieldErrors()){
            return "admin/news/add";
        }

        newsService.addNews(news);

        return "redirect:/admin/news/list";
    }

    @RequestMapping("/info")
    public String info(BigDecimal id, Model model){

        News news = newsService.queryByKey(id);
        if (news != null){
            Manager manager = managerService.queryByKey(news.getAdminUserId());
            model.addAttribute("managerName", manager == null ? null : manager.getName());
        }

        model.addAttribute("news", news);
        return "admin/news/info";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(News query, Model model){

        News news = newsService.queryByKey(new BigDecimal(query.getId()));
        model.addAttribute("news", news);
        return "admin/news/update";
    }

    @RequestMapping("/update")
    public String update(@Valid News news, BindingResult bindingResult, Model model){

        if (bindingResult.hasFieldErrors()){
            return "admin/news/add";
        }

        newsService.update(news);
        return "redirect:/admin/news/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(BigDecimal id){

        newsService.delete(id);
        return "/admin/news/list";
    }
}
