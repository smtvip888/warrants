package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserCashBonusGet;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
public interface UserCashBonusGetService {

    List<UserCashBonusGet> queryListByExample(UserCashBonusGet userCashBonusGet, Page page);

    boolean changeStatus(Integer status, BigDecimal id);

    int insert(UserCashBonusGet userCashBonusGet);
}
