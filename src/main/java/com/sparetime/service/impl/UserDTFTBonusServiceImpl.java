package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserDTFTBonusMapper;
import com.sparetime.domian.UserDTFTBonus;
import com.sparetime.service.UserDTFTBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class UserDTFTBonusServiceImpl implements UserDTFTBonusService{

    @Autowired
    private UserDTFTBonusMapper userDTFTBonusMapper;

    @Override
    public List<UserDTFTBonus> queryListByExample(UserDTFTBonus userDTFTBonus, Page page) {
        return userDTFTBonusMapper.selectListByExample(userDTFTBonus, page);
    }

    @Override
    public int insert(UserDTFTBonus userDTFTBonus) {
        return userDTFTBonusMapper.insert(userDTFTBonus);
    }
}
