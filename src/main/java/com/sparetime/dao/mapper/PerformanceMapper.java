package com.sparetime.dao.mapper;

import com.sparetime.domian.Performance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/23.
 */
public interface PerformanceMapper {

    @Insert("insert into performance (user_id,product_name,amount,create_time) values (#{performance.userId},#{performance.productName},#{performance.amount},#{performance.createTime})")
    int insert(@Param("performance")Performance performance);

    @Select("<script>" +
            "select sum(amount) from performance where 1=1 " +
            "<if test='performance.userId != null'>and user_id =#{performance.userId}</if>" +
            "<if test='performance.productName != null'>and product_name =#{performance.productName}</if>" +
            "<if test='performance.amount != null'>and amount =#{performance.amount}</if>" +
            "<if test='performance.startTime != null'><![CDATA[and create_time >= #{performance.startTime}]]></if>" +
            "<if test='performance.endTime != null'><![CDATA[and create_time <= #{performance.endTime}]]></if>" +
            "</script>")
    BigDecimal sum(@Param("performance") Performance performance);
}
