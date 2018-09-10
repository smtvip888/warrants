package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserJTFTBonusMapper;
import com.sparetime.domian.UserJTFTBonus;
import com.sparetime.service.UserJTFTBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class UserJTFTBonusServiceImpl implements UserJTFTBonusService {

    @Autowired
    private UserJTFTBonusMapper userJTFTBonusMapper;

    @Override
    public List<UserJTFTBonus> queryListByExample(UserJTFTBonus userJTFTBonus, Page page) {
        return userJTFTBonusMapper.selectListByExample(userJTFTBonus, page);
    }

    @Override
    public int insert(UserJTFTBonus userJTFTBonus) {
        return userJTFTBonusMapper.insert(userJTFTBonus);
    }
}
