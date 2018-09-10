package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserDTLYBonus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserDTLYBonusMapper {

    @Select("<script>" +
            "select * from Users_DTLY_Bonus where 1=1" +
            "<if test='userDTLYBonus.userId != null'>and userId = #{userDTLYBonus.userId}</if>" +
            "<if test='userDTLYBonus.netUserId != null'>and netUserId = #{userDTLYBonus.netUserId}</if>" +
            "<if test='userDTLYBonus.tradeType != null'>and tradeType = #{userDTLYBonus.tradeType}</if>" +
            "<if test='userDTLYBonus.bonusType != null'>and bonusType = #{userDTLYBonus.bonusType}</if>" +
            "<if test='userDTLYBonus.amount != null'>and amount = #{userDTLYBonus.amount}</if>" +
            "<if test='userDTLYBonus.fromUserId != null'>and fromUserId = #{userDTLYBonus.fromUserId}</if>" +
            "<if test='userDTLYBonus.inOrOut != null'>and inOrOut = #{userDTLYBonus.inOrOut}</if>" +
            "<if test='userDTLYBonus.remark != null'>and remark = #{userDTLYBonus.remark}</if>" +
            "<if test='userDTLYBonus.startTime != null'><![CDATA[and cdate >= #{userDTLYBonus.startTime}]]></if>" +
            "<if test='userDTLYBonus.endTime != null'><![CDATA[and cdate <= #{userDTLYBonus.endTime}]]></if>" +
            "</script>")
    List<UserDTLYBonus> selectListByExample(@Param("userDTLYBonus") UserDTLYBonus userDTLYBonus, Page page);
}
