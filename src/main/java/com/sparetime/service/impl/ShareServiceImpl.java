package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.ShareMapper;
import com.sparetime.domian.Share;
import com.sparetime.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareMapper shareMapper;

    @Override
    public List<Share> queryListByExample(Share share, Page page) {
        return shareMapper.selectListByExample(share, page);
    }

    @Override
    public Share queryByKey(BigDecimal id) {
        return shareMapper.selectByKey(id);
    }
}
