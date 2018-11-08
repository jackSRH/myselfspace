package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/8
 * @Description:
 */
public enum  ReqDateType {
    DAY(1,1,"日"),WEEK(2,2,"周"),MONTH(3,2,"月"),YEAR(4,3,"年");

    public Integer id;
    public Integer itemCycle;
    public String desc;

    ReqDateType(Integer id, Integer itemCycle, String desc) {
        this.id = id;
        this.itemCycle = itemCycle;
        this.desc = desc;
    }
}
