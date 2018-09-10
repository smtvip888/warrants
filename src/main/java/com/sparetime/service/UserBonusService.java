package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserBonus;

import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
public interface UserBonusService {

    List<UserBonus> queryListByExample(UserBonus userBonus, Page page);

    int insert(UserBonus userBonus);
}
