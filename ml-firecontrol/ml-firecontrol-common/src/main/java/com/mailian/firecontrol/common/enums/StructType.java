package com.mailian.firecontrol.common.enums;

public enum StructType {
    REMOTE(1,"遥控设置"),FACILITY(3,"设施监测");

    public Integer id;
    public String desc;

    StructType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

}
