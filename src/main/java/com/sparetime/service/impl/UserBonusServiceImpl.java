package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserBonusMapper;
import com.sparetime.domian.UserBonus;
import com.sparetime.service.UserBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
@Service
public class UserBonusServiceImpl implements UserBonusService {

    @Autowired
    private UserBonusMapper userBonusMapper;

    @Override
    public List<UserBonus> queryListByExample(UserBonus userBonus, Page page) {
        return userBonusMapper.selectListByExample(userBonus, page);
    }

    @Override
    public int insert(UserBonus userBonus) {
        userBonus.setCdate(new Date());
        return userBonusMapper.insert(userBonus);
    }
}
