package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SharesPrice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
public interface SharesPriceService {

    SharesPrice queryByMaxId();

    BigDecimal getMaxPriceByDate(Date start, Date end);

    BigDecimal[] priceTrend(int days);

    List<SharesPrice> queryListByExample(SharesPrice price, Page page);

    int updateShares(BigDecimal shares, Long id);

    SharesPrice queryByKey(long id);

    int insert(SharesPrice price);
}
