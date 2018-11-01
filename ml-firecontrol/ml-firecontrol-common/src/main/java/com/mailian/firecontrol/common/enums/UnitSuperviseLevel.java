package com.mailian.firecontrol.common.enums;

/**
 * @Description:单位监管等级
 */
public enum UnitSuperviseLevel {
    LEVEL1(1,"一级"),LEVEL2(2,"二级"),LEVEL3(3,"三级");

    public Integer id;
    public String value;

    UnitSuperviseLevel(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        UnitSuperviseLevel[] unitSuperviseLevels = UnitSuperviseLevel.values();
        for (UnitSuperviseLevel unitSuperviseLevel : unitSuperviseLevels) {
            if(id.intValue() == unitSuperviseLevel.id.intValue()){
                return unitSuperviseLevel.value;
            }
        }
        return "";
    }
}
