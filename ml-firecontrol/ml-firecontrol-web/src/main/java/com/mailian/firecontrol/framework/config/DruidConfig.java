package com.mailian.firecontrol.framework.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.mailian.core.db.DataScopeInterceptor;
import com.mailian.core.db.EmptyWhereInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description: Druid数据库连接池配置
 */
@Configuration
public class DruidConfig {
    private final static Logger logger = LoggerFactory.getLogger(DruidConfig.class);

    /**
     *  因为SpringBoot也为我们提供了一个默认的datasource，可谓贴心，当我们需要自己的连接池的时候，可以使用@primary注解，告诉Springboot
     *  优先使用我们自己的DataSource
     * @return
     */
    @Bean(name="dataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*/*Mapper.xml"));
        sessionFactory.setPlugins(new Interceptor[]{new DataScopeInterceptor(),new EmptyWhereInterceptor()});
        return sessionFactory.getObject();
    }

    @Bean(name = "mapperScannerConfigurer")
    @Primary
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.mailian.firecontrol.dao");
        msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return msc;
    }
}
