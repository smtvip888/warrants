package com.sparetime.dao.mapper;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.User;
import com.sparetime.domian.extend.UserGuide;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserMapper {

    @Select("<script>" +
            "select * from Users where 1 = 1 " +
            "<if test='user.userId != null'>and userId = #{user.userId}</if>" +
            "<if test='user.recommendUserId != null'>and recommendUserId = #{user.recommendUserId}</if>" +
            "<if test='user.parentUserId != null'>and parentUserId = #{user.parentUserId}</if>" +
            "<if test='user.placeArea != null'>and placeArea = #{user.placeArea}</if>" +
            "<if test='user.userSN != null'>and userSN = #{user.userSN}</if>" +
            "<if test='user.userName != null'>and userName = #{user.userName}</if>" +
            "<if test='user.regCountry != null'>and regCountry = #{user.regCountry}</if>" +
            "<if test='user.password != null'>and password = #{user.password}</if>" +
            "<if test='user.password1 != null'>and password1 = #{user.password1}</if>" +
            "<if test='user.mobile != null'>and mobile = #{user.mobile}</if>" +
            "<if test='user.email != null'>and email = #{user.email}</if>" +
            "<if test='user.status != null'>and status = #{user.status}</if>" +
            "</script>")
    List<User> selectByExample(@Param("user") User user, Page page);

    @Select("select * from Users where userId = #{id}")
    User selectByKey(@Param("id") BigDecimal id);

    @Update("<script>" +
            "update Users " +
            "<set>" +
            "<if test='user.recommendUserId != null'>recommendUserId = #{user.recommendUserId},</if>" +
            "<if test='user.parentUserId != null'>parentUserId = #{user.parentUserId},</if>" +
            "<if test='user.placeArea != null'>placeArea = #{user.placeArea},</if>" +
            "<if test='user.userSN != null'>userSN = #{user.userSN},</if>" +
            "<if test='user.userName != null'>userName = #{user.userName},</if>" +
            "<if test='user.regCountry != null'>regCountry = #{user.regCountry},</if>" +
            "<if test='user.password != null'>password = #{user.password},</if>" +
            "<if test='user.password1 != null'>password1 = #{user.password1},</if>" +
            "<if test='user.mobile != null'>mobile = #{user.mobile},</if>" +
            "<if test='user.email != null'>email = #{user.email},</if>" +
            "<if test='user.status != null'>status = #{user.status},</if>" +
            "<if test='user.upgraded != null'>upgraded = #{user.upgraded},</if>" +
            "<if test='user.isService != null'>isService = #{user.isService},</if>" +
            "</set>" +
            "where userId = #{user.userId}" +
            "</script>")
    int updateSelective(@Param("user") User user);

    @Insert("insert into Users (" +
            "recommendUserId, parentUserId, placeArea, userSN, userName, regCountry, password, password1, mobile, email, status, ip, regDate) " +
            "values (" +
            "#{user.recommendUserId}, #{user.parentUserId}, #{user.placeArea}, #{user.userSN}, #{user.userName}, #{user.regCountry}, " +
            "#{user.password}, #{user.password1}, #{user.mobile}, #{user.email}, #{user.status}, #{user.ip}, #{user.regDate})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "userId", keyProperty = "user.userId", resultType = BigDecimal.class, before = false)
    int insert(@Param("user") User user);

    @Select("select count(1) from Users where parentUserId = #{parentUserId} and placeArea = #{placeArea}")
    int countByParentUserIdAndPlaceArea(@Param("parentUserId") BigDecimal parentUserId, @Param("placeArea") int placeArea);

    @Select("select * from Users where userName = #{userName}")
    User selectByName(@Param("userName") String userName);

    @Update("update Users set mobile = #{mobile}, email = #{email} where userId = #{userId}")
    int updateMobileAndEmail(@Param("mobile")String mobile, @Param("email") String email, @Param("userId") BigDecimal userId);

    @Update("update Users set password = #{password} where userId = #{userId}")
    int changePassword(@Param("password") String password, @Param("userId") BigDecimal userId);

    @Update("update Users set password1 = #{password1} where userId = #{userId}")
    int changePassword1(@Param("password1")String password1, @Param("userId") BigDecimal userId);

    @Select("<script>" +
            "select count(1) from (" +
            "   select u.userId, u.RegDate, n.NetUserId, n.ProductId, p.`name` from Users u " +
            "   left join Net_Users n on u.UserId = n.UserId " +
            "   left join Products p on n.ProductId = p.ProductId) t " +
            "where 1=1" +
            "<if test='startTime != null'><![CDATA[and t.regDate >= #{startTime}]]></if>" +
            "<if test='endTime != null'><![CDATA[and t.regDate <= #{endTime}]]></if>" +
            "<if test='name != null'>and t.name = #{name}</if>" +
            "</script>")
    long countRegInfo(@Param("startTime")Date startTime, @Param("endTime")Date endTime, @Param("name")String name);

    @Select("<script>" +
            "select u.userId, u.userName, u.regDate, p.productId, p.`name` as productName, s.num from Users u " +
            "left join (select userId, sum(shares - outShares) as num from Shares group by userId) s on u.userId = s.userId  " +
            "left join Net_Users n on u.userId = n.userId " +
            "left join Products p on p.productId = n.productId " +
            "where 1 =1 " +
            "<if test='guide.startTime != null'><![CDATA[ and u.regDate >= #{guide.startTime}]]></if>" +
            "<if test='guide.endTime != null'><![CDATA[ and u.regDate <= #{guide.endTime}]]></if>" +
            "<if test='guide.productName != null'>and p.name = #{guide.productName}</if>" +
            "<if test='guide.num != null'><![CDATA[ and s.num >= #{guide.num} ]]></if>" +
            "</script>")
    List<UserGuide> selectGuideList(@Param("guide") UserGuide guide, Page page);

    @Select("select * from Users where subId = #{subId}")
    User selectBySubId(@Param("subId") BigDecimal subId);

    @Select("select max(subId) from Users")
    Long selectMaxSubId();
}
