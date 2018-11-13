package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/13
 * @Description:
 */
public enum OptType {
    ALARM(0,"产生告警"),RES_ALARM(1,"告警响应"),CONFIRM_ALARM(2,"确定告警"),PROGRESS_ALARM(3,"告警处理中"),COMPLETE_ALARM(4,"已处理告警");

    public Integer id;
    public String desc;

    OptType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
