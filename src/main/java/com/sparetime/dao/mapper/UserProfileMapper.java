package com.sparetime.dao.mapper;

import com.sparetime.domian.UserProfile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;

public interface UserProfileMapper {

    @Select("select * from Users_Profile where userId = #{userId}")
    UserProfile selectByUserId(@Param("userId") BigDecimal userId);

    @Update("update Users_Profile set " +
            "idCard = #{userProfile.idCard}," +
            "postCode = #{userProfile.postCode}," +
            "address = #{userProfile.address}," +
            "province = #{userProfile.province}," +
            "city = #{userProfile.city}," +
            "birthday = #{userProfile.birthday}," +
            "bankCountry = #{userProfile.bankCountry}," +
            "bankName = #{userProfile.bankName}," +
            "bankCode = #{userProfile.bankCode}," +
            "bankAccountName = #{userProfile.bankAccountName}," +
            "bankAccountNumber = #{userProfile.bankAccountNumber} " +
            "where userId = ${userProfile.userId}")
    int update(@Param("userProfile") UserProfile userProfile);

    @Select("select count(1) from Users_Profile p left join Users u on p.userId = u.userId where p.idCard = #{idCard} and u.regDate >= #{regDate}")
    int countByIdCard(@Param("idCard") String idCard, @Param("regDate")Date regDate);
}
