package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/9/10
 * @Description: 通知具体类型
 */
public enum RealNoticeType {
    ALARM_NOTICE(0,"告警通知");

    public Integer id;
    public String desc;

    RealNoticeType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
