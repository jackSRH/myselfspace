package com.mailian.core.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description: 渠道类型
 */
public enum  ChannelType {
    WEB(1,"webAPI"),APP(2,"appAPI");

    public Integer id;
    public String desc;

    ChannelType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
