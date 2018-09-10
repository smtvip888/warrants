package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.BannersMapper;
import com.sparetime.domian.Banners;
import com.sparetime.service.BannersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannersServiceImpl implements BannersService {

    @Autowired
    private BannersMapper bannersMapper;

    @Override
    public List<Banners> queryListByExample(Banners banners, Page page) {
        return bannersMapper.selectListByExample(banners, page);
    }

    @Override
    public Banners queryByKey(Long id) {
        return bannersMapper.selectByKey(id);
    }

    @Override
    public int insert(Banners banners) {
        return bannersMapper.insert(banners);
    }

    @Override
    public int update(Banners banners) {
        return bannersMapper.update(banners);
    }

    @Override
    public int delete(Long id) {
        return bannersMapper.delete(id);
    }
}
