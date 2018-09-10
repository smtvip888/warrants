package com.sparetime.service;

import com.sparetime.domian.UserProfile;

import java.math.BigDecimal;
import java.util.Date;

public interface UserProfileService {

    UserProfile queryByUserId(BigDecimal userId);

    int update(UserProfile userProfile);

    int countByIdCard(String idCard, Date regDate);
}
