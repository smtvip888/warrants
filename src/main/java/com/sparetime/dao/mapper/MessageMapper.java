package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Message;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/27.
 */
public interface MessageMapper {

    @Select("<script>" +
            "select * from Messages where 1=1" +
            "<if test='message.msgId != null'>and msgId = #{message.msgId}</if>" +
            "<if test='message.toUserId != null'>and toUserId = #{message.toUserId}</if>" +
            "<if test='message.title != null'>and title = #{message.title}</if>" +
            "<if test='message.type != null'>and type = #{message.type}</if>" +
            "<if test='message.sendUserId != null'>and sendUserId = #{message.sendUserId}</if>" +
            "<if test='message.adminUserId != null'>and adminUserId = #{message.adminUserId}</if>" +
            "<if test='message.isDelete != null'>and isDelete = #{message.isDelete}</if>" +
            "<if test='message.isRead != null'>and isRead = #{message.isRead}</if>" +
            "order by isRead, id desc" +
            "</script>")
    List<Message> selectListByExample(@Param("message") Message message, Page page);

    @Insert("insert into Messages (msgId,toUserId,type,sendUserId,adminUserId,title,body, ip, cdate) values (" +
            "#{message.msgId},#{message.toUserId},#{message.type},#{message.sendUserId},#{message.adminUserId}," +
            "#{message.title},#{message.body},#{message.ip},#{message.cdate})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = Long.class, keyColumn = "id", keyProperty = "message.id", before = false)
    int insert(@Param("message") Message message);

    @Select("select * from Messages where id = #{id}")
    Message selectByKey(@Param("id")BigDecimal id);

    @Update("<script>" +
            "update Messages" +
            "<set>" +
            "<if test = 'message.msgId != null'> MsgId = #{message.msgId},</if>" +
            "<if test = 'message.toUserId != null'> toUserId = #{message.toUserId},</if>" +
            "<if test = 'message.title != null'> title = #{message.title},</if>" +
            "<if test = 'message.body != null'> body = #{message.body},</if>" +
            "<if test = 'message.type != null'> type = #{message.type},</if>" +
            "<if test = 'message.sendUserId != null'> sendUserId = #{message.sendUserId},</if>" +
            "<if test = 'message.adminUserId != null'> adminUserId = #{message.adminUserId},</if>" +
            "<if test = 'message.isDelete != null'> isDelete = #{message.isDelete},</if>" +
            "<if test = 'message.isRead != null'> isRead = #{message.isRead},</if>" +
            "</set>" +
            "where id = #{message.id}" +
            "</script>")
    int updateSelective(@Param("message") Message message);

    @Select("select * from Messages where msgId = #{msgId} order by id")
    List<Message> selectListByMsgId(@Param("msgId") Long msgId);
}
