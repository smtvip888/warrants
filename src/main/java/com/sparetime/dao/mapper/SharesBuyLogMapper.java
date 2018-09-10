package com.sparetime.dao.mapper;

import com.sparetime.domian.SharesBuyLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/8/22.
 */
public interface SharesBuyLogMapper {

    @Select("select * from Shares_Buy_Logs where buyId = #{buyId}")
    List<SharesBuyLog> selectListByBuyId(@Param("buyId")BigDecimal buyId);
}
