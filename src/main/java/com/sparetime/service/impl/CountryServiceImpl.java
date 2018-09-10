package com.sparetime.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.CountryMapper;
import com.sparetime.domian.Country;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.service.CountryService;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private LogService logService;

    @Autowired
    private ManagerService managerService;

    @Override
    public List<Country> queryListByExample(Country query, Page page) {
        return countryMapper.selectListByExample(query, page);
    }

    @Override
    public Country queryByKey(BigDecimal countryId) {
        return countryMapper.selectByKey(countryId);
    }

    @Override
    @Transactional
    public int insert(Country country) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(12);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("新增:" + JSONObject.toJSONString(country));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return countryMapper.insert(country);
    }

    @Override
    @Transactional
    public int update(Country country) {

        Country old = countryMapper.selectByKey(country.getCountryId());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(12);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改:" + FieldUtil.diffField(old, country));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return countryMapper.update(country);
    }

    @Override
    public int delete(Long id) {

        Country country = countryMapper.selectByKey(new BigDecimal(id));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(12);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("删除:" + JSONObject.toJSONString(country));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
        return countryMapper.delete(id);
    }
}
