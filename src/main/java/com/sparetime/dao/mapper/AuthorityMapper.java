package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Authority;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
public interface AuthorityMapper {

    @Select("select * from authority where id in (select auth_id from manager_auth where manager_id = #{managerId})")
    List<Authority> selectAuthByManagerId(@Param("managerId")Long managerId);

    @Select("<script>" +
            "select * from authority where 1 = 1" +
            "<if test='authority.id != null'> and id = #{authority.id}</if>" +
            "<if test='authority.authName != null'> and auth_name = #{authority.authName}</if>" +
            "<if test='authority.authCode != null'> and auth_code = #{authority.authCode}</if>" +
            "<if test='authority.description != null'> and description = #{authority.description}</if>" +
            "</script>")
    List<Authority> selectListByExample(@Param("authority") Authority authority, Page page);

    @Select("select * from authority where id in (select auth_id from resource_auth where resource_id =#{resourceId})")
    List<Authority> selectListByResourceId(@Param("resourceId") Long resourceId);

    @Select("select * from authority where id = #{id}")
    Authority selectByKey(@Param("id") Long id);

    @Insert("insert into authority (auth_name, auth_code, description,create_time,modify_time) values (#{auth.authName}, #{auth.authCode}, #{auth.description},#{auth.createTime},#{auth.modifyTime})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = Long.class, keyColumn = "id", keyProperty = "auth.id", before = false)
    int insert(@Param("auth") Authority auth);

    @Update("update authority set " +
            "auth_name = #{auth.authName}, " +
            "auth_code = #{auth.authCode}, " +
            "description = #{auth.description}, " +
            "modify_time = #{auth.modifyTime} " +
            "where id = #{auth.id}")
    int update(@Param("auth") Authority auth);

    @Delete("delete from authority where id = #{id}")
    int delete(@Param("id") Long id);

    @Select("select auth_id from resource_auth where resource_id = #{resourceId}")
    List<Long> selectIdByResourceId(@Param("resourceId") Long resourceId);

    @Select("select `write` from resource_auth where resource_id = #{resourceId} and auth_id = #{authId}")
    int isWrite(@Param("resourceId") Long resourceId, @Param("authId") Long authId);
}
