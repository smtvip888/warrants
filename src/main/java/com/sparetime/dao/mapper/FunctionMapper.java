package com.sparetime.dao.mapper;

import com.sparetime.domian.TodayPrice;
import com.sparetime.domian.User;
import com.sparetime.domian.extend.SharesNum;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
public interface FunctionMapper {

    @Select("call P_PayBonus(#{userId})")
    Object callP_PayBonus(@Param("userId")BigDecimal userId);

    @Select("call P_Auto_Match_Buy()")
    Object callP_Auto_Match_Buy();

    @Select("call P_GetUserCanSellShares(#{userId})")
    BigDecimal callP_GetUserCanSellShares(@Param("userId") BigDecimal userId);

//    @Select("call P_UpdateUserCanSellShares(#{userId}, #{shares})")
//    Object callP_UpdateUserCanSellShares(@Param("userId") BigDecimal userId, @Param("shares") BigDecimal shares);

    @Select("call P_UpdateUserCanSellShares(#{sellId})")
    Object callP_UpdateUserCanSellShares(@Param("sellId") BigDecimal sellId);

    @Select("call P_GetTodaySharePrice()")
    List<TodayPrice> callP_GetTodaySharePrice();

    @Select("call P_PayBonus_Upgrade(#{userId}, #{diff})")
    Object callP_PayBonus_Upgrade(@Param("userId")BigDecimal userId, @Param("diff") BigDecimal diff);

    @Select("call P_GetUserCanSellShares_JT(#{userId})")
    SharesNum callP_GetUserCanSellShares_JT(@Param("userId") BigDecimal userId);

    @Select("call P_GetUserCanSellShares_DT(#{userId})")
    SharesNum callP_GetUserCanSellShares_DT(@Param("userId") BigDecimal userId);

    @Select("call P_GetParentUserIds(#{parentId})")
    String callP_GetParentUserIds(@Param("parentId") BigDecimal parentId);

    @Select("call P_SplitShares()")
    Object callP_SplitShares();

    @Select("call P_GetTransferUserIds(#{out}, #{in})")
    String callP_GetTransferUserIds(@Param("out") BigDecimal out, @Param("in") BigDecimal in);

    @Select("call P_Shares_Sell(#{sellId})")
    Object callP_Shares_Sell(BigDecimal sellId);

    @Select("call P_GuidanceSell(#{startTime}, #{endTime}, #{productId}, #{num})")
    Object callGuidanceSell(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("productId") Integer productId, @Param("num") BigDecimal num);

    @Select("call P_Shares_Auto_Buy(#{num})")
    Object callP_Shares_Auto_Buy(@Param("num") Integer num);
}
