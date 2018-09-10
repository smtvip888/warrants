package com.sparetime.dao.mapper;

import com.sparetime.domian.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

    @Insert("insert into Orders (userId, netUserId, productId, price, payAmount, isUp, payUserId, cdate) value (" +
            "#{order.userId}, #{order.netUserId}, #{order.productId}, #{order.price}, #{order.payAmount}, #{order.isUp}, #{order.payUserId}," +
            "#{order.cdate})")
    int insert(@Param("order")Order order);
}
