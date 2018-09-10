package com.sparetime.controller.fore;

import com.sparetime.domian.News;
import com.sparetime.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by muye on 17/8/19.
 */
@Controller
@RequestMapping("/fore/other")
public class ForeOtherController {

    @Autowired
    private NewsService newsService;

    @RequestMapping("/news")
    public String news(BigDecimal newsId, Model model){

        News news = newsService.queryByKey(newsId);
        if (news == null) throw new RuntimeException("新闻不存在");
        if (news.getIsDelete() != 0) throw new  RuntimeException("新闻为发布");
        model.addAttribute("news",news);
        return "fore/other/news";
    }

    @RequestMapping("/lang")
    @ResponseBody
    public String lang(String lang, HttpServletRequest request){

        request.getSession().setAttribute("lang", lang);
        return "success";
    }
}
