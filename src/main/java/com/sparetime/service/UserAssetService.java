package com.sparetime.service;

import com.sparetime.domian.User;
import com.sparetime.domian.UserAsset;
import com.sparetime.domian.UserCashBonusGet;
import com.sparetime.domian.UserZCJF;

import java.math.BigDecimal;

public interface UserAssetService {

    UserAsset queryByUserId(BigDecimal userId);

    void addZCJF(User user, BigDecimal fromUserId, String remark, BigDecimal amount);

    void delZCJF(UserZCJF userZCJF);

    int update(UserAsset userAsset);

    void transferZCJF(User out, User in, BigDecimal num, String ip);

    void convert(BigDecimal userId, BigDecimal surplus, String ip);

    void getCash(BigDecimal userId, UserCashBonusGet cashBonusGet);

    void deductLYJFYE(String userName, BigDecimal amount, String remark);
}
