package com.sparetime.service.impl;

import com.sparetime.dao.mapper.ShareSellBuyCrossMapper;
import com.sparetime.domian.ShareSellBuyCross;
import com.sparetime.service.ShareSellBuyCrossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class ShareSellBuyCrossServiceImpl implements ShareSellBuyCrossService{

    @Autowired
    private ShareSellBuyCrossMapper shareSellBuyCrossMapper;

    @Override
    public List<ShareSellBuyCross> queryListBySellId(BigDecimal sellId) {
        return shareSellBuyCrossMapper.selectBySellId(sellId);
    }

    @Override
    public List<ShareSellBuyCross> queryListByBuyId(BigDecimal buyId) {
        return shareSellBuyCrossMapper.selectByBuyId(buyId);
    }
}
