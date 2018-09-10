package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserDTLYBonusMapper;
import com.sparetime.domian.UserDTLYBonus;
import com.sparetime.service.UserDTLYBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class UserDTLYBonusServiceImpl implements UserDTLYBonusService {

    @Autowired
    private UserDTLYBonusMapper userDTLYBonusMapper;

    @Override
    public List<UserDTLYBonus> queryListByExample(UserDTLYBonus userDTLYBonus, Page page) {
        return userDTLYBonusMapper.selectListByExample(userDTLYBonus, page);
    }
}
