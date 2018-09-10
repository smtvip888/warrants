package com.sparetime.dao.mapper;

import com.sparetime.domian.UserAccount;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface UserAccountMapper {

    @Select("select * from Users_Accounts where userId = #{userId}")
    List<UserAccount> selectListByUserId(@Param("userId") BigDecimal userId);

    @Select("select * from Users_Accounts where clientUserId = #{clientUserId}")
    List<UserAccount> selectListByChild(@Param("clientUserId") BigDecimal clientUserId);

    @Insert("insert into Users_Accounts (userId, clientUserId, status, cdate) values (#{userAccount.userId}, #{userAccount.clientUserId}, #{userAccount.status},#{userAccount.cdate})")
    int insert(@Param("userAccount") UserAccount userAccount);

    @Delete("delete from Users_Accounts where id = #{id}")
    int delete(@Param("id") Long id);

    @Select("select * from Users_Accounts where id = #{id}")
    UserAccount selectByKey(@Param("id") Long id);
}
