package com.mailian.firecontrol.service.job;

import com.mailian.firecontrol.service.AlarmOpertionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@Component
public class AlarmJob {
    private static final Logger log = LoggerFactory.getLogger(AlarmJob.class);
    @Autowired
    private AlarmOpertionService alarmOpertionService;

    /**
     * 补全缺失的告警
     */
    private void completionEquipmentAlarm(){
        alarmOpertionService.completionEquipmentAlarm();
    }
}
