package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:告警处理状态
 */
public enum AlarmHandleStatus {
    NODEAL(0,"未处理"),RESPONDED(1,"已响应"),DEALING(2,"处理中"),DEALED(3,"已处理");


    public Integer id;
    public String value;

    AlarmHandleStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        AlarmHandleStatus[] alarmHandleStatuses = AlarmHandleStatus.values();
        for (AlarmHandleStatus alarmHandleStatus : alarmHandleStatuses) {
            if(id.intValue() == alarmHandleStatus.id.intValue()){
                return alarmHandleStatus.value;
            }
        }
        return "";
    }
}
