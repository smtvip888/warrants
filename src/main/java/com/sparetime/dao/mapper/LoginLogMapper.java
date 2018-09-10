package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by muye on 17/8/21.
 */
public interface LoginLogMapper {

    @Insert("insert into login_log (user_id, type, ip, login_time) values (#{loginLog.userId}, #{loginLog.type}, #{loginLog.ip}, #{loginLog.loginTime})")
    int insert(@Param("loginLog") LoginLog loginLog);

    @Select("<script>" +
            "select * from login_log where 1=1 " +
            "<if test='loginLog.userId != null'>and user_id = #{loginLog.userId}</if>" +
            "<if test='loginLog.type != null'>and type = #{loginLog.type}</if>" +
            "<if test='loginLog.ip != null'>and ip = #{loginLog.ip}</if>" +
            "order by login_time desc" +
            "</script>")
    List<LoginLog> selectListByExample(@Param("loginLog") LoginLog loginLog, Page page);
}
