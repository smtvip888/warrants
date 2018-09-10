package com.sparetime.service;

import com.sparetime.domian.SharesStatistics;

/**
 * Created by muye on 17/9/10.
 */
public interface SharesStatisticsService {

    SharesStatistics getTodaySharesStatistics();

    SharesStatistics sumSharesStatistics();
}
