package com.mailian.firecontrol.framework.handler;

import com.alibaba.fastjson.TypeReference;
import com.mailian.core.adapter.MqttMessageHandlerAdapter;
import com.mailian.core.bean.SpringContext;
import com.mailian.core.util.JsonUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PushMessageTopic;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.service.cache.DeviceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
public class GwinfoMessageHandler implements MqttMessageHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(GwinfoMessageHandler.class);

    @Override
    public boolean supports(String topic) {
        return Pattern.matches(PushMessageTopic.GWINFO.regex,topic);
    }

    @Override
    public int handle(String message) {
        int result = 0;
        try {
            List<Device> deviceList = JsonUtils.getObjectFromJsonString(message,new TypeReference<List<Device>>(){});

            if(StringUtils.isNotEmpty(deviceList)){
                DeviceCache deviceCache = (DeviceCache) SpringContext.getBean("deviceCache");
                deviceCache.addDevices(deviceList);
            }
            result = 1;
        } catch (Exception e) {
            log.error("处理网关数据失败,message:{}",message,e);
        }

        return result;
    }
}
