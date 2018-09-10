package com.sparetime.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.ManagerMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/18.
 */
@Service
public class ManagerServiceImpl implements ManagerService{

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    @Lazy
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private LogService logService;

    @Override
    public Manager queryManagerByName(String name) {
        Assert.notNull(name, "name cant be null");
        return managerMapper.selectByName(name);
    }

    @Override
    public List<Manager> queryByExample(Manager manager, Page page) {
        return managerMapper.selectByExample(manager, page);
    }

    @Override
    @Transactional
    public Long addManager(Manager manager, Long[] authIds) {

        manager.setPassword(bCryptPasswordEncoder.encode(manager.getPassword()));
        manager.setCreateTime(new Date());
        manager.setModifyTime(new Date());
        managerMapper.insert(manager);
        if (authIds == null) authIds = new Long[0];
        List<String> authNames = new ArrayList<>();

        Arrays.asList(authIds).forEach(authId -> {
            managerMapper.insertAuth(manager.getId(), authId);
            authNames.add(authorityService.queryByKey(authId).getAuthName());
        });

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager logManager = managerMapper.selectByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(6);
        log.setUserType(2);
        log.setUserName(logManager.getName());
        log.setRemark("新增:" + JSONObject.toJSONString(manager) + " | 权限： " + String.join(",", authNames));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return manager.getId();
    }

    @Override
    public boolean existByName(String name) {
        return managerMapper.countByName(name) > 0;
    }

    @Override
    @Transactional
    public void update(Manager manager, Long[] authIds) {
        FieldUtil.spaceToNull(manager);
        Manager old = managerMapper.selectByKey(manager.getId());
        if (!StringUtils.isEmpty(manager.getPassword())){
            manager.setPassword(bCryptPasswordEncoder.encode(manager.getPassword()));
        }
        manager.setModifyTime(new Date());
        managerMapper.update(manager);
        managerMapper.deleteAuth(manager.getId());

        if (authIds == null) authIds = new Long[0];
        List<String> authNames = new ArrayList<>();

        Arrays.asList(authIds).forEach(authId -> {
            managerMapper.insertAuth(manager.getId(), authId);
            authNames.add(authorityService.queryByKey(authId).getAuthName());
        });

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager logManager = managerMapper.selectByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(6);
        log.setUserType(2);
        log.setUserName(logManager.getName());
        log.setRemark("修改：Id：" + manager.getId() + ":" + FieldUtil.diffField(old, manager) + " | 权限：" + String.join(",", authNames));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Manager manager = managerMapper.selectByKey(id);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager logManager = managerMapper.selectByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(6);
        log.setUserType(2);
        log.setUserName(logManager.getName());
        log.setRemark("删除：Id：" + id + " | " + JSONObject.toJSONString(manager));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        managerMapper.delete(id);
        managerMapper.deleteAuth(id);
    }

    @Override
    public Manager queryByKey(Long id) {
        return managerMapper.selectByKey(id);
    }
}
