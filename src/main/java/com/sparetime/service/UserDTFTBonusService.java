package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserDTFTBonus;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserDTFTBonusService {

    List<UserDTFTBonus> queryListByExample(UserDTFTBonus userDTFTBonus, Page page);

    int insert(UserDTFTBonus userDTFTBonus);
}
