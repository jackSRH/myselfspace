package com.mailian.firecontrol.framework.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/2
 * @Description:
 */
@Configuration
public class MapperNoWarn extends MybatisAutoConfiguration {

    public MapperNoWarn(MybatisProperties properties,
                        ObjectProvider<Interceptor[]> interceptorsProvider,
                        ResourceLoader resourceLoader,
                        ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                        ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {

        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);

    }
}
