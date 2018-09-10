package com.sparetime.service.impl;

import com.sparetime.dao.mapper.BonusTypeMapper;
import com.sparetime.domian.BonusType;
import com.sparetime.service.BonusTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by muye on 17/7/26.
 */
@Service
public class BonusTypeServiceImpl implements BonusTypeService {

    @Autowired
    private BonusTypeMapper bonusTypeMapper;

    @Override
    public List<BonusType> queryAll() {
        return bonusTypeMapper.selectAll();
    }
}
