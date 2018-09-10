package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SharesPrice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
public interface SharesPriceMapper {

    @Select("select * from Shares_Price where id = (select max(id) from Shares_Price)")
    SharesPrice selectByMaxId();

    @Select("select " +
            "case WHEN maxP is null THEN (select price from Shares_Price where cdate < #{startTime} order by cdate desc limit 1) ELSE maxP END as maxP " +
            "from (" +
            "select max(Price) as maxP from Shares_Price where cdate >= #{startTime} and cdate <= #{endTime}" +
            ") t")
    BigDecimal selectMaxPriceByDate(@Param("startTime")Date date, @Param("endTime") Date endTime);

    @Select("<script>" +
            "select * from Shares_Price where 1=1 " +
            "<if test='price.price != null'>and price = #{price.price},</if>" +
            "<if test='price.shares != null'>and shares = #{price.shares},</if>" +
            "<if test='price.succesShares != null'>and succesShares = #{price.succesShares},</if>" +
            "<if test='price.userShares != null'>and userShares = #{price.userShares},</if>" +
            "order by id desc" +
            "</script>")
    List<SharesPrice> selectListByExample(@Param("price") SharesPrice price, Page page);

    @Update("update Shares_Price set shares = #{shares} where id = #{id}")
    int updateShares(@Param("shares") BigDecimal shares, @Param("id") Long id);

    @Select("select * from Shares_Price where id = #{id}")
    SharesPrice selectByKey(@Param("id") long id);

    @Insert("insert into Shares_Price (price, shares, succesShares, userShares, ip, adminUserName, cdate) values (" +
            "#{price.price}," +
            "#{price.shares}," +
            "#{price.succesShares}," +
            "#{price.userShares}," +
            "#{price.ip}," +
            "#{price.adminUserName}," +
            "#{price.cdate}" +
            ")")
    int insert(@Param("price") SharesPrice price);
}
