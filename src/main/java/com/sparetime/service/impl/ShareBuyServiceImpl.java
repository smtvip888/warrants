package com.sparetime.service.impl;

import com.sparetime.common.cons.CashBonusInOrOutEnum;
import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.common.util.NumberBuilder;
import com.sparetime.dao.mapper.FunctionMapper;
import com.sparetime.dao.mapper.ShareBuyMapper;
import com.sparetime.domian.ShareBuy;
import com.sparetime.domian.UserAsset;
import com.sparetime.domian.UserDTFTBonus;
import com.sparetime.domian.UserJTFTBonus;
import com.sparetime.service.ShareBuyService;
import com.sparetime.service.UserAssetService;
import com.sparetime.service.UserDTFTBonusService;
import com.sparetime.service.UserJTFTBonusService;
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
public class ShareBuyServiceImpl implements ShareBuyService {

    @Autowired
    private ShareBuyMapper shareBuyMapper;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private UserJTFTBonusService userJTFTBonusService;

    @Autowired
    private UserDTFTBonusService userDTFTBonusService;

    @Autowired
    private FunctionMapper functionMapper;

    @Override
    public List<ShareBuy> queryListByExample(ShareBuy shareBuy, Page page) {
        FieldUtil.spaceToNull(shareBuy);
        return shareBuyMapper.selectListByExample(shareBuy, page);
    }

    @Override
    @Transactional
    public void buy(ShareBuy shareBuy) {

        UserAsset userAsset = userAssetService.queryByUserId(shareBuy.getUserId());

        if ((shareBuy.getFundType() == 2 && userAsset.getJTFTJJYE().compareTo(shareBuy.getPrice()) == -1)
                ||(shareBuy.getFundType() == 3 && userAsset.getDTFTJJYE().compareTo(shareBuy.getPrice()) == -1)){
            throw new RuntimeException("余额不足");
        }

        shareBuy.setTradeSN(NumberBuilder.build());
        shareBuy.setCdate(new Date());
        shareBuyMapper.insert(shareBuy);

        if (shareBuy.getFundType() == 2) {
            UserJTFTBonus bonus = new UserJTFTBonus();
            bonus.setUserId(shareBuy.getUserId());
            bonus.setNetUserId(shareBuy.getNetUserId());
            bonus.setTradeType(2);
            bonus.setBonusType(7);
            bonus.setAmount(shareBuy.getPrice());
            bonus.setFromUserId(shareBuy.getUserId());
            bonus.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
            bonus.setRemark("权证买入支出");

            userJTFTBonusService.insert(bonus);
            userAsset.setJTFTJJYE(userAsset.getJTFTJJYE().subtract(shareBuy.getPrice()));
        }

        if (shareBuy.getFundType() == 3){
            UserDTFTBonus bonus = new UserDTFTBonus();
            bonus.setUserId(shareBuy.getUserId());
            bonus.setNetUserId(shareBuy.getNetUserId());
            bonus.setTradeType(2);
            bonus.setBonusType(7);
            bonus.setAmount(shareBuy.getPrice());
            bonus.setFromUserId(shareBuy.getUserId());
            bonus.setInOrOut(CashBonusInOrOutEnum.支出.getCode());
            bonus.setRemark("权证买入支出");
            userDTFTBonusService.insert(bonus);
            userAsset.setDTFTJJYE(userAsset.getDTFTJJYE().subtract(shareBuy.getPrice()));
        }

        userAssetService.update(userAsset);
        //functionMapper.callP_Auto_Match_Buy();
    }

    @Override
    public ShareBuy queryByKey(BigDecimal id) {
        return shareBuyMapper.selectByKey(id);
    }

    @Override
    public List<ShareBuy> querySelfListLikeTradeSN(String tradeSN, BigDecimal userId, Page page) {
        return shareBuyMapper.selectSelfListLikeTradeSN(tradeSN, userId, page);
    }

    @Override
    public BigDecimal sumBuyPrice(ShareBuy shareBuy) {
        return shareBuyMapper.sumBuyPrice(shareBuy);
    }
}
