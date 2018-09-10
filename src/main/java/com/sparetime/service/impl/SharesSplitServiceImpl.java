package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.dao.mapper.SharesSplitMapper;
import com.sparetime.domian.SharesSplit;
import com.sparetime.service.SharesSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/8/23.
 */
@Service
public class SharesSplitServiceImpl implements SharesSplitService {

    @Autowired
    private SharesSplitMapper sharesSplitMapper;

    @Override
    public List<SharesSplit> queryListByExample(SharesSplit split, Page page) {
        return sharesSplitMapper.selectListByExample(split, page);
    }
}
