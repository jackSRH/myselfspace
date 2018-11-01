package com.mailian.firecontrol.common.enums;

/**
 * @Description: 设施告警误告类型
 */
public enum FaMisreportType {
    MISREPORT(1,"误报"),EFFECTIVE(2,"有效"),OTHER(3,"其他");


    public Integer id;
    public String value;

    FaMisreportType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        FaMisreportType[] faMisreportTypes = FaMisreportType.values();
        for (FaMisreportType faMisreportType : faMisreportTypes) {
            if(id.intValue() == faMisreportType.id.intValue()){
                return faMisreportType.value;
            }
        }
        return "";
    }
}
