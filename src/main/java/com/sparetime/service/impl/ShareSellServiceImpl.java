package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.common.util.NumberBuilder;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.dao.mapper.ShareSellMapper;
import com.sparetime.domian.ShareSell;
import com.sparetime.service.ShareSellService;
import com.sparetime.service.SharesPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class ShareSellServiceImpl implements ShareSellService {

    @Autowired
    private ShareSellMapper shareSellMapper;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private SharesPriceService sharesPriceService;

    @Override
    public List<ShareSell> queryListByExample(ShareSell sell, Page page) {
        FieldUtil.spaceToNull(sell);
        return shareSellMapper.selectListByExample(sell, page);
    }

    @Override
    public ShareSell queryByKey(BigDecimal id) {
        return shareSellMapper.selectByKey(id);
    }

    @Override
    public BigDecimal getUserCanSellShares(BigDecimal userId) {

        return functionMapper.callP_GetUserCanSellShares(userId);
    }

    @Override
    @Transactional
    public void sell(ShareSell shareSell, int type) {

//        if (sharesPriceService.queryByMaxId().getPrice().compareTo(new BigDecimal("0.35")) == 1)
//            throw new RuntimeException("当前价格大于0.35，不能卖出");

//        if (shareSell.getShares().compareTo(functionMapper.callP_GetUserCanSellShares(shareSell.getUserId())) == 1){
//            throw new RuntimeException("挂卖数量不能大于可卖出数");
//        }

        if (type == 1 && shareSell.getShares().compareTo(functionMapper.callP_GetUserCanSellShares_JT(shareSell.getUserId()).getCanSellShares()) == 1)
            throw new RuntimeException("挂卖数量不能大于可卖出数");
        else if (type == 2 && shareSell.getShares().compareTo(functionMapper.callP_GetUserCanSellShares_DT(shareSell.getUserId()).getCanSellShares()) == 1)
            throw new RuntimeException("挂卖数量不能大于可卖出数");

        shareSell.setTradeSN(NumberBuilder.build());
        shareSell.setPrice(sharesPriceService.queryByMaxId().getPrice());
        shareSell.setShareType(type);
        shareSell.setCdate(new Date());
        shareSellMapper.insert(shareSell);
        functionMapper.callP_UpdateUserCanSellShares(shareSell.getSellId());
        functionMapper.callP_Shares_Sell(shareSell.getSellId());
        //functionMapper.callP_Auto_Match_Buy();
    }

    @Override
    public List<ShareSell> querySelfListLikeTradeSN(String tradeSN, BigDecimal userId, Page page) {
        return shareSellMapper.selectSelfListLikeTradeSN(tradeSN, userId, page);
    }
}
