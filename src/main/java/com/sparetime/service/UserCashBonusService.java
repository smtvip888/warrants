package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserCashBonus;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserCashBonusService {

    List<UserCashBonus> queryListByExample(UserCashBonus userCashBonus, Page page);
}
