package com.sparetime.service;

import com.sparetime.domian.SharesSellRefundLog;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/8/22.
 */
public interface SharesSellRefundLogService {

    List<SharesSellRefundLog> queryListBySellId(BigDecimal sellId);
}
