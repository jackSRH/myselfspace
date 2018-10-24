package com.mailian.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/9/4
 * @Description:
 */
@Configuration
public class SystemConfig {
    @Value("${server.idcard}")
    public String serverIdCard;
}
