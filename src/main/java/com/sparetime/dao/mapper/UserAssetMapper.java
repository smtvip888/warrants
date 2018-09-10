package com.sparetime.dao.mapper;

import com.sparetime.domian.UserAsset;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface UserAssetMapper {

    @Select("select * from Users_Asset where userId = #{userId}")
    UserAsset selectByUserId(@Param("userId") BigDecimal userId);

    @Insert("<script>" +
            "insert into Users_Asset (" +
            "userId, teamNum, teamTurnover, leftTurnoverSurplus, rightTurnoverSurplus, leftTotalTurnover, rightTotalTurnover, " +
            "totalBonus, bonusSurplus, sharesIntegral, DTFTJJ, DTFTJJYE, JTFTJJ, JTFTJJYE, LYJF, LYJFYE, SCJF, SCJFYE, ZCJF, ZCJFYYE" +
            ") " +
            "values (" +
            "#{userAsset.userId},#{userAsset.teamNum},#{userAsset.teamTurnover},#{userAsset.leftTurnoverSurplus}," +
            "#{userAsset.rightTurnoverSurplus},#{userAsset.leftTotalTurnover},#{userAsset.rightTotalTurnover},#{userAsset.totalBonus}," +
            "#{userAsset.bonusSurplus},#{userAsset.sharesIntegral},#{userAsset.DTFTJJ},#{userAsset.DTFTJJYE}, #{userAsset.JTFTJJ}," +
            "#{userAsset.JTFTJJYE}, #{userAsset.LYJF}, #{userAsset.LYJFYE}, #{userAsset.SCJF}, #{userAsset.SCJFYE}, #{userAsset.ZCJF}, #{userAsset.ZCJFYYE}" +
            ")" +
            "</script>")
    int insert(@Param("userAsset") UserAsset userAsset);

    @Insert("insert into Users_Asset (userId, JTFTJJYE) values (#{userId}, #{JTFTJJYE})")
    int insertUserId(@Param("userId") BigDecimal userId, @Param("JTFTJJYE") BigDecimal JTFTJJYE);

    @Update("update Users_Asset set " +
            "teamNum = #{userAsset.teamNum}," +
            "teamTurnover = #{userAsset.teamTurnover}," +
            "leftTurnoverSurplus = #{userAsset.leftTurnoverSurplus}," +
            "rightTurnoverSurplus = #{userAsset.rightTurnoverSurplus}," +
            "leftTotalTurnover = #{userAsset.leftTotalTurnover}," +
            "rightTotalTurnover = #{userAsset.rightTotalTurnover}," +
            "bonusSurplus = #{userAsset.bonusSurplus}," +
            "sharesIntegral = #{userAsset.sharesIntegral}," +
            "DTFTJJ = #{userAsset.DTFTJJ}," +
            "DTFTJJYE = #{userAsset.DTFTJJYE}," +
            "JTFTJJ = #{userAsset.JTFTJJ}," +
            "JTFTJJYE = #{userAsset.JTFTJJYE}," +
            "LYJF = #{userAsset.LYJF}," +
            "LYJFYE = #{userAsset.LYJFYE}," +
            "SCJF = #{userAsset.SCJF}," +
            "SCJFYE = #{userAsset.SCJFYE}," +
            "ZCJF = #{userAsset.ZCJF}," +
            "ZCJFYYE = #{userAsset.ZCJFYYE}" +
            "where userId = #{userAsset.userId}" +
            "")
    int update(@Param("userAsset") UserAsset userAsset);
}
