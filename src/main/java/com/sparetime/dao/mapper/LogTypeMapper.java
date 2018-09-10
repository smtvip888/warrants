package com.sparetime.dao.mapper;

import com.sparetime.domian.LogType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/10/17.
 */
public interface LogTypeMapper {

    @Select("select * from LogsType order by sort")
    List<LogType> selectAll();
}
