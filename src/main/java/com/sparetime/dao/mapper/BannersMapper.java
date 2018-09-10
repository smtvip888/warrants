package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Banners;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BannersMapper {

    @Select("<script>" +
            "select * from Banners where 1 = 1" +
            "<if test='banners.imgurl != null'>and imgurl = #{banners.imgurl}</if> " +
            "<if test='banners.url != null'>and url = #{banners.url}</if> " +
            "order by sort" +
            "</script>")
    List<Banners> selectListByExample(@Param("banners") Banners banners, Page page);

    @Select("select * from Banners where id = #{id}")
    Banners selectByKey(@Param("id") Long id);

    @Insert("insert into Banners (`desc`, imgurl, url, sort) values (#{banners.desc}, #{banners.imgurl}, #{banners.url}, #{banners.sort})")
    int insert(@Param("banners") Banners banners);

    @Update("update Banners set `desc` = #{banners.desc}, imgurl = #{banners.imgurl}, url = #{banners.url}, sort = #{banners.sort} where id = #{banners.id}")
    int update(@Param("banners") Banners banners);

    @Delete("delete from Banners where id = #{id}")
    int delete(@Param("id") Long id);
}
