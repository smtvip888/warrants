package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.UserCashSellMapper;
import com.sparetime.domian.*;
import com.sparetime.service.UserAssetService;
import com.sparetime.service.UserBonusService;
import com.sparetime.service.UserCashBuyService;
import com.sparetime.service.UserCashSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserCashSellServiceImpl implements UserCashSellService {

    @Autowired
    private UserCashSellMapper userCashSellMapper;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private UserBonusService userBonusService;

    @Autowired
    private UserCashBuyService userCashBuyService;

    @Override
    public List<UserCashSell> queryListByExample(UserCashSell query, BigDecimal start, BigDecimal end, Page page) {
        FieldUtil.spaceToNull(query);
        return userCashSellMapper.selectListByExample(query, start, end, page);
    }

    @Override
    public UserCashSell queryByKey(BigDecimal sellId) {
        return userCashSellMapper.selectByKey(sellId);
    }

    @Override
    @Transactional
    public void sell(User user, UserCashSell sell, String ip) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");

        sell.setBrokerage(sell.getBrokerage() == null ? BigDecimal.ZERO : sell.getBrokerage());
        UserAsset userAsset = userAssetService.queryByUserId(user.getUserId());
        if (userAsset.getBonusSurplus().compareTo(sell.getAmount().add(sell.getBrokerage())) == -1){
            throw new RuntimeException("余额不足");
        }
        userAsset.setBonusSurplus(userAsset.getBonusSurplus().subtract(sell.getAmount().add(sell.getBrokerage())));
        userAssetService.update(userAsset);

        sell.setTradeSN(sdf.format(new Date()));
        sell.setUserId(user.getUserId());
        sell.setIp(ip);
        sell.setStatus(1);
        sell.setCdate(new Date());
        userCashSellMapper.insert(sell);

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(user.getUserId());
        userBonus.setFromUserId(user.getUserId());
        userBonus.setTradeType(2);
        userBonus.setBonusType(7);
        userBonus.setInOrOut(2);
        userBonus.setAmount(sell.getAmount().add(sell.getBrokerage()));
        userBonus.setCashBonus(sell.getAmount());
        userBonus.setDTFTBonus(BigDecimal.ZERO);
        userBonus.setLYJFBonus(BigDecimal.ZERO);
        userBonus.setJTFTBonus(BigDecimal.ZERO);
        userBonus.setSCJFBonus(BigDecimal.ZERO);
        userBonus.setJYFBonus(sell.getBrokerage());
        userBonus.setRemark("积分卖出");
        userBonus.setIp(ip);
        userBonusService.insert(userBonus);

    }

    @Override
    @Transactional
    public void gathering(UserCashSell sell, String ip) {

        sell.setStatus(4);
        sell.setConfirmDate(new Date());
        sell.setConfirmIp(ip);
        userCashSellMapper.update(sell);

        UserCashBuy buy = userCashBuyService.queryByKey(sell.getBuyId());
        buy.setStatus(2);
        buy.setPayDate(null);
        userCashBuyService.update(buy);

        UserAsset userAsset = userAssetService.queryByUserId(buy.getUserId());
        userAsset.setBonusSurplus(userAsset.getBonusSurplus().add(sell.getAmount()));
        userAssetService.update(userAsset);

        UserBonus userBonus = new UserBonus();
        userBonus.setUserId(sell.getBuyUserId());
        userBonus.setFromUserId(sell.getUserId());
        userBonus.setTradeType(2);
        userBonus.setBonusType(7);
        userBonus.setInOrOut(1);
        userBonus.setAmount(sell.getAmount());
        userBonus.setCashBonus(sell.getAmount());
        userBonus.setDTFTBonus(BigDecimal.ZERO);
        userBonus.setLYJFBonus(BigDecimal.ZERO);
        userBonus.setJTFTBonus(BigDecimal.ZERO);
        userBonus.setSCJFBonus(BigDecimal.ZERO);
        userBonus.setJYFBonus(BigDecimal.ZERO);
        userBonus.setRemark("积分买入");
        userBonus.setIp(ip);
        userBonus.setCdate(new Date());
        userBonusService.insert(userBonus);
    }

    @Override
    public int update(UserCashSell sell) {
        return userCashSellMapper.update(sell);
    }
}
