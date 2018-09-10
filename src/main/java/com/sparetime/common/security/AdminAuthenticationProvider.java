package com.sparetime.common.security;

import com.sparetime.common.util.CommonUtil;
import com.sparetime.domian.LoginLog;
import com.sparetime.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by muye on 17/6/11.
 */
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginLogService loginLogService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = adminDetailsService.loadUserByUsername(userName);

        if (!passwordEncoder.matches(password, user.getPassword())){
            request.getSession().setAttribute("filed", "用户名或密码不正确");
            throw new BadCredentialsException("The password is wrong");
        }

        String kaptcha = request.getParameter("kaptcha");

        if (!request.getSession().getAttribute("KAPTCHA_SESSION_KEY").equals(kaptcha)){
            request.getSession().setAttribute("filed", "验证码错误");
            throw new BadCredentialsException("验证码错误");
        }

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(new BigDecimal((long)request.getSession().getAttribute("managerId")));
        loginLog.setType(1);
        loginLog.setIp(request.getHeader("x-forwarded-for") == null ?
                request.getRemoteAddr() :
                request.getHeader("x-forwarded-for"));

        loginLogService.insert(loginLog);

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}