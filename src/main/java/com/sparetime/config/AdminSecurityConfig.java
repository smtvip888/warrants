package com.sparetime.config;

import com.sparetime.common.security.AdminLoginSuccessHandler;
import com.sparetime.common.security.AdminAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by muye on 17/7/17.
 *
 * 登录及权限配置
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminAuthenticationProvider adminAuthenticationProvider;

    @Autowired
    private AdminLoginSuccessHandler successHandler;

    @Autowired
    ITemplateResolver templateResolver;

    @Autowired
    SpringSecurityDialect springSecurityDialect;

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(provider);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/admin/**")
                .headers().frameOptions().sameOrigin()
                .and()
//                .headers().cacheControl().disable().and()
                .authenticationProvider(adminAuthenticationProvider)
                .authorizeRequests()
                .anyRequest()
                .hasAnyAuthority("_ADMIN")
                .and()
                .formLogin()
                .loginPage("/admin/login").permitAll().successHandler(successHandler)
                .and()
                .logout().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Set<IDialect> dialectSet = new HashSet<>();
        dialectSet.add(springSecurityDialect);
        templateEngine.setAdditionalDialects(dialectSet);
        return templateEngine;
    }
}
