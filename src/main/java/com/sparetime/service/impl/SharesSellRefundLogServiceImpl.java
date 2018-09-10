package com.sparetime.service.impl;

import com.sparetime.dao.mapper.SharesSellRefundLogMapper;
import com.sparetime.domian.SharesSellRefundLog;
import com.sparetime.service.SharesSellRefundLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/8/22.
 */
@Service
public class SharesSellRefundLogServiceImpl implements SharesSellRefundLogService {

    @Autowired
    private SharesSellRefundLogMapper sharesSellRefundLogMapper;

    @Override
    public List<SharesSellRefundLog> queryListBySellId(BigDecimal sellId) {
        return sharesSellRefundLogMapper.selectListBySellId(sellId);
    }
}
