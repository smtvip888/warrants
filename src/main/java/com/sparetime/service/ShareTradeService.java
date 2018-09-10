package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareTrade;

import java.math.BigDecimal;
import java.util.List;

public interface ShareTradeService {

    List<ShareTrade> queryListByExample(ShareTrade shareTrade, Page page);

    ShareTrade queryByKey(BigDecimal id);

    BigDecimal sellSurplus(BigDecimal userId);
}
