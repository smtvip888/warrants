package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.News;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/26.
 */
public interface NewsMapper {

    @Select("<script>" +
            "select * from News where 1=1" +
            "<if test='news.title != null'>and title = #{news.title}</if>" +
            "<if test='news.summary != null'>and summary = #{news.summary}</if>" +
            "<if test='news.body != null'>and body = #{news.body}</if>" +
            "<if test='news.adminUserId != null'>and adminUserId = #{news.adminUserId}</if>" +
            "<if test='news.isDelete != null'>and isDelete = #{news.isDelete}</if>" +
            "<if test='news.isPop != null'>and isPop = #{news.isPop}</if>" +
            "<if test='news.lang != null'>and lang = #{news.lang}</if>" +
            "order by id desc" +
            "</script>")
    List<News> selectListByExample(@Param("news") News news, Page page);

    @Insert("insert into News (title, summary, body, adminUserId, isPop, lang, cdate) " +
            "values (#{news.title}, #{news.summary}, #{news.body}, #{news.adminUserId}, #{news.isPop}, #{news.lang}, #{news.cdate})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "news.id", resultType = Long.class, before = false)
    int insert(@Param("news")News news);

    @Select("select * from News where id = #{id}")
    News selectByKey(@Param("id") BigDecimal id);

    @Update("update News set " +
            "title = #{news.title}," +
            "summary = #{news.summary}," +
            "body = #{news.body}," +
            "adminUserId = #{news.adminUserId}," +
            "isDelete = #{news.isDelete}," +
            "lang = #{news.lang}," +
            "isPop = #{news.isPop}  " +
            "where id = #{news.id}")
    int update(@Param("news") News news);

    @Delete("delete from News where id = #{id}")
    int delete(@Param("id") BigDecimal id);
}
