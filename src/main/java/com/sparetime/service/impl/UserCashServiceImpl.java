package com.sparetime.service.impl;

import com.sparetime.dao.mapper.UserCashBuyImageMapper;
import com.sparetime.domian.UserCashBuyImage;
import com.sparetime.service.UserCashImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserCashServiceImpl implements UserCashImageService {

    @Autowired
    private UserCashBuyImageMapper userCashBuyImageMapper;

    @Override
    public List<UserCashBuyImage> queryListByBuyId(BigDecimal buyId) {
        return userCashBuyImageMapper.selectListByBuyId(buyId);
    }

    @Override
    public int insert(UserCashBuyImage image) {
        return userCashBuyImageMapper.insert(image);
    }

    @Override
    public int deleteByBuyId(BigDecimal buyId) {
        return userCashBuyImageMapper.deleteByBuyId(buyId);
    }
}
