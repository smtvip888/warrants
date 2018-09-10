package com.sparetime.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@EnableRedisHttpSession
@PropertySource("file:${config.dir.path}/db.properties")
public class HttpSessionRedisConfig extends AbstractHttpSessionApplicationInitializer{

    @Value("${spring.redis.host}")
    private String host;

    public HttpSessionRedisConfig(){
        super(HttpSessionRedisConfig.class);
    }

    @Bean
    public LettuceConnectionFactory connectionFactory(){
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(host);
        return connectionFactory;
    }
}
