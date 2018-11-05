package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/2
 * @Description:
 */
public enum  AlarmType {
    ALARM(1,"报警"),EARLY_WARNING(2,"预警");

    public Integer id;
    public String desc;

    AlarmType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
