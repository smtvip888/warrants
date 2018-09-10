package com.sparetime.service.impl;

import com.sparetime.dao.mapper.LogTypeMapper;
import com.sparetime.domian.LogType;
import com.sparetime.service.LogTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/10/17.
 */
@Service
public class LogTypeServiceImpl implements LogTypeService {

    @Autowired
    private LogTypeMapper logTypeMapper;

    @Override
    public List<LogType> queryAll() {
        return logTypeMapper.selectAll();
    }
}
