package com.sparetime.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.SysConfigMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.SysConfig;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/28.
 */
@Service
public class SysServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Override
    public SysConfig queryByKey(String key) {
        return sysConfigMapper.selectByKey(key);
    }

    @Override
    public List<SysConfig> queryListByExample(SysConfig config, Page page) {
        return sysConfigMapper.selectListByExample(config, page);
    }

    @Override
    @Transactional
    public int updateValue(String value, String key) {

        SysConfig old = sysConfigMapper.selectByKey(key);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(7);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改：配置名称：" + old.getKey() + " | 配置值：" + old.getValue() + "->" + value);
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return sysConfigMapper.updateValue(value, key);
    }

    @Override
    @Transactional
    public int add(SysConfig config) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(7);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("新增：" + JSONObject.toJSONString(config));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return sysConfigMapper.insert(config);
    }
}
