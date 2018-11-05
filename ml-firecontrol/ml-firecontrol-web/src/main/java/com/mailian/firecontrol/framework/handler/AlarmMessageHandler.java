package com.mailian.firecontrol.framework.handler;

import com.alibaba.fastjson.TypeReference;
import com.mailian.core.adapter.MqttMessageHandlerAdapter;
import com.mailian.core.bean.SpringContext;
import com.mailian.core.util.JsonUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PushMessageTopic;
import com.mailian.firecontrol.dto.push.Alarm;
import com.mailian.firecontrol.service.AlarmOpertionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
public class AlarmMessageHandler implements MqttMessageHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(AlarmMessageHandler.class);

    @Override
    public boolean supports(String topic) {
        return Pattern.matches(PushMessageTopic.ALARM.regex,topic);
    }

    @Override
    public int handle(String message) {
        int result = 0;
        try {
            List<Alarm> alarmList = JsonUtils.getObjectFromJsonString(message,new TypeReference<List<Alarm>>(){});

            if(StringUtils.isNotEmpty(alarmList)){
                AlarmOpertionService alarmOpertionService = (AlarmOpertionService) SpringContext.getBean("alarmOpertionServiceImpl");
                alarmOpertionService.dealRealTimeAlarm(alarmList);
            }
            result = 1;
        } catch (Exception e) {
            log.error("处理实时告警失败,message:{}",message,e);
        }
        return result;
    }
}
