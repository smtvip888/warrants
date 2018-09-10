package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/10/13.
 */
public interface LogMapper {

    @Insert("insert into Logs (logType, userType, userName, remark, ip, cdate) values (" +
            "#{log.logType}," +
            "#{log.userType}," +
            "#{log.userName}," +
            "#{log.remark}," +
            "#{log.ip}," +
            "#{log.cdate}" +
            ")")
    int insert(@Param("log")Log log);

    @Select("<script>" +
            "select * from Logs where 1 = 1 " +
            "<if test='log.userName != null'>and userName = #{log.userName}</if>" +
            "<if test='log.userType != null'>and userType = #{log.userType}</if>" +
            "<if test='log.logType != null'>and logType = #{log.logType}</if>" +
            "order by id desc" +
            "</script>")
    List<Log> selectListByExample(@Param("log") Log log, Page page);
}
