package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Manager;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 17/7/18.
 */
public interface ManagerMapper {

    @Select("select * from manager where name = #{name}")
    Manager selectByName(@Param("name")String name);

    @Select("<script>" +
            "select * from manager where 1 = 1" +
            "<if test='manager.name != null'> and name = #{manager.name}</if>" +
            "<if test='manager.password != null'> and password = #{manager.password}</if>" +
            "</script>")
    List<Manager> selectByExample(@Param("manager") Manager manager, Page page);

    @Insert("insert into manager (name, password, create_time, modify_time) values (#{manager.name}, #{manager.password},#{manager.createTime},#{manager.modifyTime})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "manager.id", resultType = Long.class, before = false)
    int insert(@Param("manager") Manager manager);

    @Insert("insert into manager_auth (manager_id, auth_id) values (#{managerId}, #{authId})")
    int insertAuth(@Param("managerId") Long managerId, @Param("authId") Long authId);

    @Select("select count(1) from manager where name = #{name}")
    int countByName(@Param("name") String name);

    @Update("<script>" +
            "update manager set name = #{manager.name} " +
            "<if test='manager.password != null'>, password = #{manager.password}</if> " +
            "<if test='manager.modifyTime != null'>, modify_time = #{manager.modifyTime}</if> " +
            "where id = #{manager.id}" +
            "</script>")
    int update(@Param("manager") Manager manager);

    @Delete("delete from manager_auth where manager_id = #{managerId}")
    int deleteAuth(@Param("managerId") Long managerId);

    @Delete("delete from manager where id = #{id}")
    int delete(@Param("id") Long id);

    @Select("select * from manager where id = #{id}")
    Manager selectByKey(@Param("id") Long id);
}