package com.sparetime.dao.mapper;

import com.sparetime.domian.NetUser;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

/**
 * Created by muye on 17/7/25.
 */
public interface NetUserMapper {

    @Insert("<script>" +
            "insert into Net_Users (" +
            "userId,recommendUserId,parentUserId,placeArea,productId,price,totalProfit,lastSalePrice,isOut,cdate" +
            ") values (" +
            "#{netUser.userId},#{netUser.recommendUserId},#{netUser.parentUserId},#{netUser.placeArea},#{netUser.productId}," +
            "#{netUser.price},#{netUser.totalProfit},#{netUser.lastSalePrice},#{netUser.isOut},#{netUser.cdate}" +
            ")" +
            "</script>")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = BigDecimal.class, keyColumn = "netUserId", keyProperty = "netUser.netUserId", before = false)
    int insert(@Param("netUser")NetUser netUser);

    @Select("select * from Net_Users where userId = #{userId} order by netUserId desc limit 1")
    NetUser selectByUserId(@Param("userId") BigDecimal userId);

    @Update("<script>" +
            "update Net_Users" +
            "<set>" +
            "<if test='netUser.userId != null'>userId = #{netUser.userId},</if>" +
            "<if test='netUser.recommendUserId != null'>recommendUserId = #{netUser.recommendUserId},</if>" +
            "<if test='netUser.parentUserId != null'>parentUserId = #{netUser.parentUserId},</if>" +
            "<if test='netUser.placeArea != null'>placeArea = #{netUser.placeArea},</if>" +
            "<if test='netUser.productId != null'>productId = #{netUser.productId},</if>" +
            "<if test='netUser.price != null'>price = #{netUser.price},</if>" +
            "<if test='netUser.totalProfit != null'>totalProfit = #{netUser.totalProfit},</if>" +
            "<if test='netUser.lastSalePrice != null'>lastSalePrice = #{netUser.lastSalePrice},</if>" +
            "<if test='netUser.isOut != null'>isOut = #{netUser.isOut},</if>" +
            "</set>" +
            "where netUserId = #{netUser.netUserId}" +
            "</script>")
    int update(@Param("netUser")NetUser netUser);
}
