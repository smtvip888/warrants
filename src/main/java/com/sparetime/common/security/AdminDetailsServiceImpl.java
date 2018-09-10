package com.sparetime.common.security;

import com.sparetime.domian.Authority;
import com.sparetime.domian.Manager;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by muye on 17/6/12.
 */
@Component
public class AdminDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Manager manager = managerService.queryManagerByName(s);
        if (manager == null){
            request.getSession().setAttribute("filed", "用户名或密码不正确");
            throw new UsernameNotFoundException("name not found : " + s);
        }

        List<Authority> authList = authorityService.queryAuthByManagerId(manager.getId());
        if (!CollectionUtils.isEmpty(authList)){
            Authority authority = new Authority();
            authority.setAuthCode("_ADMIN");
            authority.setAuthName("后台基础权限");
            authList.add(authority);
        }

        request.getSession().setAttribute("managerId", manager.getId());
        return new User(manager.getName(), manager.getPassword(), authList);
    }
}
