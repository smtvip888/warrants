package com.sparetime.dao.mapper;

import com.sparetime.domian.ShareSellBuyCross;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface ShareSellBuyCrossMapper {

    @Select("select * from Shares_Sell_Buy_Cross where sellId = #{sellId}")
    List<ShareSellBuyCross> selectBySellId(@Param("sellId")BigDecimal sellId);

    @Select("select * from Shares_Sell_Buy_Cross where buyId = #{buyId}")
    List<ShareSellBuyCross> selectByBuyId(@Param("buyId")BigDecimal buyId);
}
