package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.News;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/26.
 */
public interface NewsService {

    List<News> queryListByExample(News news, Page page);

    News queryByKey(BigDecimal id);

    Long addNews(News news);

    int update(News news);

    int delete(BigDecimal id);
}
