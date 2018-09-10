package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.UserJTSCBonusMapper;
import com.sparetime.domian.UserJTSCBonus;
import com.sparetime.service.UserJTSCBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
@Service
public class UserJTSCBonusServiceImpl implements UserJTSCBonusService {

    @Autowired
    private UserJTSCBonusMapper userJTSCBonusMapper;

    @Override
    public List<UserJTSCBonus> queryListByExample(UserJTSCBonus userJTSCBonus, Page page) {
        return userJTSCBonusMapper.selectListByExample(userJTSCBonus, page);
    }
}
