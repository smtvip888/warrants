package com.sparetime.service.impl;

import com.sparetime.dao.mapper.PerformanceMapper;
import com.sparetime.domian.Performance;
import com.sparetime.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/23.
 */
@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    private PerformanceMapper performanceMapper;

    @Override
    public int insert(Performance performance) {
        performance.setCreateTime(new Date());
        return performanceMapper.insert(performance);
    }

    @Override
    public BigDecimal sum(Performance performance) {
        BigDecimal sum = performanceMapper.sum(performance);
        return sum == null ? BigDecimal.ZERO : sum;
    }
}
