package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserJTSCBonus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserJTSCBonusMapper {

    @Select("<script>" +
            "select * from Users_JTSC_Bonus where 1=1" +
            "<if test='userJTSCBonus.userId != null'>and userId = #{userJTSCBonus.userId}</if>" +
            "<if test='userJTSCBonus.netUserId != null'>and netUserId = #{userJTSCBonus.netUserId}</if>" +
            "<if test='userJTSCBonus.tradeType != null'>and tradeType = #{userJTSCBonus.tradeType}</if>" +
            "<if test='userJTSCBonus.bonusType != null'>and bonusType = #{userJTSCBonus.bonusType}</if>" +
            "<if test='userJTSCBonus.amount != null'>and amount = #{userJTSCBonus.amount}</if>" +
            "<if test='userJTSCBonus.fromUserId != null'>and fromUserId = #{userJTSCBonus.fromUserId}</if>" +
            "<if test='userJTSCBonus.inOrOut != null'>and inOrOut = #{userJTSCBonus.inOrOut}</if>" +
            "<if test='userJTSCBonus.remark != null'>and remark = #{userJTSCBonus.remark}</if>" +
            "<if test='userJTSCBonus.startTime != null'><![CDATA[and cdate >= #{userJTSCBonus.startTime}]]></if>" +
            "<if test='userJTSCBonus.endTime != null'><![CDATA[and cdate <= #{userJTSCBonus.endTime}]]></if>" +
            "</script>")
    List<UserJTSCBonus> selectListByExample(@Param("userJTSCBonus") UserJTSCBonus userJTSCBonus, Page page);
}
