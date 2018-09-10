package com.sparetime.dao.mapper;

import com.sparetime.domian.UserCashBuyImage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface UserCashBuyImageMapper {

    @Select("select * from Users_Cash_Buy_Images where buyId = #{buyId}")
    List<UserCashBuyImage> selectListByBuyId(@Param("buyId")BigDecimal buyId);

    @Insert("insert into Users_Cash_Buy_Images (buyId, imgPath, ip, cdate) values (#{image.buyId}, #{image.imgPath}, #{image.ip}, #{image.cdate})")
    int insert(@Param("image") UserCashBuyImage image);

    @Delete("delete from Users_Cash_Buy_Images where buyId = #{buyId}")
    int deleteByBuyId(@Param("buyId") BigDecimal buyId);
}
