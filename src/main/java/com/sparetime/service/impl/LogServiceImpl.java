package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.LogMapper;
import com.sparetime.domian.Log;
import com.sparetime.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/10/13.
 */
@Service
public class LogServiceImpl implements LogService{

    @Autowired
    private LogMapper logMapper;

    @Override
    public int insert(Log log) {
        return logMapper.insert(log);
    }

    @Override
    public List<Log> queryListByExample(Log log, Page page) {
        FieldUtil.spaceToNull(log);
        return logMapper.selectListByExample(log, page);
    }
}
