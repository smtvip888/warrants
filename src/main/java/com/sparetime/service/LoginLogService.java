package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.LoginLog;

import java.util.List;

/**
 * Created by muye on 17/8/21.
 */
public interface LoginLogService {

    int insert(LoginLog loginLog);

    List<LoginLog> queryListByExample(LoginLog loginLog, Page page);
 }
