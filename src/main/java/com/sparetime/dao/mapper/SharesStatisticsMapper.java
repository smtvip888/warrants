package com.sparetime.dao.mapper;

import com.sparetime.domian.SharesStatistics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/9/10.
 */
public interface SharesStatisticsMapper {

    @Select("select * from Shares_Statistics where cdate > #{cdate}")
    List<SharesStatistics> selectByAfterTime(@Param("cdate")Date cdate);
}
