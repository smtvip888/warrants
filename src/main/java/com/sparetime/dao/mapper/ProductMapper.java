package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Product;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
public interface ProductMapper {

    @Select("select * from Products where is_show = 0")
    List<Product> selectAll();

    @Select("select * from Products where productId = #{productId}")
    Product selectByKey(@Param("productId") BigDecimal productId);

    @Select("<script>" +
            "select * from Products where 1=1" +
            "<if test='product.invested != null'>and invested = #{product.invested}</if>" +
            "<if test='product.name != null'>and name = #{product.name}</if>" +
            "<if test='product.TJJ != null'>and TJJ = #{product.TJJ}</if>" +
            "<if test='product.DPJ != null'>and DPJ = #{product.DPJ}</if>" +
            "<if test='product.FDJ1 != null'>and FDJ1 = #{product.FDJ1}</if>" +
            "<if test='product.FDJ2 != null'>and FDJ2 = #{product.FDJ2}</if>" +
            "<if test='product.FDJ3 != null'>and FDJ3 = #{product.FDJ3}</if>" +
            "<if test='product.FDJ4 != null'>and FDJ4 = #{product.FDJ4}</if>" +
            "<if test='product.FDJ5 != null'>and FDJ5 = #{product.FDJ5}</if>" +
            "<if test='product.JDJCS != null'>and JDJCS = #{product.JDJCS}</if>" +
            "<if test='product.CJBS != null'>and CJBS = #{product.CJBS}</if>" +
            "<if test='product.KCGS != null'>and KCGS = #{product.KCGS}</if>" +
            "<if test='product.KMGS != null'>and KMGS = #{product.KMGS}</if>" +
            "<if test='product.PGBL != null'>and PGBL = #{product.PGBL}</if>" +
            "</script>")
    List<Product> selectListByExample(@Param("product") Product product, Page page);

    @Insert("insert into Products (invested, TJJ, DPJ, FDJ1, FDJ2, FDJ3, FDJ4, FDJ5, JDJCS, CJBS, KCGS, KMGS, PGBL, level, name, is_show) values (" +
            "#{product.invested}, #{product.TJJ}, #{product.DPJ}, #{product.FDJ1}, #{product.FDJ2}, #{product.FDJ3}, #{product.FDJ4}, #{product.FDJ5}," +
            "#{product.JDJCS}, #{product.CJBS}, #{product.KCGS}, #{product.KMGS}, #{product.PGBL}, #{product.level}, #{product.name}, #{product.isShow})")
    int insert(@Param("product") Product product);

    @Update("update Products set " +
            "invested = #{product.invested}," +
            "TJJ = #{product.TJJ}," +
            "DPJ = #{product.DPJ}," +
            "FDJ1 = #{product.FDJ1}," +
            "FDJ2 = #{product.FDJ2}," +
            "FDJ3 = #{product.FDJ3}," +
            "FDJ4 = #{product.FDJ4}," +
            "FDJ5 = #{product.FDJ5}," +
            "JDJCS = #{product.JDJCS}," +
            "CJBS = #{product.CJBS}," +
            "KCGS = #{product.KCGS}," +
            "KMGS = #{product.KMGS}," +
            "PGBL = #{product.PGBL}," +
            "level = #{product.level}," +
            "name = #{product.name}," +
            "is_show = #{product.isShow} " +
            "where productId = #{product.productId}")
    int update(@Param("product") Product product);

    @Delete("delete from Products where productId = #{productId}")
    int delete(@Param("productId") BigDecimal productId);
}
