package com.sparetime.service.impl;

import com.sparetime.dao.mapper.UserAccountMapper;
import com.sparetime.domian.Log;
import com.sparetime.domian.UserAccount;
import com.sparetime.domian.UserProfile;
import com.sparetime.service.LogService;
import com.sparetime.service.UserAccountService;
import com.sparetime.service.UserProfileService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    @Override
    public List<UserAccount> queryListByUserId(BigDecimal userId) {
        return userAccountMapper.selectListByUserId(userId);
    }

    @Override
    public List<UserAccount> queryListByChild(BigDecimal child) {
        return userAccountMapper.selectListByChild(child);
    }

    @Override
    @Transactional
    public int add(UserAccount userAccount) {

        UserProfile userProfile = userProfileService.queryByUserId(userAccount.getUserId());
        UserProfile childProfile = userProfileService.queryByUserId(userAccount.getClientUserId());

        List<UserAccount> list = userAccountMapper.selectListByUserId(userAccount.getUserId());
        list.forEach(v ->{
            if (v.getClientUserId().equals(userAccount.getClientUserId())){
                throw new RuntimeException("该用户已经添加过了");
            }
        });

        if (userAccount.getUserId().equals(userAccount.getClientUserId()))
            throw new RuntimeException("不能添加自己");

        if (userProfile.getIdCard() == null){
            throw new RuntimeException("你没有填写身份证信息");
        }
        if (!userProfile.getIdCard().equals(childProfile.getIdCard())){
            throw new RuntimeException("子用户的身份证信息与你不一致");
        }
        userAccount.setCdate(new Date());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        Log log = new Log();
        log.setUserType(1);
        log.setLogType(21);
        log.setIp(ip);
        log.setCdate(new Date());
        log.setUserName((String) request.getSession().getAttribute("userName"));
        log.setRemark("新增子账户 Id: " + userAccount.getClientUserId() + " | user Name: " + userService.queryByKey(userAccount.getClientUserId()).getUserName());

        logService.insert(log);
        return userAccountMapper.insert(userAccount);
    }

    @Override
    @Transactional
    public int delete(Long id) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");

        UserAccount userAccount = userAccountMapper.selectByKey(id);

        Log log = new Log();
        log.setUserType(1);
        log.setLogType(21);
        log.setIp(ip);
        log.setUserName((String) request.getSession().getAttribute("userName"));
        log.setRemark("删除子账户 Id：" + userAccount.getClientUserId() + " | user name: " + userService.queryByKey(userAccount.getClientUserId()).getUserName());
        log.setCdate(new Date());

        logService.insert(log);

        return userAccountMapper.delete(id);
    }
}
