package com.sparetime.dao.mapper;

import com.sparetime.domian.SharesSellRefundLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/8/22.
 */
public interface SharesSellRefundLogMapper {

    @Select("select * from Shares_Sell_Refund_Logs where sellId = #{sellId}")
    List<SharesSellRefundLog> selectListBySellId(@Param("sellId") BigDecimal sellId);
}
