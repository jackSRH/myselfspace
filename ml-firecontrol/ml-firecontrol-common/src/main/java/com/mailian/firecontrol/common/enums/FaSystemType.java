package com.mailian.firecontrol.common.enums;

public enum FaSystemType {
    FIRESYSTEM(1,"fire_conrol","火灾自动报警系统"),POWERSYSTEM(2,"power_monitor","智慧用电检测系统");

    public Integer id;
    public String code;
    public String value;

    FaSystemType(Integer id, String code, String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public static String getCodeById(Integer id){
        FaSystemType faSystemTypes [] = FaSystemType.values();
        for (FaSystemType faSystemType : faSystemTypes) {
            if(faSystemType.id.equals(id)){
                return faSystemType.code;
            }
        }
        return null;
    }
}
