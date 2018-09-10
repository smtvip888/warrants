package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Share;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface ShareMapper {

    @Select("<script>" +
            "select * from Shares where 1 = 1 " +
            "<if test='share.buyId != null'>and buyId = #{share.buyId}</if>" +
            "<if test='share.userId != null'>and userId = #{share.userId}</if>" +
            "<if test='share.netUserId != null'>and netUserId = #{share.netUserId}</if>" +
            "<if test='share.shareType != null'>and shareType = #{share.shareType}</if>" +
            "<if test='share.isOriginalStake != null'>and isOriginalStake = #{share.isOriginalStake}</if>" +
            "<if test='share.price != null'>and price = #{share.price}</if>" +
            "<if test='share.shares != null'>and shares = #{share.shares}</if>" +
            "<if test='share.outShares != null'>and outShares = #{share.outShares}</if>" +
            "<if test='share.splitCount != null'>and splitCount = #{share.splitCount}</if>" +
            "<if test='share.remark != null'>and remark = #{share.remark}</if>" +
            "order by shareId desc" +
            "</script>")
    List<Share> selectListByExample(@Param("share") Share share, Page page);

    @Select("select * from Shares where shareId = #{id}")
    Share selectByKey(@Param("id") BigDecimal id);
}
