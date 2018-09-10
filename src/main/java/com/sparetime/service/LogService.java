package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Log;

import java.util.List;

/**
 * Created by muye on 17/10/13.
 */
public interface LogService {

    int insert(Log log);

    List<Log> queryListByExample(Log log, Page page);
}
