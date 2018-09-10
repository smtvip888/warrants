package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.User;
import com.sparetime.domian.UserProfile;
import com.sparetime.domian.extend.UserGuide;
import com.sparetime.domian.extend.UserTree;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserService {

    List<User> queryListByExample(User user, Page page);

    User queryByKey(BigDecimal id);

    void updateSelective(User user, UserProfile profile);

    BigDecimal getUserIdByUserSN(String userSN);

    BigDecimal addUser(User user, BigDecimal productId, BigDecimal loginUserId, String bankAccountName, String idCard, BigDecimal ZCJFYYE, BigDecimal DTFTJJYE);

    boolean existByParentUserIdAndPlaceArea(BigDecimal parentUserId, int placeArea);

    User queryByName(String name);

    UserTree getParentUser(User user, int level);

    void upgrade(BigDecimal userId, BigDecimal productId);

    void changeProfile(User user, UserProfile userProfile, String ip);

    void changePassword(String password, BigDecimal userId, String ip);

    void changePassword1(String password1, BigDecimal userId, String ip);

    void active(BigDecimal userId, BigDecimal productId);

    long countRegInfo(Date startTime, Date endTime, String name);

    List<UserGuide> queryGuideList(UserGuide query, Page page);

    void guide(Date startTime, Date endTime, Integer productId, BigDecimal num);

    User queryBySubId(BigDecimal subId);

    Long queryMaxSubId();
}
