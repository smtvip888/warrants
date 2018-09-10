package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.SysConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by muye on 17/8/28.
 */
public interface SysConfigMapper {

    @Select("select * from sys_config where `key` = #{key}")
    SysConfig selectByKey(@Param("key")String key);

    @Select("<script>" +
            "select * from sys_config where 1=1 " +
            "<if test='config.key != null'>and `key` = #{config.key}</if>" +
            "<if test='config.value != null'>and `value` = #{config.value}</if>" +
            "</script>")
    List<SysConfig> selectListByExample(@Param("config") SysConfig config, Page page);

    @Update("update sys_config set `value` = #{value} where `key` = #{key}")
    int updateValue(@Param("value") String value, @Param("key") String key);

    @Insert("insert into sys_config (`key`, `value`, remark) values (#{config.key},#{config.value},#{config.remark})")
    int insert(@Param("config") SysConfig config);
}
