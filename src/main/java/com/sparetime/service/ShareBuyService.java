package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareBuy;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface ShareBuyService {

    List<ShareBuy> queryListByExample(ShareBuy shareBuy, Page page);

    void buy(ShareBuy shareBuy);

    ShareBuy queryByKey(BigDecimal id);

    List<ShareBuy> querySelfListLikeTradeSN(String tradeSN, BigDecimal userId, Page page);

    BigDecimal sumBuyPrice(ShareBuy shareBuy);
}
