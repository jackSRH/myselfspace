package com.mailian.firecontrol.framework.handler;

import com.alibaba.fastjson.TypeReference;
import com.mailian.core.adapter.MqttMessageHandlerAdapter;
import com.mailian.core.bean.SpringContext;
import com.mailian.core.util.JsonUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PushMessageTopic;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
public class DataitemMessageHandler implements MqttMessageHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(DataitemMessageHandler.class);

    @Override
    public boolean supports(String topic) {
        return Pattern.matches(PushMessageTopic.DATAITEM.regex,topic);
    }

    @Override
    public int handle(String message) {
        int result = 0;
        try {
            List<DeviceItem> deviceItemList = JsonUtils.getObjectFromJsonString(message,new TypeReference<List<DeviceItem>>(){});
            if(StringUtils.isNotEmpty(deviceItemList)){
                DeviceItemRepository deviceItemRepository = (DeviceItemRepository) SpringContext.getBean("deviceItemRepository");
                deviceItemRepository.updateDeviceItemInfosOfSub(deviceItemList);
            }
            result = 1;
        } catch (Exception e) {
            log.error("处理数据项信息失败,message:{}",message,e);
        }
        return result;
    }
}
