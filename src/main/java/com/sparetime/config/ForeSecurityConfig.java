package com.sparetime.config;

import com.sparetime.common.security.AdminLoginSuccessHandler;
import com.sparetime.common.security.ForeAuthenticationProvider;
import com.sparetime.common.security.ForeDetailsService;
import com.sparetime.common.security.ForeLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

/**
 * Created by muye on 17/7/18.
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class ForeSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    @Lazy
    private ForeAuthenticationProvider foreAuthenticationProvider;

    @Autowired
    @Lazy
    private SwitchUserFilter switchUserFilter;

    @Autowired
    ForeLoginSuccessHandler foreLoginSuccessHandler;

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(foreAuthenticationProvider);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http/*.csrf().disable()*/
//                .headers().cacheControl().disable().and()
                .addFilterAfter(switchUserFilter, FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers("/fore/other/lang").permitAll()
                .antMatchers("/fore/**").hasAnyAuthority("FORE")
                .anyRequest().permitAll()
                .and().authenticationProvider(foreAuthenticationProvider)
                .formLogin()
                .loginPage("/fore/login").failureUrl("/fore/login?error=true").permitAll().successHandler(foreLoginSuccessHandler)
                .and()
                .logout().permitAll();
    }

    @Bean
    public SwitchUserFilter switchUserFilter(ForeDetailsService foreDetailsService){
        SwitchUserFilter switchUserFilter = new SwitchUserFilter();
        switchUserFilter.setExitUserUrl("/fore/logout");
        switchUserFilter.setTargetUrl("/");
        switchUserFilter.setUserDetailsService(foreDetailsService);
        switchUserFilter.setUsernameParameter("userName");
        switchUserFilter.setSwitchUserUrl("/fore/switchUser");
        return switchUserFilter;
    }

}
