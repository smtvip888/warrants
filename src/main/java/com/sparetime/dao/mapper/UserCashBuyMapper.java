package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserCashBuy;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface UserCashBuyMapper {

    @Insert("insert into Users_Cash_Buy (tradeSN,userId,amount,currencyName,exchangeRate,sellId,sellUserId,ip,status,cdate,payIp,payDate) " +
            "values (" +
            "#{buy.tradeSN}," +
            "#{buy.userId}," +
            "#{buy.amount}," +
            "#{buy.currencyName}," +
            "#{buy.exchangeRate}," +
            "#{buy.sellId}," +
            "#{buy.sellUserId}," +
            "#{buy.ip}," +
            "#{buy.status}," +
            "#{buy.cdate}," +
            "#{buy.payIp}," +
            "#{buy.payDate}" +
            ")")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = BigDecimal.class, keyColumn = "buyId", keyProperty = "buy.buyId", before = false)
    int insert(@Param("buy")UserCashBuy buy);

    @Select("<script>" +
            "select * from Users_Cash_Buy where 1 = 1 " +
            "<if test = 'buy.userId != null'>and userId = #{buy.userId}</if>" +
            "<if test = 'buy.status != null'>and status = #{buy.status}</if>" +
            "<if test = 'buy.tradeSN != null'>and tradeSN = #{buy.tradeSN}</if>" +
            "order by buyId desc" +
            "</script>")
    List<UserCashBuy> selectListByExample(@Param("buy") UserCashBuy buy, Page page);

    @Select("select * from Users_Cash_Buy where buyId = #{buyId}")
    UserCashBuy selectByKey(@Param("buyId") BigDecimal buyId);

    @Update("<script>" +
            "update Users_Cash_Buy set " +
            "tradeSN = #{buy.tradeSN}," +
            "userId = #{buy.userId}," +
            "amount = #{buy.amount}," +
            "currencyName = #{buy.currencyName}," +
            "exchangeRate = #{buy.exchangeRate}," +
            "sellId = #{buy.sellId}," +
            "sellUserId = #{buy.sellUserId}," +
            "ip = #{buy.ip}," +
            "status = #{buy.status}," +
            "<if test='buy.payDate != null'>payDate = #{buy.payDate}, </if> " +
            "payIp = #{buy.payIp}" +
            "where buyId = #{buy.buyId}" +
            "</script>"
    )
    int update(@Param("buy") UserCashBuy buy);
}
