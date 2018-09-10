package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SharesSplit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/8/23.
 */
public interface SharesSplitMapper {

    @Select("<script>" +
            "select * from Shares_Split where 1=1 " +
            "<if test='split.splitPrice != null'>and splitPrice=#{split.splitPrice},</if>" +
            "<if test='split.afterSplitPrice != null'>and afterSplitPrice=#{split.afterSplitPrice},</if>" +
            "<if test='split.lastNetUserId != null'>and lastNetUserId=#{split.lastNetUserId},</if>" +
            "<if test='split.userCount != null'>and userCount=#{split.userCount},</if>" +
            "<if test='split.largessShares != null'>and largessShares=#{split.largessShares},</if>" +
            "order by cdate desc" +
            "</script>")
    List<SharesSplit> selectListByExample(@Param("split") SharesSplit split, Page page);
}
