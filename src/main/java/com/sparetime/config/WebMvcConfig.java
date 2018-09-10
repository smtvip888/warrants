package com.sparetime.config;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.sparetime.aop.HttpCacheFilter;
import com.sparetime.domian.SysConfig;
import com.sparetime.service.SysConfigService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;

/**
 * Created by muye on 17/7/27.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//    @Value("${file.path}")
//    private String filePath;

    @Value("${http.port}")
    private int httpPort;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        SysConfig sysConfig = sysConfigService.queryByKey("file_path");
        if (sysConfig == null || StringUtils.isEmpty(sysConfig.getValue()))
            throw new RuntimeException("配置文件为空");
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:" + sysConfig.getValue() + "/")
                /*.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic())
                .setCacheControl(CacheControl.noCache())*/;
        super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/fore/index");
        super.addViewControllers(registry);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        return new ServletRegistrationBean(new KaptchaServlet(),"/images/kaptcha.jpg");
    }

    @Bean
    public FilterRegistrationBean httpCache(){
        FilterRegistrationBean registration = new FilterRegistrationBean(new HttpCacheFilter());
        registration.addUrlPatterns("/images/*", "/css/*", "/fonts/*", "/i/*", "/js/*");
        return registration;
    }

    //@Bean
    public EmbeddedServletContainerFactory servletContainer() {

        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected void postProcessContext(Context context) {

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/fore/*");
                collection.addPattern("/admin/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(serverPort);
        return connector;
    }
}
