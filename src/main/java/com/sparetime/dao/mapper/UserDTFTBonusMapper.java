package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserDTFTBonus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserDTFTBonusMapper {

    @Select("<script>" +
            "select * from Users_DTFT_Bonus where 1=1" +
            "<if test='userDTFTBonus.userId != null'>and userId = #{userDTFTBonus.userId}</if>" +
            "<if test='userDTFTBonus.netUserId != null'>and netUserId = #{userDTFTBonus.netUserId}</if>" +
            "<if test='userDTFTBonus.tradeType != null'>and tradeType = #{userDTFTBonus.tradeType}</if>" +
            "<if test='userDTFTBonus.bonusType != null'>and bonusType = #{userDTFTBonus.bonusType}</if>" +
            "<if test='userDTFTBonus.amount != null'>and amount = #{userDTFTBonus.amount}</if>" +
            "<if test='userDTFTBonus.fromUserId != null'>and fromUserId = #{userDTFTBonus.fromUserId}</if>" +
            "<if test='userDTFTBonus.inOrOut != null'>and inOrOut = #{userDTFTBonus.inOrOut}</if>" +
            "<if test='userDTFTBonus.remark != null'>and remark = #{userDTFTBonus.remark}</if>" +
            "<if test='userDTFTBonus.startTime != null'><![CDATA[and cdate >= #{userDTFTBonus.startTime}]]></if>" +
            "<if test='userDTFTBonus.endTime != null'><![CDATA[and cdate <= #{userDTFTBonus.endTime}]]></if>" +
            "</script>")
    List<UserDTFTBonus> selectListByExample(@Param("userDTFTBonus") UserDTFTBonus userDTFTBonus, Page page);

    @Insert("insert into Users_DTFT_Bonus (userId, netUserId, tradeType, bonusType, amount, fromUserId, inOrOut, remark) values (" +
            "#{userDTFTBonus.userId}, #{userDTFTBonus.netUserId}, #{userDTFTBonus.tradeType}, #{userDTFTBonus.bonusType}, #{userDTFTBonus.amount}, #{userDTFTBonus.fromUserId}, #{userDTFTBonus.inOrOut}, #{userDTFTBonus.remark}" +
            ")")
    int insert(@Param("userDTFTBonus") UserDTFTBonus userDTFTBonus);
}
