package com.sparetime.service;

import com.sparetime.domian.SharesBuyLog;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/8/22.
 */
public interface SharesBuyLogService {

    List<SharesBuyLog> queryListByBuyId(BigDecimal buyId);
}
