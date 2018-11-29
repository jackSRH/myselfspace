package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/2
 * @Description:
 */
public enum UnitStatus {
    ONLINE(0,"在线"),OFFLINE(1,"离线"),ALARM(2,"告警"),EARLYWARNING(3,"预警");

    public Integer id;
    public String desc;

    UnitStatus(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getDesc(Integer id){
        UnitStatus[] unitStatuses = UnitStatus.values();
        for (UnitStatus unitStatus : unitStatuses) {
            if(id.intValue() == unitStatus.id.intValue()){
                return unitStatus.desc;
            }
        }
        return "";
    }

}
