package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/2
 * @Description:
 */
public enum  AlarmType {
    ALARM(1,"烟感报警"),EARLY_WARNING(2,"漏电预警");
    //,OFF_LINE(3,"离线告警");

    public Integer id;
    public String desc;

    AlarmType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getDesc(Integer id){
        AlarmType[] alarmTypes = AlarmType.values();
        for (AlarmType alarmType : alarmTypes) {
            if(id.intValue() == alarmType.id.intValue()){
                return alarmType.desc;
            }
        }
        return "";
    }

}
