package com.sparetime.service.impl;

import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.SharesConfigMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.Manager;
import com.sparetime.domian.SharesConfig;
import com.sparetime.service.LogService;
import com.sparetime.service.ManagerService;
import com.sparetime.service.SharesConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by muye on 17/8/8.
 */
@Service
public class SharesConfigServiceImpl implements SharesConfigService {

    @Autowired
    private SharesConfigMapper sharesConfigMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LogService logService;

    @Override
    public int clean() {
        return sharesConfigMapper.delete();
    }

    @Override
    public SharesConfig getConfig() {
        return sharesConfigMapper.select();
    }

    @Override
    @Transactional
    public int change(SharesConfig sharesConfig) {

        SharesConfig old = sharesConfigMapper.select();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryByKey((Long) request.getSession().getAttribute("managerId"));
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setLogType(11);
        log.setUserType(2);
        log.setUserName(manager.getName());
        log.setRemark(FieldUtil.diffField(old, sharesConfig));
        log.setIp(ip);
        log.setCdate(new Date());
        logService.insert(log);

        sharesConfigMapper.delete();
        return sharesConfigMapper.insert(sharesConfig);
    }
}
