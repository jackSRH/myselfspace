package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
public enum DiaItemType {
    DATA_ITEM(1,"数据项"),ALARM(2,"告警或者开关"),TELECONTROL(3,"遥控");

    public Integer id;
    public String desc;

    DiaItemType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
