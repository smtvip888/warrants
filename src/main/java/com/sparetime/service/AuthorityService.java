package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Authority;

import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
public interface AuthorityService {

    List<Authority> queryAuthByManagerId(Long managerId);

    List<Authority> queryListByExample(Authority authority, Page page);

    List<Authority> queryListByResourceId(Long resourceId);

    Authority queryByKey(Long id);

    Long insert(Authority authority);

    void update(Authority authority);

    void delete(Long id);

    List<Long> queryIdByResourceId(Long resourceId);

    boolean isWrite(Long resourceId, Long authId);
}
