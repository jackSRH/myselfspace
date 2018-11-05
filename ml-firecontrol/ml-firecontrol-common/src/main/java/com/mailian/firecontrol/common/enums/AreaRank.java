package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
public enum AreaRank {
    PROVINCE(0,"省份"),CITY(1,"城市"),AREA(2,"区域"),OTHER(99,"其它");

    public Integer id;
    public String desc;

    AreaRank(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
