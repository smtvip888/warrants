package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserCashBonus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserCashBonusMapper {

    @Select("<script>" +
            "select * from Users_Cash_Bonus where 1=1" +
            "<if test='userCashBonus.userId != null'>and userId = #{userCashBonus.userId}</if>" +
            "<if test='userCashBonus.netUserId != null'>and netUserId = #{userCashBonus.netUserId}</if>" +
            "<if test='userCashBonus.tradeType != null'>and tradeType = #{userCashBonus.tradeType}</if>" +
            "<if test='userCashBonus.bonusType != null'>and bonusType = #{userCashBonus.bonusType}</if>" +
            "<if test='userCashBonus.amount != null'>and amount = #{userCashBonus.amount}</if>" +
            "<if test='userCashBonus.fromUserId != null'>and fromUserId = #{userCashBonus.fromUserId}</if>" +
            "<if test='userCashBonus.inOrOut != null'>and inOrOut = #{userCashBonus.inOrOut}</if>" +
            "<if test='userCashBonus.remark != null'>and remark = #{userCashBonus.remark}</if>" +
            "<if test='userCashBonus.startTime != null'><![CDATA[and cdate >= #{userCashBonus.startTime}]]></if>" +
            "<if test='userCashBonus.endTime != null'><![CDATA[and cdate <= #{userCashBonus.endTime}]]></if>" +
            "</script>")
    List<UserCashBonus> selectListByExample(@Param("userCashBonus") UserCashBonus userCashBonus, Page page);
}
