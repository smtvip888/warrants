package com.sparetime.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.ProductMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.Product;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Override
    public List<Product> queryAll() {
        return productMapper.selectAll();
    }

    @Override
    public List<Product> queryListByExample(Product product, Page page) {
        return productMapper.selectListByExample(product, page);
    }

    @Override
    public int insert(Product product) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Log log = new Log();
        log.setLogType(9);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("新增:" + JSONObject.toJSONString(product));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
        return productMapper.insert(product);
    }

    @Override
    @Transactional
    public int update(Product product) {

        Product old = productMapper.selectByKey(product.getProductId());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(9);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改:" + FieldUtil.diffField(old, product));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return productMapper.update(product);
    }

    @Override
    public Product queryByKey(BigDecimal id) {
        return productMapper.selectByKey(id);
    }

    @Override
    public int delete(BigDecimal productId) {

        Product product = productMapper.selectByKey(productId);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(9);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("删除：Id：" + productId + " | " + JSONObject.toJSONString(product));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return productMapper.delete(productId);
    }
}
