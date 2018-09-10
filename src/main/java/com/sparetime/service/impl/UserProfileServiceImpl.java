package com.sparetime.service.impl;

import com.sparetime.dao.mapper.UserProfileMapper;
import com.sparetime.domian.UserProfile;
import com.sparetime.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public UserProfile queryByUserId(BigDecimal userId) {
        return userProfileMapper.selectByUserId(userId);
    }

    @Override
    public int update(UserProfile userProfile) {
        return userProfileMapper.update(userProfile);
    }

    @Override
    public int countByIdCard(String idCard, Date regDate) {
        return userProfileMapper.countByIdCard(idCard, regDate);
    }
}
