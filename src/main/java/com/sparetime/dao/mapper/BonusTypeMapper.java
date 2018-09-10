package com.sparetime.dao.mapper;

import com.sparetime.domian.BonusType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/7/26.
 */
public interface BonusTypeMapper {

    @Select("select * from BonusType")
    List<BonusType> selectAll();
}
