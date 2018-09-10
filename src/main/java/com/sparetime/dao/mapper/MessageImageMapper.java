package com.sparetime.dao.mapper;

import com.sparetime.domian.MessageImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * Created by muye on 17/11/7.
 */
public interface MessageImageMapper {

    @Insert("insert into Messages_Images (mid, imgPath, cdate) values (#{img.mid}, #{img.imgPath}, #{img.cdate})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", resultType = Long.class, keyColumn = "id", keyProperty = "img.id", before = false)
    int insert(@Param("img")MessageImage img);

    @Select("select * from Messages_Images where id = #{id}")
    MessageImage selectByKey(@Param("id")Long id);
}
