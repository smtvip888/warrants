package com.sparetime.service.impl;

import com.sparetime.dao.mapper.NetUserMapper;
import com.sparetime.domian.NetUser;
import com.sparetime.service.NetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/14.
 */
@Service
public class NetUserServiceImpl implements NetUserService {

    @Autowired
    private NetUserMapper netUserMapper;

    @Override
    public int insert(NetUser netUser) {
        return netUserMapper.insert(netUser);
    }

    @Override
    public NetUser queryByUserId(BigDecimal userId) {
        return netUserMapper.selectByUserId(userId);
    }
}
