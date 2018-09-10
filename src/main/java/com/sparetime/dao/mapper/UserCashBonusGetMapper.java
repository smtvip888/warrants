package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserCashBonusGet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by muye on 17/7/24.
 */
public interface UserCashBonusGetMapper {

    @Select("<script>" +
            "select * from Users_Cash_Bonus_Get where 1=1" +
            "<if test='userCashBonusGet.userId != null'>and userId = #{userCashBonusGet.userId}</if>" +
            "<if test='userCashBonusGet.amount != null'>and amount = #{userCashBonusGet.amount}</if>" +
            "<if test='userCashBonusGet.bankName != null'>and bankName = #{userCashBonusGet.bankName}</if>" +
            "<if test='userCashBonusGet.bankCode != null'>and bankCode = #{userCashBonusGet.bankCode}</if>" +
            "<if test='userCashBonusGet.bankAddress != null'>and bankAddress = #{userCashBonusGet.bankAddress}</if>" +
            "<if test='userCashBonusGet.accountName != null'>and accountName = #{userCashBonusGet.accountName}</if>" +
            "<if test='userCashBonusGet.accountNumber != null'>and accountNumber = #{userCashBonusGet.accountNumber}</if>" +
            "<if test='userCashBonusGet.status != null'>and status = #{userCashBonusGet.status}</if>" +
            "<if test='userCashBonusGet.remark != null'>and remark = #{userCashBonusGet.remark}</if>" +
            "<if test='userCashBonusGet.adminUserId != null'>and adminUserId = #{userCashBonusGet.adminUserId}</if>" +
            "</script>")
    List<UserCashBonusGet> selectListByExample(@Param("userCashBonusGet") UserCashBonusGet userCashBonusGet, Page page);

    @Update("<script>" +
            "update Users_Cash_Bonus_Get" +
            "<set>" +
            "<if test='userCashBonusGet.userId != null'>userId = #{userCashBonusGet.userId},</if>" +
            "<if test='userCashBonusGet.amount != null'>amount = #{userCashBonusGet.amount},</if>" +
            "<if test='userCashBonusGet.bankName != null'>bankName = #{userCashBonusGet.bankName},</if>" +
            "<if test='userCashBonusGet.bankCode != null'>bankCode = #{userCashBonusGet.bankCode},</if>" +
            "<if test='userCashBonusGet.bankAddress != null'>bankAddress = #{userCashBonusGet.bankAddress},</if>" +
            "<if test='userCashBonusGet.accountName != null'>accountName = #{userCashBonusGet.accountName},</if>" +
            "<if test='userCashBonusGet.accountNumber != null'>accountNumber = #{userCashBonusGet.accountNumber},</if>" +
            "<if test='userCashBonusGet.status != null'>status = #{userCashBonusGet.status},</if>" +
            "<if test='userCashBonusGet.remark != null'>remark = #{userCashBonusGet.remark},</if>" +
            "<if test='userCashBonusGet.adminUserId != null'>adminUserId = #{userCashBonusGet.adminUserId},</if>" +
            "</set>" +
            "where id = #{userCashBonusGet.id}" +
            "</script>")
    int updateSelective(@Param("userCashBonusGet") UserCashBonusGet userCashBonusGet);

    @Insert("insert into Users_Cash_Bonus_Get (userId,amount,bankName,bankCode,bankAddress,accountName,accountNumber,status,remark,adminUserId,payDate) values (" +
            "#{userCashBonusGet.userId},#{userCashBonusGet.amount},#{userCashBonusGet.bankName},#{userCashBonusGet.bankCode}," +
            "#{userCashBonusGet.bankAddress},#{userCashBonusGet.accountName},#{userCashBonusGet.accountNumber},#{userCashBonusGet.status}," +
            "#{userCashBonusGet.remark},#{userCashBonusGet.adminUserId},#{userCashBonusGet.payDate}" +
            ")")
    int insert(@Param("userCashBonusGet") UserCashBonusGet userCashBonusGet);
}
