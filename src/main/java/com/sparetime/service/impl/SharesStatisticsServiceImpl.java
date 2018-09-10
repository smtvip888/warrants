package com.sparetime.service.impl;

import com.sparetime.dao.mapper.SharesStatisticsMapper;
import com.sparetime.domian.SharesStatistics;
import com.sparetime.service.SharesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/9/10.
 */
@Service
public class SharesStatisticsServiceImpl implements SharesStatisticsService {

    @Autowired
    private SharesStatisticsMapper sharesStatisticsMapper;

    @Override
    public SharesStatistics getTodaySharesStatistics() {

        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));

        SharesStatistics sharesStatistics = new SharesStatistics();
        sharesStatistics.setTotalProfit(BigDecimal.ZERO);
        sharesStatistics.setDTBonus(BigDecimal.ZERO);
        sharesStatistics.setDTFT(BigDecimal.ZERO);
        sharesStatistics.setDTLY(BigDecimal.ZERO);

        List<SharesStatistics> list = sharesStatisticsMapper.selectByAfterTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (!CollectionUtils.isEmpty(list))
            return list.get(0);
        return sharesStatistics;
    }

    @Override
    public SharesStatistics sumSharesStatistics() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.of(0,0,0));
        List<SharesStatistics> list = sharesStatisticsMapper.selectByAfterTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));

        SharesStatistics sharesStatistics = new SharesStatistics();
        sharesStatistics.setTotalProfit(BigDecimal.ZERO);
        sharesStatistics.setDTBonus(BigDecimal.ZERO);
        sharesStatistics.setDTFT(BigDecimal.ZERO);
        sharesStatistics.setDTLY(BigDecimal.ZERO);

        if (!CollectionUtils.isEmpty(list)){

            list.forEach(s -> {
                sharesStatistics.setTotalProfit(sharesStatistics.getTotalProfit().add(s.getTotalProfit()));
                sharesStatistics.setDTBonus(sharesStatistics.getDTBonus().add(s.getDTBonus()));
                sharesStatistics.setDTFT(sharesStatistics.getDTFT().add(s.getDTFT()));
                sharesStatistics.setDTLY(sharesStatistics.getDTLY().add(s.getDTLY()));
            });
        }
        return sharesStatistics;
    }
}
