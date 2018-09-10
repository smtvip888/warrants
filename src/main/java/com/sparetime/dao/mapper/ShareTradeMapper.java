package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareTrade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface ShareTradeMapper {

    @Select("<script>" +
            "select * from Shares_Trade where 1 = 1" +
            "<if test='shareTrade.shareId != null'>and shareId = #{shareTrade.shareId}</if>" +
            "<if test='shareTrade.userId != null'>and userId = #{shareTrade.userId}</if>" +
            "<if test='shareTrade.netUserId != null'>and netUserId = #{shareTrade.netUserId}</if>" +
            "<if test='shareTrade.shareType != null'>and shareType = #{shareTrade.shareType}</if>" +
            "<if test='shareTrade.isOriginalStake != null'>and isOriginalStake = #{shareTrade.isOriginalStake}</if>" +
            "<if test='shareTrade.price != null'>and price = #{shareTrade.price}</if>" +
            "<if test='shareTrade.shares != null'>and shares = #{shareTrade.shares}</if>" +
            "<if test='shareTrade.sellShares != null'>and sellShares = #{shareTrade.sellShares}</if>" +
            "<if test='shareTrade.splitCount != null'>and splitCount = #{shareTrade.splitCount}</if>" +
            "<if test='shareTrade.remark != null'>and remark = #{shareTrade.remark}</if>" +
            "</script>")
    List<ShareTrade> selectListByExample(@Param("shareTrade") ShareTrade shareTrade, Page page);

    @Select("select * from Shares_Trade where id = #{id}")
    ShareTrade selectByKey(@Param("id")BigDecimal id);

    @Select("SELECT IFNULL( SUM(Shares - SellShares),0) FROM Shares_Trade WHERE UserId = #{userId} AND (Shares - SellShares) > 0")
    BigDecimal sellSurplus(@Param("userId")BigDecimal userId);
}
