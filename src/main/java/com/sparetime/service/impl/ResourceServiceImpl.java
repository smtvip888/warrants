package com.sparetime.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.cons.AuthEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.ResourceMapper;
import com.sparetime.domian.Authority;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.Resource;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by muye on 17/7/19.
 */
@Service
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public Resource getResourceTree(List<Authority> authList) {

        List<Long> authIds = new ArrayList<>();
        Resource tree = resourceMapper.selectRoot();

        if (authList.stream().anyMatch(auth -> AuthEnum.SUP_ADMIN.name().equals(auth.getAuthCode()))) {
            getResourceTree(tree, null);
        } else{
            authIds.add(-1l);
            authList.forEach(auth -> authIds.add(auth.getId()));
            getResourceTree(tree, authIds);
        }

        return tree;
    }

    @Override
    public List<Resource> queryListByExample(Resource query, Page page) {

        return resourceMapper.selectListByExample(query, page);
    }

    @Override
    public Resource queryByKey(Long id) {
        return resourceMapper.selectByKey(id);
    }

    @Override
    @Transactional
    public Long addResource(Resource resource, Long[] authIds, Long[] writes) {
        resource.setCreateTime(new Date());
        resource.setModifyTime(new Date());
        resourceMapper.insert(resource);

        if (authIds != null && authIds.length > 0){
            Arrays.asList(authIds)
                    .forEach(authId -> resourceMapper.insertAuth(resource.getId(), authId, Stream.of(writes).anyMatch(authId::equals) ? 1 : 0));
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        List<Authority> auths = authorityService.queryListByResourceId(resource.getId());
        StringBuffer sb = new StringBuffer(" resource: ");
        sb.append(JSONObject.toJSONString(resource));
        sb.append(" auths: [");
        if (auths != null){
            auths.forEach(auth -> sb.append("{\"authName\":" + auth.getAuthName() + ", \"isWrite\":" +  authorityService.isWrite(resource.getId(), auth.getId()) + "}"));
        }
        sb.append("]");
        Log log = new Log();
        log.setLogType(4);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("新增：" + sb.toString());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        return resource.getId();
    }

    @Override
    @Transactional
    public void update(Resource resource, Long[] authIds, Long[] writes) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Resource old = resourceMapper.selectByKey(resource.getId());
        StringBuffer sb = new StringBuffer(" 权限: [");
        List<Authority> oldAuth = authorityService.queryListByResourceId(resource.getId());
        if (oldAuth != null){
            oldAuth.forEach(auth -> sb.append("{\"权限名\":" + auth.getAuthName() + ", \"写权限\":" +  authorityService.isWrite(resource.getId(), auth.getId()) + "}" ));
        }
        sb.append("] -> [");
        resource.setModifyTime(new Date());
        resourceMapper.update(resource);
        resourceMapper.deleteAuth(resource.getId());
        if (authIds != null && authIds.length > 0){
            Arrays.asList(authIds)
                    .forEach(authId -> resourceMapper.insertAuth(resource.getId(), authId, Stream.of(writes).anyMatch(authId::equals) ? 1 : 0));
        }
        List<Authority> newAuth = authorityService.queryListByResourceId(resource.getId());
        if (newAuth != null){
            newAuth.forEach(auth -> sb.append("{\"权限名\":" + auth.getAuthName() + ", \"写权限\":" +  authorityService.isWrite(resource.getId(), auth.getId()) + "}" ));
        }
        sb.append("]");
        Log log = new Log();
        log.setLogType(4);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("修改：Id：" + resource.getId() + " | " + FieldUtil.diffField(old, resource) + sb.toString());
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Resource resource = resourceMapper.selectByKey(id);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Log log = new Log();
        log.setLogType(4);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark("删除：Id：" + id + " | " + JSONObject.toJSONString(resource));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        resourceMapper.delete(id);
        resourceMapper.deleteAuth(id);
    }

    @Override
    public Resource queryByLikeUrl(String url) {
        return resourceMapper.selectByLikeUrl(url);
    }

    private void getResourceTree(Resource resource, List<Long> authList){

        List<Resource> children = resourceMapper.selectListByPidAndAuths(resource.getId(), authList);
        resource.setChildren(children);
        for (Resource r : children){
            getResourceTree(r, authList);
        }
    }
}
