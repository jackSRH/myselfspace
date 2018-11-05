package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/2
 * @Description:
 */
public enum AlarmHandleStatus {
    UNTREATED(0,"未处理"),RESPONSE(1,"已响应"),UNDER_WAY(2,"处理中"),COMPLETED(3,"已处理");

    public Integer id;
    public String desc;

    AlarmHandleStatus(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
