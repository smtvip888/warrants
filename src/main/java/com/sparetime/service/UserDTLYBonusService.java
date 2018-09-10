package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserDTLYBonus;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserDTLYBonusService {

    List<UserDTLYBonus> queryListByExample(UserDTLYBonus userDTLYBonus, Page page);
}
