package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Share;

import java.math.BigDecimal;
import java.util.List;

public interface ShareService {

    List<Share> queryListByExample(Share share, Page page);

    Share queryByKey(BigDecimal id);
}
