package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserBonus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/8/15.
 */
public interface UserBonusMapper {

    @Select("<script>" +
            "select * from Users_Bonus where 1 = 1" +
            "<if test='userBonus.userId != null'>and userId = #{userBonus.userId}</if>" +
            "<if test='userBonus.netUserId != null'>and netUserId = #{userBonus.netUserId}</if>" +
            "<if test='userBonus.fromUserId != null'>and fromUserId = #{userBonus.fromUserId}</if>" +
            "<if test='userBonus.tradeType != null'>and tradeType = #{userBonus.tradeType}</if>" +
            "<if test='userBonus.bonusType != null'>and bonusType = #{userBonus.bonusType}</if>" +
            "<if test='userBonus.inOrOut != null'>and inOrOut = #{userBonus.inOrOut}</if>" +
            "<if test='userBonus.startTime != null'><![CDATA[and cdate >= #{userBonus.startTime}]]></if>" +
            "<if test='userBonus.endTime != null'><![CDATA[and cdate <= #{userBonus.endTime}]]></if>" +
            "order by id desc" +
            "</script>")
    List<UserBonus> selectListByExample(@Param("userBonus")UserBonus userBonus, Page page);

    @Insert("insert into Users_Bonus (userId,netUserId,fromUserId,tradeType,bonusType,inOrOut,amount," +
            "cashBonus,DTFTBonus,LYJFBonus,JTFTBonus,SCJFBonus,JYFBonus,remark,ip,cdate) values (" +
            "#{userBonus.userId},#{userBonus.netUserId},#{userBonus.fromUserId},#{userBonus.tradeType}," +
            "#{userBonus.bonusType},#{userBonus.inOrOut},#{userBonus.amount},#{userBonus.cashBonus}," +
            "#{userBonus.DTFTBonus},#{userBonus.LYJFBonus},#{userBonus.JTFTBonus},#{userBonus.SCJFBonus}," +
            "#{userBonus.JYFBonus},#{userBonus.remark},#{userBonus.ip},#{userBonus.cdate})")
    int insert(@Param("userBonus") UserBonus userBonus);
}
