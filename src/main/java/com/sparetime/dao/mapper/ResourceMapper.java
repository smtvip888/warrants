package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Resource;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
public interface ResourceMapper {

    @Select("<script>" +
            "select * from resource where pid = #{pid}" +
            "<if test='authList != null'> and id in (" +
            "   select resource_id from resource_auth where auth_id in " +
            "   <foreach item='item' index='index' collection='authList' open='(' separator=',' close=')'>" +
            "   #{item}" +
            "   </foreach>" +
            ")" +
            "</if>" +
            "</script>")
    List<Resource> selectListByPidAndAuths(@Param("pid")Long pid, @Param("authList")List<Long> authList);

    @Select("select * from resource where pid = (select min(pid) from resource)")
    Resource selectRoot();

    @Select("<script>" +
            "select * from resource where 1=1 " +
            "<if test='resource.pid != null'>and pid = #{resource.pid}</if>" +
            "<if test='resource.uri != null'>and uri = #{resource.uri}</if>" +
            "<if test='resource.icon != null'>and  icon = #{resource.icon}</if>" +
            "<if test='resource.name != null'>and name = #{resource.name}</if>" +
            "<if test='resource.description != null'>and description = #{resource.description}</if>" +
            "</script>")
    List<Resource> selectListByExample(@Param("resource")Resource resource, Page page);

    @Select("select * from resource where id = #{id}")
    Resource selectByKey(@Param("id") Long id);

    @Insert("insert into resource (pid, uri, icon, name, description,create_time,modify_time) " +
            "values (#{resource.pid}, #{resource.uri}, #{resource.icon}, #{resource.name},#{resource.description}, #{resource.createTime},#{resource.modifyTime})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = Long.class, keyColumn = "id", keyProperty = "resource.id", before = false)
    int insert(@Param("resource") Resource resource);

    @Insert("insert into resource_auth (resource_id, auth_id, `write`) values (#{resourceId}, #{authId}, #{write})")
    int insertAuth(@Param("resourceId") Long resourceId, @Param("authId") Long authId, @Param("write") int write);

    @Update("update resource set " +
            "pid = #{resource.pid}," +
            "uri = #{resource.uri}," +
            "icon = #{resource.icon}," +
            "name = #{resource.name}," +
            "description = #{resource.description}, " +
            "modify_time = #{resource.modifyTime} " +
            "where id = #{resource.id}")
    int update(@Param("resource") Resource resource);

    @Delete("delete from resource where id = #{id}")
    int delete(@Param("id") Long id);

    @Delete("delete from resource_auth where resource_id = #{resourceId}")
    int deleteAuth(@Param("resourceId") Long resourceId);

    @Select("select * from resource where uri like concat(#{url},'%')")
    Resource selectByLikeUrl(@Param("url") String url);
}
