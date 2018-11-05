package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:告警等级
 */
public enum AlarmLevel {
    ORDINARY(1,"普通"),SERIOUS(2,"严重");

    public Integer id;
    public String value;

    AlarmLevel(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        AlarmLevel [] alarmLevels = AlarmLevel.values();
        for (AlarmLevel alarmLevel : alarmLevels) {
            if(id.intValue() == alarmLevel.id.intValue()){
                return alarmLevel.value;
            }
        }
        return "";
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static AlarmLevel getAlarmLevel(Integer id){
        AlarmLevel [] alarmLevels = AlarmLevel.values();
        for (AlarmLevel alarmLevel : alarmLevels) {
            if(id.intValue() == alarmLevel.id.intValue()){
                return alarmLevel;
            }
        }
        return null;
    }
}
