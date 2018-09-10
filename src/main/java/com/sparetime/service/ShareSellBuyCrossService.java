package com.sparetime.service;

import com.sparetime.domian.ShareSellBuyCross;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface ShareSellBuyCrossService {

    List<ShareSellBuyCross> queryListBySellId(BigDecimal sellId);

    List<ShareSellBuyCross> queryListByBuyId(BigDecimal buyId);
}
