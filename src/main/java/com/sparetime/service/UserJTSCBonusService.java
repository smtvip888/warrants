package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserJTSCBonus;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserJTSCBonusService {

    List<UserJTSCBonus> queryListByExample(UserJTSCBonus userJTSCBonus, Page page);
}
