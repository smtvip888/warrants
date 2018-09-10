package com.sparetime.service.impl;

import com.sparetime.dao.mapper.SharesBuyLogMapper;
import com.sparetime.domian.SharesBuyLog;
import com.sparetime.service.SharesBuyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/8/22.
 */
@Service
public class SharesBuyLogServiceImpl implements SharesBuyLogService {

    @Autowired
    private SharesBuyLogMapper sharesBuyLogMapper;

    @Override
    public List<SharesBuyLog> queryListByBuyId(BigDecimal buyId) {
        return sharesBuyLogMapper.selectListByBuyId(buyId);
    }
}
