package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SysConfig;

import java.util.List;

/**
 * Created by muye on 17/8/28.
 */
public interface SysConfigService {

    SysConfig queryByKey(String key);

    List<SysConfig> queryListByExample(SysConfig config, Page page);

    int updateValue(String value, String key);

    int add(SysConfig config);
}
