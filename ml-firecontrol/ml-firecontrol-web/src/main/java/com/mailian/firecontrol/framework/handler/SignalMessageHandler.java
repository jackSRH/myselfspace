package com.mailian.firecontrol.framework.handler;

import com.alibaba.fastjson.TypeReference;
import com.mailian.core.adapter.MqttMessageHandlerAdapter;
import com.mailian.core.bean.SpringContext;
import com.mailian.core.util.JsonUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PushMessageTopic;
import com.mailian.firecontrol.dto.push.DeviceItemRealTimeData;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
public class SignalMessageHandler implements MqttMessageHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SignalMessageHandler.class);

    @Override
    public boolean supports(String topic) {
        return Pattern.matches(PushMessageTopic.SIGNAL.regex,topic);
    }

    @Override
    public int handle(String message) {
        int result = 0;
        try {
            List<DeviceItemRealTimeData> deviceItemRealTimeDataList = JsonUtils.getObjectFromJsonString(message,new TypeReference<List<DeviceItemRealTimeData>>(){});

            if(StringUtils.isNotEmpty(deviceItemRealTimeDataList)){
                DecimalFormat fnum = new DecimalFormat("##0.00");
                DeviceItemRepository deviceItemRepository = (DeviceItemRepository) SpringContext.getBean("deviceItemRepository");
                for (DeviceItemRealTimeData deviceItemRealTimeData : deviceItemRealTimeDataList) {
                    deviceItemRealTimeData.setVal(Float.parseFloat(fnum.format(deviceItemRealTimeData.getVal())));
                }
                deviceItemRepository.updateRealTime(deviceItemRealTimeDataList);
            }
            result = 1;
        } catch (Exception e) {
            log.error("处理数据项实时数据失败,message:{}",message,e);
        }
        return result;
    }
}
