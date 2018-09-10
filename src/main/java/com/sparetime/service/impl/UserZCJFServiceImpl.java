package com.sparetime.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserZCJFMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.User;
import com.sparetime.domian.UserZCJF;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.UserService;
import com.sparetime.service.UserZCJFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class UserZCJFServiceImpl implements UserZCJFService {

    @Autowired
    private UserZCJFMapper userZCJFMapper;

    @Autowired
    private LogService logService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private UserService userService;

    @Override
    public List<UserZCJF> queryListByExample(UserZCJF userZCJF, Page page) {
        return userZCJFMapper.selectListByExample(userZCJF, page);
    }

    @Override
    public int insert(UserZCJF userZCJF) {

        userZCJF.setCdate(new Date());
        return userZCJFMapper.insert(userZCJF);
    }

    @Override
    public int del(Long id) {


        UserZCJF userZCJF = userZCJFMapper.selectByKey(id);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        User user = userService.queryByKey(userZCJF.getUserId());

        Log log = new Log();
        log.setLogType(19);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("注册积分充值取消: " + user.getUserName() + " + 充值金额：" + userZCJF.getAmount());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return userZCJFMapper.updateIsdel(1, id);
    }
}
