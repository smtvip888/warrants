package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.ShareBuy;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface ShareBuyMapper {

    @Select("<script>" +
            "select * from Shares_Buy where 1=1" +
            "<if test='shareBuy.tradeSN != null'>and tradeSN like concat(concat('%',#{shareBuy.tradeSN}),'%')</if>" +
            "<if test='shareBuy.userId != null'>and userId = #{shareBuy.userId}</if>" +
            "<if test='shareBuy.netUserId != null'>and netUserId = #{shareBuy.netUserId}</if>" +
            "<if test='shareBuy.shares != null'>and shares = #{shareBuy.shares}</if>" +
            "<if test='shareBuy.price != null'>and price = #{shareBuy.price}</if>" +
            "<if test='shareBuy.succesShares != null'>and succesShares = #{shareBuy.succesShares}</if>" +
            "<if test='shareBuy.status != null'>and status = #{shareBuy.status}</if>" +
            "<if test='shareBuy.status != null and shareBuy.status == 1'>order by buyId</if>" +
            "<if test='shareBuy.status == null or shareBuy.status != 1'>order by buyId desc</if>" +
            "</script>")
    List<ShareBuy> selectListByExample(@Param("shareBuy") ShareBuy shareBuy, Page page);

    @Select("<script>" +
            "select sum(price) from Shares_Buy where 1=1" +
            "<if test='shareBuy.tradeSN != null'>and tradeSN like concat(concat('%',#{shareBuy.tradeSN}),'%')</if>" +
            "<if test='shareBuy.userId != null'>and userId = #{shareBuy.userId}</if>" +
            "<if test='shareBuy.netUserId != null'>and netUserId = #{shareBuy.netUserId}</if>" +
            "<if test='shareBuy.shares != null'>and shares = #{shareBuy.shares}</if>" +
            "<if test='shareBuy.price != null'>and price = #{shareBuy.price}</if>" +
            "<if test='shareBuy.succesShares != null'>and succesShares = #{shareBuy.succesShares}</if>" +
            "<if test='shareBuy.status != null'>and status = #{shareBuy.status}</if>" +
            "order by buyId desc" +
            "</script>")
    BigDecimal sumBuyPrice(@Param("shareBuy") ShareBuy shareBuy);

    @Insert("insert into Shares_Buy (tradeSN, userId, netUserId, fundType, price, buyPrice, succesPrice, shares, succesShares, status, ip, cdate) values (" +
            "#{shareBuy.tradeSN}, #{shareBuy.userId}, #{shareBuy.netUserId}, #{shareBuy.fundType}, #{shareBuy.price}, #{shareBuy.buyPrice}, #{shareBuy.succesPrice}, " +
            "#{shareBuy.shares}, #{shareBuy.succesShares}, #{shareBuy.status}, #{shareBuy.ip}, #{shareBuy.cdate}" +
            ")")
    int insert(@Param("shareBuy") ShareBuy shareBuy);

    @Select("select * from Shares_Buy where buyId = #{id}")
    ShareBuy selectByKey(@Param("id")BigDecimal id);

    @Select("select * from Shares_Buy where tradeSN like concat(concat('%',#{tradeSN}),'%') and userId = #{userId} order by buyId desc")
    List<ShareBuy> selectSelfListLikeTradeSN(@Param("tradeSN") String tradeSN, @Param("userId") BigDecimal userId, Page page);
}
