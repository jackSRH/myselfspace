package com.mailian.firecontrol.common.enums;


import com.mailian.core.util.StringUtils;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/23
 * @Description:
 */
public enum SysConfigType {
    ALARM_INFO(0,"sys.alarminfo","告警信息模板"),
    ALARM_RESPONSE_OVERTIME(1,"sys.alarmresponseovertime","响应告警超时时间");

    public Integer type;
    public String key;
    public String desc;

    SysConfigType(Integer type, String key, String desc) {
        this.type = type;
        this.key = key;
        this.desc = desc;
    }

    /**
     * 根据类型获取对应的配置
     * @param type
     * @return
     */
    public static SysConfigType getByType(Integer type){
        if(StringUtils.isNull(type)){
            return null;
        }

        SysConfigType [] sysConfigTypes = SysConfigType.values();
        for (SysConfigType sysConfigType : sysConfigTypes) {
            if(sysConfigType.type == type){
                return sysConfigType;
            }
        }
        return null;
    }
}
