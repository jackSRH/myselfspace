package com.mailian.firecontrol.common.enums;


import com.mailian.core.util.StringUtils;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/9
 * @Description:数据字典类型
 */
public enum DictType {
    POWER_MONITOR("power_monitor","智慧用电监测系统"),
    FIRE_CONROL("fire_conrol","火灾自动报警系统");

    public String code;
    public String desc;

    DictType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DictType getByCode(String type){
        if(StringUtils.isEmpty(type)){
            return null;
        }

        DictType [] dictTypes = DictType.values();
        for (DictType dictType : dictTypes) {
            if(dictType.code.equals(type)){
                return dictType;
            }
        }
        return null;
    }
}
