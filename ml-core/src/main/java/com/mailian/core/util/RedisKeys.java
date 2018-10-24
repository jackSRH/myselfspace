package com.mailian.core.util;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
public class RedisKeys {

    public static String getSysConfigKey(String serverIdCard,String key){
        if(StringUtils.isEmpty(serverIdCard)){
            serverIdCard = "ml";
        }
        return serverIdCard+":sys:config:" + key;
    }

}
