package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:历史数据查找类型
 */
public enum FindHistoryDataType {
    RECORDWAVE(0,"录波"),HOUR(1,"时"),DAY(2,"日"),MONTH(3,"月"),YEAR(4,"年");

    public Integer id;
    public String value;

    FindHistoryDataType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        FindHistoryDataType[] findHistoryDataTypes = FindHistoryDataType.values();
        for (FindHistoryDataType findHistoryDataType : findHistoryDataTypes) {
            if(id.intValue() == findHistoryDataType.id.intValue()){
                return findHistoryDataType.value;
            }
        }
        return "";
    }


}
