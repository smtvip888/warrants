package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.NewsMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.News;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/26.
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Override
    public List<News> queryListByExample(News news, Page page) {
        FieldUtil.spaceToNull(news);
        return newsMapper.selectListByExample(news, page);
    }

    @Override
    public News queryByKey(BigDecimal id) {
        return newsMapper.selectByKey(id);
    }

    @Override
    @Transactional
    public Long addNews(News news) {

        String managerName = ((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);
        if (manager != null)
            news.setAdminUserId(manager.getId());
        news.setCdate(new Date());
        newsMapper.insert(news);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(10);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("新增:" + news.getTitle() + " | /admin/news/info?id=" + news.getId());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return news.getId();
    }

    @Override
    public int update(News news) {

        News old = newsMapper.selectByKey(new BigDecimal(news.getId()));

        String managerName = ((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);
        if (manager != null)
            news.setAdminUserId(manager.getId());

        news.setIsDelete(news.getIsDelete() == null ? 0 : news.getIsDelete());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(10);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改:" + old.getTitle() + "->" + news.getTitle() + " | /admin/news/info?id=" + news.getId());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return newsMapper.update(news);
    }

    @Override
    public int delete(BigDecimal id) {

        News news = newsMapper.selectByKey(id);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        String managerName = ((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);

        Log log = new Log();
        log.setLogType(10);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("删除:" + news.getTitle() + " | Id：" + id);
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
        return newsMapper.delete(id);
    }
}
