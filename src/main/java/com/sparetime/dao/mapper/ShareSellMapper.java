package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareSell;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface ShareSellMapper {

    @Select("<script>" +
            "select * from Shares_Sell where 1 = 1" +
            "<if test='sell.tradeSN != null'>and tradeSN like concat(concat('%',#{sell.tradeSN}),'%')</if>" +
            "<if test='sell.userId != null'>and userId = #{sell.userId}</if>" +
            "<if test='sell.NetUserId != null'>and NetUserId = #{sell.NetUserId}</if>" +
            "<if test='sell.Shares != null'>and Shares = #{sell.Shares}</if>" +
            "<if test='sell.price != null'>and price = #{sell.price}</if>" +
            "<if test='sell.succesShares != null'>and succesShares = #{sell.succesShares}</if>" +
            "<if test='sell.status != null'>and status = #{sell.status}</if>" +
            "order by sellId desc" +
            "</script>")
    List<ShareSell> selectListByExample(@Param("sell") ShareSell sell, Page page);

    @Select("select * from Shares_Sell where sellId = #{id}")
    ShareSell selectByKey(@Param("id") BigDecimal id);

    @Insert("insert into Shares_Sell (tradeSN, userId, netUserId, shareType, isOriginalStake, shares, price, succesShares, splitCount, tradeIds, status, ip, cdate) values (" +
            "#{shareSell.tradeSN},#{shareSell.userId},#{shareSell.netUserId},#{shareSell.shareType},#{shareSell.isOriginalStake},#{shareSell.shares}," +
            "#{shareSell.price},#{shareSell.succesShares},#{shareSell.splitCount},#{shareSell.tradeIds},#{shareSell.status}, #{shareSell.ip}, #{shareSell.cdate}" +
            ")")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = BigDecimal.class, keyColumn = "sellId", keyProperty = "shareSell.sellId", before = false)
    int insert(@Param("shareSell") ShareSell shareSell);

    @Select("select * from Shares_Sell where tradeSN like concat(concat('%',#{tradeSN}),'%') and userId = #{userId} order by sellId desc")
    List<ShareSell> selectSelfListLikeTradeSN(@Param("tradeSN") String tradeSN, @Param("userId") BigDecimal userId, Page page);
}
