package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.UserCashBuyMapper;
import com.sparetime.dao.mapper.UserCashSellMapper;
import com.sparetime.domian.User;
import com.sparetime.domian.UserCashBuy;
import com.sparetime.domian.UserCashBuyImage;
import com.sparetime.domian.UserCashSell;
import com.sparetime.service.UserCashBuyService;
import com.sparetime.service.UserCashImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserCashBuyServiceImpl implements UserCashBuyService {

    @Autowired
    private UserCashBuyMapper userCashBuyMapper;

    @Autowired
    private UserCashSellMapper userCashSellMapper;

    @Autowired
    private UserCashImageService userCashImageService;

    @Override
    @Transactional
    public UserCashBuy buy(User user, UserCashSell sell, String ip) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");

        UserCashBuy buy = new UserCashBuy();
        buy.setUserId(user.getUserId());
        buy.setAmount(sell.getAmount());
        buy.setCurrencyName(sell.getCurrencyName());
        buy.setExchangeRate(sell.getExchangeRate());
        buy.setIp(ip);
        buy.setStatus(1);
        buy.setCdate(new Date());
        buy.setSellId(sell.getSellId());
        buy.setSellUserId(sell.getUserId());
        buy.setTradeSN(sdf.format(new Date()));
        userCashBuyMapper.insert(buy);

        sell.setStatus(2);
        sell.setBuyId(buy.getBuyId());
        sell.setBuyUserId(user.getUserId());
        sell.setConfirmDate(null);
        userCashSellMapper.update(sell);

        return buy;
    }

    @Override
    public List<UserCashBuy> queryListByExample(UserCashBuy query, Page page) {
        FieldUtil.spaceToNull(query);
        return userCashBuyMapper.selectListByExample(query, page);
    }

    @Override
    public UserCashBuy queryByKey(BigDecimal buyId) {
        return userCashBuyMapper.selectByKey(buyId);
    }

    @Override
    @Transactional
    public void buyImgCommit(BigDecimal buyId, List<String> paths, String ip) {

        UserCashBuy buy = userCashBuyMapper.selectByKey(buyId);
        buy.setPayDate(new Date());
        buy.setPayIp(ip);
        userCashBuyMapper.update(buy);

        List<UserCashBuyImage> list = new ArrayList<>();
        paths.forEach(image -> {
            UserCashBuyImage userCashBuyImage = new UserCashBuyImage();
            userCashBuyImage.setBuyId(buyId);
            userCashBuyImage.setImgPath(image);
            userCashBuyImage.setIp(ip);
            userCashBuyImage.setCdate(new Date());
            list.add(userCashBuyImage);
        });

        userCashImageService.deleteByBuyId(buyId);
        list.forEach(image -> {
            userCashImageService.insert(image);
        });

        UserCashSell sell = userCashSellMapper.selectByKey(buy.getSellId());
        sell.setStatus(3);
        sell.setConfirmDate(null);
        userCashSellMapper.update(sell);
    }

    @Override
    public int update(UserCashBuy buy) {
        return userCashBuyMapper.update(buy);
    }
}
