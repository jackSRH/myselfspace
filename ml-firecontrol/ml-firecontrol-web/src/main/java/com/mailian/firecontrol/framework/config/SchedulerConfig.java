package com.mailian.firecontrol.framework.config;

import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/30
 * @Description:
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {
    @Resource
    private DataSource dataSource;

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        /*延时启动(秒)*/
        schedulerFactoryBean.setStartupDelay(30);
        schedulerFactoryBean.setAutoStartup(true);
        /*启动时更新己存在的Job*/
        schedulerFactoryBean.setOverwriteExistingJobs(true);

        schedulerFactoryBean.setDataSource(dataSource);
    }
}
