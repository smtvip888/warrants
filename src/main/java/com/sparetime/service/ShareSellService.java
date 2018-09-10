package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareSell;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface ShareSellService {

    List<ShareSell> queryListByExample(ShareSell sell, Page page);

    ShareSell queryByKey(BigDecimal id);

    BigDecimal getUserCanSellShares(BigDecimal userId);

    void sell(ShareSell shareSell, int type);

    List<ShareSell> querySelfListLikeTradeSN(String tradeSN, BigDecimal userId, Page page);
}
