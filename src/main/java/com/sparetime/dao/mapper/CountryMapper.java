package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Country;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface CountryMapper {

    @Select("<script>" +
            "select * from Country where 1 = 1 " +
            "<if test='country.isEnable != null'>and isEnable = #{country.isEnable}</if>" +
            "order by sort" +
            "</script>")
    List<Country> selectListByExample(@Param("country") Country country, Page page);

    @Select("select * from Country where countryId = #{countryId}")
    Country selectByKey(@Param("countryId")BigDecimal countryId);

    @Insert("insert into Country (countryName,countryCode,currencyName,exchangeRate,isEnable,sort) values (" +
            "#{country.countryName}," +
            "#{country.countryCode}," +
            "#{country.currencyName}," +
            "#{country.exchangeRate}," +
            "#{country.isEnable}," +
            "#{country.sort}" +
            ")")
    int insert(@Param("country") Country country);

    @Update("update Country set " +
            "countryName = #{country.countryName}," +
            "countryCode = #{country.countryCode}," +
            "currencyName = #{country.currencyName}," +
            "exchangeRate = #{country.exchangeRate}," +
            "isEnable = #{country.isEnable}," +
            "sort = #{country.sort} " +
            "where countryId = #{country.countryId}")
    int update(@Param("country") Country country);

    @Delete("delete from Country where countryId = #{id}")
    int delete(@Param("id") Long id);
}
