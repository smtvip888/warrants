package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserJTFTBonus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserJTFTBonusMapper {

    @Select("<script>" +
            "select * from Users_JTFT_Bonus where 1=1" +
            "<if test='userJTFTBonus.userId != null'>and userId = #{userJTFTBonus.userId}</if>" +
            "<if test='userJTFTBonus.netUserId != null'>and netUserId = #{userJTFTBonus.netUserId}</if>" +
            "<if test='userJTFTBonus.tradeType != null'>and tradeType = #{userJTFTBonus.tradeType}</if>" +
            "<if test='userJTFTBonus.bonusType != null'>and bonusType = #{userJTFTBonus.bonusType}</if>" +
            "<if test='userJTFTBonus.amount != null'>and amount = #{userJTFTBonus.amount}</if>" +
            "<if test='userJTFTBonus.fromUserId != null'>and fromUserId = #{userJTFTBonus.fromUserId}</if>" +
            "<if test='userJTFTBonus.inOrOut != null'>and inOrOut = #{userJTFTBonus.inOrOut}</if>" +
            "<if test='userJTFTBonus.remark != null'>and remark = #{userJTFTBonus.remark}</if>" +
            "<if test='userJTFTBonus.startTime != null'><![CDATA[and cdate >= #{userJTFTBonus.startTime}]]></if>" +
            "<if test='userJTFTBonus.endTime != null'><![CDATA[and cdate <= #{userJTFTBonus.endTime}]]></if>" +
            "</script>")
    List<UserJTFTBonus> selectListByExample(@Param("userJTFTBonus") UserJTFTBonus userJTFTBonus, Page page);

    @Insert("insert into Users_JTFT_Bonus (userId, netUserId, tradeType, bonusType, amount, fromUserId, inOrOut, remark) values (" +
            "#{userJTFTBonus.userId}, #{userJTFTBonus.netUserId}, #{userJTFTBonus.tradeType}, #{userJTFTBonus.bonusType}, #{userJTFTBonus.amount}, #{userJTFTBonus.fromUserId}, #{userJTFTBonus.inOrOut}, #{userJTFTBonus.remark}" +
            ")")
    int insert(@Param("userJTFTBonus") UserJTFTBonus userJTFTBonus);
}
