package com.sparetime.service;

import com.sparetime.domian.SharesConfig;

/**
 * Created by muye on 17/8/8.
 */
public interface SharesConfigService {

    int clean();

    SharesConfig getConfig();

    int change(SharesConfig sharesConfig);
}
