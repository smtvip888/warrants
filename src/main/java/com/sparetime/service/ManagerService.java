package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Manager;

import java.util.List;

/**
 * Created by muye on 17/7/18.
 */
public interface ManagerService {

    Manager queryManagerByName(String name);

    List<Manager> queryByExample(Manager manager, Page page);

    Long addManager(Manager manager, Long[] authIds);

    boolean existByName(String name);

    void update(Manager manager, Long[] authIds);

    void delete(Long id);

    Manager queryByKey(Long id);
}
