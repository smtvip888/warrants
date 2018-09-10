package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserCashBonusMapper;
import com.sparetime.domian.UserCashBonus;
import com.sparetime.service.UserCashBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class UserCashBonusServiceImpl implements UserCashBonusService {

    @Autowired
    private UserCashBonusMapper userCashBonusMapper;

    @Override
    public List<UserCashBonus> queryListByExample(UserCashBonus userCashBonus, Page page) {
        return userCashBonusMapper.selectListByExample(userCashBonus, page);
    }
}
