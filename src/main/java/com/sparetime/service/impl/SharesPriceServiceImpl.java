package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.SharesPriceMapper;
import com.sparetime.domian.SharesPrice;
import com.sparetime.service.SharesPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
@Service
public class SharesPriceServiceImpl implements SharesPriceService {

    @Autowired
    private SharesPriceMapper sharesPriceMapper;

    @Override
    public SharesPrice queryByMaxId() {
        return sharesPriceMapper.selectByMaxId();
    }

    @Override
    public BigDecimal getMaxPriceByDate(Date start, Date end) {
        BigDecimal price = sharesPriceMapper.selectMaxPriceByDate(start, end);
        return price == null ? BigDecimal.ZERO : price;
    }

    @Override
    public BigDecimal[] priceTrend(int days) {

        BigDecimal[] trend = new BigDecimal[days];
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        for (int i = 0; i < days; days --){
            LocalDateTime start = dateTime.plusDays(-days + 1);
            LocalDateTime end = start.plusHours(24);
            trend[trend.length - days] = getMaxPriceByDate(
                    Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));
        }
        return trend;
    }

    @Override
    public List<SharesPrice> queryListByExample(SharesPrice price, Page page) {
        return sharesPriceMapper.selectListByExample(price, page);
    }

    @Override
    public int updateShares(BigDecimal shares, Long id) {
        SharesPrice sharesPrice = sharesPriceMapper.selectByKey(id);
        if (shares.compareTo(sharesPrice.getSuccesShares()) != 1)
            throw new RuntimeException("权证数必须大于成交数");
        return sharesPriceMapper.updateShares(shares, id);
    }

    @Override
    public SharesPrice queryByKey(long id) {
        return sharesPriceMapper.selectByKey(id);
    }


    @Override
    public int insert(SharesPrice price) {
        return sharesPriceMapper.insert(price);
    }
}
