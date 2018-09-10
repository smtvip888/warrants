package com.sparetime.service;

import com.sparetime.domian.Performance;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/23.
 */
public interface PerformanceService {

    int insert(Performance performance);

    BigDecimal sum(Performance performance);
}
