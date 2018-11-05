package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/2
 * @Description:
 */
public enum  AlarmWay {
    AUTO(0,"自动"),MANUAL(1,"手动");

    public Integer id;
    public String desc;

    AlarmWay(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
