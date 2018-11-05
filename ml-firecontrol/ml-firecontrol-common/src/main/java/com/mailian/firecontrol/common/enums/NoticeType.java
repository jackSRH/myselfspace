package com.mailian.firecontrol.common.enums;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/7
 * @Description:
 */
public enum NoticeType {
    ALARM(0,"告警");

    public Integer id;
    public String value;

    NoticeType(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * 获取对应属性值
     * @param id
     * @return
     */
    public static NoticeType getValue(Integer id){
        NoticeType[] noticeTypes = NoticeType.values();
        for (NoticeType noticeType : noticeTypes) {
            if(id.intValue() == noticeType.id.intValue()){
                return noticeType;
            }
        }
        return null;
    }
}
