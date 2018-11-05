package com.mailian.firecontrol.service;

import com.mailian.firecontrol.dto.push.Alarm;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
public interface AlarmOpertionService {
    /**
     * 处理实时告警
     * @param alarms
     * @return
     */
    void dealRealTimeAlarm(List<Alarm> alarms);

    void completionEquipmentAlarm();
}
