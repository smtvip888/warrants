package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.LoginLogMapper;
import com.sparetime.domian.LoginLog;
import com.sparetime.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/21.
 */
@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public int insert(LoginLog loginLog) {
        FieldUtil.spaceToNull(loginLog);
        loginLog.setLoginTime(new Date());
        return loginLogMapper.insert(loginLog);
    }

    @Override
    public List<LoginLog> queryListByExample(LoginLog loginLog, Page page) {
        FieldUtil.spaceToNull(loginLog);
        return loginLogMapper.selectListByExample(loginLog, page);
    }
}
