package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserJTFTBonus;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserJTFTBonusService {

    List<UserJTFTBonus> queryListByExample(UserJTFTBonus userJTFTBonus, Page page);

    int insert(UserJTFTBonus userJTFTBonus);
}
