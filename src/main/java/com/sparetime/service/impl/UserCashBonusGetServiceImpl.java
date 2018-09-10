package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.UserCashBonusGetMapper;
import com.sparetime.domian.UserCashBonusGet;
import com.sparetime.service.UserCashBonusGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
@Service
public class UserCashBonusGetServiceImpl implements UserCashBonusGetService {

    @Autowired
    private UserCashBonusGetMapper userCashBonusGetMapper;

    @Override
    public List<UserCashBonusGet> queryListByExample(UserCashBonusGet userCashBonusGet, Page page) {
        FieldUtil.spaceToNull(userCashBonusGet);
        return userCashBonusGetMapper.selectListByExample(userCashBonusGet, page);
    }

    @Override
    public boolean changeStatus(Integer status, BigDecimal id) {

        UserCashBonusGet userCashBonusGet = new UserCashBonusGet();
        userCashBonusGet.setId(id.longValue());
        userCashBonusGet.setStatus(status);
        return userCashBonusGetMapper.updateSelective(userCashBonusGet) > 0;
    }

    @Override
    public int insert(UserCashBonusGet userCashBonusGet) {
        return userCashBonusGetMapper.insert(userCashBonusGet);
    }
}
