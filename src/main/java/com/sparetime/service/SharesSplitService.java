package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SharesSplit;

import java.util.List;

/**
 * Created by muye on 17/8/23.
 */
public interface SharesSplitService {

    List<SharesSplit> queryListByExample(SharesSplit split, Page page);
}
