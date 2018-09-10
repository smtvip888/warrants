package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserCashSell;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface UserCashSellMapper {

    @Select("<script>" +
            "select * from Users_Cash_Sell where 1 = 1" +
            "<if test='sell.currencyName != null'>and currencyName = #{sell.currencyName}</if>" +
            "<if test='sell.tradeSN != null'>and tradeSN = #{sell.tradeSN}</if>" +
            "<if test='sell.userId != null'>and userId = #{sell.userId}</if>" +
            "<if test='sell.status != null'>and status = #{sell.status}</if>" +
            "<if test='start != null'><![CDATA[and amount >= #{start}]]></if>" +
            "<if test='end != null'><![CDATA[and amount <= #{end}]]></if>" +
            "order by sellId desc" +
            "</script>"
    )
    List<UserCashSell> selectListByExample(@Param("sell") UserCashSell sell, @Param("start")BigDecimal start,
                                           @Param("end") BigDecimal end, Page page);

    @Select("select * from Users_Cash_Sell where sellId = #{sellId}")
    UserCashSell selectByKey(@Param("sellId") BigDecimal sellId);

    @Update("<script>" +
            "update Users_Cash_Sell set " +
            "tradeSN = #{sell.tradeSN}," +
            "userId = #{sell.userId}," +
            "amount = #{sell.amount}," +
            "brokerage = #{sell.brokerage}," +
            "currencyName = #{sell.currencyName}," +
            "exchangeRate = #{sell.exchangeRate}," +
            "bankName = #{sell.bankName}," +
            "bankAddress = #{sell.bankAddress}," +
            "accountName = #{sell.accountName}," +
            "accountNumber = #{sell.accountNumber}," +
            "mobile = #{sell.mobile}," +
            "buyId = #{sell.buyId}," +
            "buyUserId = #{sell.buyUserId}," +
            "ip = #{sell.ip}," +
            "status = #{sell.status}," +
//            "cdate = #{sell.cdate}," +
            "<if test='sell.confirmDate != null'>confirmDate = #{sell.confirmDate},</if> " +
            "confirmIp = #{sell.confirmIp} " +
            "where sellId = #{sell.sellId}" +
            "</script>"
    )
    int update(@Param("sell") UserCashSell sell);

    @Insert("insert into Users_Cash_Sell (" +
            "tradeSN," +
            "userId," +
            "amount," +
            "brokerage," +
            "currencyName," +
            "exchangeRate," +
            "bankName," +
            "bankAddress," +
            "accountName," +
            "accountNumber," +
            "mobile," +
            "buyId," +
            "buyUserId," +
            "ip," +
            "status," +
            "cdate," +
            "confirmIp," +
            "confirmDate) values (" +
            "#{sell.tradeSN}," +
            "#{sell.userId}," +
            "#{sell.amount}," +
            "#{sell.brokerage}," +
            "#{sell.currencyName}," +
            "#{sell.exchangeRate}," +
            "#{sell.bankName}," +
            "#{sell.bankAddress}," +
            "#{sell.accountName}," +
            "#{sell.accountNumber}," +
            "#{sell.mobile}," +
            "#{sell.buyId}," +
            "#{sell.buyUserId}," +
            "#{sell.ip}," +
            "#{sell.status}," +
            "#{sell.cdate}," +
            "#{sell.confirmIp}," +
            "#{sell.confirmDate})")
    int insert(@Param("sell") UserCashSell sell);
}
