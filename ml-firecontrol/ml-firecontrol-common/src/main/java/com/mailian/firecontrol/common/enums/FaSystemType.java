package com.mailian.firecontrol.common.enums;

public enum FaSystemType {
    FIRESYSTEM(1,"火灾自动报警系统"),POWERSYSTEM(2,"智慧用电检测系统");

    public Integer id;
    public String value;

    FaSystemType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
