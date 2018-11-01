package com.mailian.firecontrol.common.enums;

/**
 * @Description:单位类型
 */
public enum UnitType {

    REPAST(1,"餐饮"),SHOPPING(2,"购物"),STAY(3,"住宿"),
    PUBLICENTERTAINMENT(4,"公共娱乐"),MEDICAL(5,"医疗"),TEACHING(6,"教学"),
    LEISUREFITNESS(7,"休闲健身"),PRODUCTIONANDPROCESSING(8,"生产加工"),FAEDG(9,"易燃易爆危险品");

    public Integer id;
    public String value;

    UnitType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        UnitType[] unitTypes = UnitType.values();
        for (UnitType unitType : unitTypes) {
            if(id.intValue() == unitType.id.intValue()){
                return unitType.value;
            }
        }
        return "";
    }
}
