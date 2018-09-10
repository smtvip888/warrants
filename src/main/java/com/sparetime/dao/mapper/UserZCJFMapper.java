package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserZCJF;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserZCJFMapper {

    @Select("select * from Users_ZCJF where id = #{id}")
    UserZCJF selectByKey(@Param("id") Long id);

    @Select("<script>" +
            "select * from Users_ZCJF where 1=1" +
            "<if test='userZCJF.userId != null'>and userId = #{userZCJF.userId}</if>" +
            "<if test='userZCJF.netUserId != null'>and netUserId = #{userZCJF.netUserId}</if>" +
            "<if test='userZCJF.amount != null'>and amount = #{userZCJF.amount}</if>" +
            "<if test='userZCJF.fromUserId != null'>and fromUserId = #{userZCJF.fromUserId}</if>" +
            "<if test='userZCJF.inOrOut != null'>and inOrOut = #{userZCJF.inOrOut}</if>" +
            "<if test='userZCJF.isdel != null'>and isdel = #{userZCJF.isdel}</if>" +
            "<if test='userZCJF.remark != null'>and remark = #{userZCJF.remark}</if>" +
            "<if test='userZCJF.startTime != null'><![CDATA[and cdate >= #{userZCJF.startTime}]]></if>" +
            "<if test='userZCJF.endTime != null'><![CDATA[and cdate <= #{userZCJF.endTime}]]></if>" +
            "order by id desc" +
            "</script>")
    List<UserZCJF> selectListByExample(@Param("userZCJF") UserZCJF userZCJF, Page page);

    @Insert("insert into Users_ZCJF (userId, netUserId, amount, fromUserId, inOrOut, isdel, remark, ip, cdate) values (" +
            "#{userZCJF.userId}, #{userZCJF.netUserId},#{userZCJF.amount},#{userZCJF.fromUserId},#{userZCJF.inOrOut}," +
            "#{userZCJF.isdel},#{userZCJF.remark}, #{userZCJF.ip},#{userZCJF.cdate}" +
            ")")
    int insert(@Param("userZCJF") UserZCJF userZCJF);

    @Update("update Users_ZCJF set isdel = #{isdel} where id = #{id}")
    int updateIsdel(@Param("isdel") int isdel, @Param("id") Long id);
}
