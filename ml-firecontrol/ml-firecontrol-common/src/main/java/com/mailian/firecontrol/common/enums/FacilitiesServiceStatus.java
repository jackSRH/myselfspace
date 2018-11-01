package com.mailian.firecontrol.common.enums;

public enum FacilitiesServiceStatus {
    EFFECTIVE(0,"有效"),INVALID(1,"无效");

    public Integer id;
    public String value;

    FacilitiesServiceStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        FacilitiesServiceStatus[] facilitiesServiceStatuses = FacilitiesServiceStatus.values();
        for (FacilitiesServiceStatus facilitiesServiceStatus : facilitiesServiceStatuses) {
            if(id.intValue() == facilitiesServiceStatus.id.intValue()){
                return facilitiesServiceStatus.value;
            }
        }
        return "";
    }

}
