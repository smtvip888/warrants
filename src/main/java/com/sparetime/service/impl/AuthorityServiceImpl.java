package com.sparetime.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.AuthorityMapper;
import com.sparetime.domian.Authority;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Override
    public List<Authority> queryAuthByManagerId(Long managerId) {
        Assert.notNull(managerId, "managerId cant be null");
        return authorityMapper.selectAuthByManagerId(managerId);
    }

    @Override
    public List<Authority> queryListByExample(Authority authority, Page page) {
        return authorityMapper.selectListByExample(authority, page);
    }

    @Override
    public List<Authority> queryListByResourceId(Long resourceId) {
        return authorityMapper.selectListByResourceId(resourceId);
    }

    @Override
    public Authority queryByKey(Long id) {
        return authorityMapper.selectByKey(id);
    }

    @Override
    public Long insert(Authority authority) {
        authority.setCreateTime(new Date());
        authority.setModifyTime(new Date());
        authorityMapper.insert(authority);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(5);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("新增：" + JSONObject.toJSONString(authority));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return authority.getId();
    }

    @Override
    @Transactional
    public void update(Authority authority) {

        Authority old = authorityMapper.selectByKey(authority.getId());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(5);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改：Id：" + authority.getId() + " | " + FieldUtil.diffField(old, authority));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
        authority.setModifyTime(new Date());
        authorityMapper.update(authority);
    }

    @Override
    public void delete(Long id) {

        Authority authority = authorityMapper.selectByKey(id);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Log log = new Log();
        log.setLogType(5);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("删除：Id：" + id + " | " + JSONObject.toJSONString(authority));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
        authorityMapper.delete(id);
    }

    @Override
    public List<Long> queryIdByResourceId(Long resourceId) {
        return authorityMapper.selectIdByResourceId(resourceId);
    }

    @Override
    public boolean isWrite(Long resourceId, Long authId) {
        return authorityMapper.isWrite(resourceId, authId) == 1;
    }
}
