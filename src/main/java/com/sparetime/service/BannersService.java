package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Banners;

import java.util.List;

public interface BannersService {

    List<Banners> queryListByExample(Banners banners, Page page);

    Banners queryByKey(Long id);

    int insert(Banners banners);

    int update(Banners banners);

    int delete(Long id);
}
