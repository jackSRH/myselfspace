package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:数据项类型
 */
public enum PatrolResultStatus {
    NORMAL(0,"正常"),ABNORMAL(1,"异常");

    public Integer id;
    public String value;

    PatrolResultStatus(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static String getValue(Integer id){
        PatrolResultStatus[] patrolResultStatuses = PatrolResultStatus.values();
        for (PatrolResultStatus patrolResultStatus : patrolResultStatuses) {
            if(id.intValue() == patrolResultStatus.id.intValue()){
                return patrolResultStatus.value;
            }
        }
        return "";
    }
}
