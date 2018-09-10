package com.sparetime.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.sparetime.common.mysql.page.PageInterceptor;
import com.sparetime.common.mysql.result.ResultInterceptor;
import com.sparetime.common.util.DESedeUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by muye on 17/7/17.
 */
@Configuration
@PropertySource("file:${config.dir.path}/db.properties")
public class DBConfig {

    private static Logger logger = LoggerFactory.getLogger(DBConfig.class);

    private static final String ENCRYPT_KEY = "JI3$JU4%ONH2@JN4@JOI#NO$";
    private static final String ENCRYPT_IV = "gpg@123#";
    private static final String ENCRYPT_MODEL = "/CBC/PKCS5Padding";

    @Bean(name = "mainDS")
    public DruidDataSource mainDataSource(
            @Value("${db.url}") String url,
            @Value("${db.name}") String name,
            @Value("${db.password}") String password/*,
            @Value("${db.encrypt.key}") String key,
            @Value("${db.encrypt.iv}") String iv,
            @Value("${db.encrypt.model}") String model*/) throws Exception{

        logger.info("config main db");

        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(url);
        ds.setUsername(name);
        ds.setPassword(DESedeUtil.decrypt(password, ENCRYPT_KEY, ENCRYPT_IV, ENCRYPT_MODEL));
        ds.setInitialSize(0);
        ds.setMaxActive(20);
        ds.setMinIdle(0);
        ds.setMaxWait(5000);
        ds.setValidationQuery("select 1");
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setMinEvictableIdleTimeMillis(25200000);
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(1800);
        ds.setLogAbandoned(true);
        try {
            ds.setFilters("mergeStat");
        }catch (Exception e){
            logger.error("mainDS error ",e);
        }

        return ds;
    }

    @Bean(name = "mainSqlSessionFactory")
    public SqlSessionFactory mainSqlSessionFactory(@Qualifier("mainDS") DruidDataSource mainDS) throws Exception {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(mainDS);
        factoryBean.setTypeHandlersPackage("com.sparetime.dao.typehandlers");
        factoryBean.setPlugins(new Interceptor[]{new PageInterceptor(), new ResultInterceptor()});
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        logger.info("config main mapper");
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.sparetime.dao.mapper");
        configurer.setSqlSessionFactoryBeanName("mainSqlSessionFactory");
        return configurer;
    }
}
