package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/30
 * @Description:1：按小时 2：按天 3：按月 4：按年
 */
public enum  DeviceItemCycle {
    HOURE(1,"按小时"),DAY(2,"按天"),MONTH(3,"按月"),YEAR(4,"按年");

    public Integer id;
    public String desc;

    DeviceItemCycle(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
