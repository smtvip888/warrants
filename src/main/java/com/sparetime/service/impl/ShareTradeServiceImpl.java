package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.ShareTradeMapper;
import com.sparetime.domian.ShareTrade;
import com.sparetime.service.ShareTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShareTradeServiceImpl implements ShareTradeService {

    @Autowired
    private ShareTradeMapper shareTradeMapper;

    @Override
    public List<ShareTrade> queryListByExample(ShareTrade shareTrade, Page page) {
        return shareTradeMapper.selectListByExample(shareTrade, page);
    }

    @Override
    public ShareTrade queryByKey(BigDecimal id) {
        return shareTradeMapper.selectByKey(id);
    }

    @Override
    public BigDecimal sellSurplus(BigDecimal userId) {
        return shareTradeMapper.sellSurplus(userId);
    }
}
