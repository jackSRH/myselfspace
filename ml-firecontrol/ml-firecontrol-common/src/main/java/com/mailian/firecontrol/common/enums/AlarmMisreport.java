package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/9
 * @Description:
 */
public enum  AlarmMisreport {
    MISREPORT(1,"误报"),EFFECTIVE(2,"有效"),OTHER(3,"其它");

    public Integer id;
    public String desc;

    AlarmMisreport(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
