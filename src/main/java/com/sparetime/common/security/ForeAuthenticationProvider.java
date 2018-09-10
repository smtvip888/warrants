package com.sparetime.common.security;

import com.sparetime.domian.LoginLog;
import com.sparetime.service.LoginLogService;
import com.sparetime.service.SharesConfigService;
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
 * Created by muye on 17/8/12.
 */
@Component
public class ForeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ForeDetailsService foreDetailsService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    SharesConfigService sharesConfigService;

    @Autowired
    private LoginLogService loginLogService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if(sharesConfigService.getConfig().getEnabledLogin() != 1){
            request.getSession().setAttribute("filed", "系统禁止登陆，请稍后再试或与管理员联系");
            throw new BadCredentialsException("验证码错误");
        }

        String kaptcha = request.getParameter("kaptcha");

        if (!request.getSession().getAttribute("KAPTCHA_SESSION_KEY").equals(kaptcha)){
            request.getSession().setAttribute("filed", "验证码错误");
            throw new BadCredentialsException("验证码错误");
        }

        String password = authentication.getCredentials().toString();
        String userName = authentication.getName();

        UserDetails details = null;
        try {
            details = foreDetailsService.loadUserByUsername(userName);
        } catch (Exception e){
            request.getSession().setAttribute("filed", e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }

        if (details == null || !passwordEncoder.matches(password, details.getPassword())){
            request.getSession().setAttribute("filed", "用户名或密码不正确");
            throw new BadCredentialsException("用户名或密码不正确");
        }

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId((BigDecimal) request.getSession().getAttribute("userId"));
        loginLog.setType(0);
        loginLog.setIp(request.getHeader("x-forwarded-for") == null ?
                request.getRemoteAddr() :
                request.getHeader("x-forwarded-for"));
        request.getSession().setAttribute("lang", request.getParameter("lang"));
        loginLogService.insert(loginLog);
        return new UsernamePasswordAuthenticationToken(details, password, details.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
