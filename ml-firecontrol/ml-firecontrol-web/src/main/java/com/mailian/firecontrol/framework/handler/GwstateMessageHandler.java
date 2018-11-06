package com.mailian.firecontrol.framework.handler;

import com.mailian.core.adapter.MqttMessageHandlerAdapter;
import com.mailian.core.bean.SpringContext;
import com.mailian.core.enums.Status;
import com.mailian.core.util.JsonUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PushMessageTopic;
import com.mailian.firecontrol.dto.push.DeviceCommunicationStatus;
import com.mailian.firecontrol.framework.util.MqttTopicUtil;
import com.mailian.firecontrol.service.cache.DeviceCache;
import com.mailian.firecontrol.service.cache.UnitDeviceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
public class GwstateMessageHandler implements MqttMessageHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(GwstateMessageHandler.class);

    @Override
    public boolean supports(String topic) {
        return Pattern.matches(PushMessageTopic.GWSTATE.regex,topic);
    }

    @Override
    public int handle(String message) {
        int result = 0;
        try {
            DeviceCommunicationStatus deviceCommunicationStatus = JsonUtils.getObjectFromJsonString(message,DeviceCommunicationStatus.class);

            if(StringUtils.isNotNull(deviceCommunicationStatus)){
                DeviceCache deviceCache = (DeviceCache) SpringContext.getBean("deviceCache");
                UnitDeviceCache unitDeviceCache = (UnitDeviceCache) SpringContext.getBean("unitDeviceCache");
                deviceCache.updateStatus(deviceCommunicationStatus);
                unitDeviceCache.upUnitOnlineStatus(deviceCommunicationStatus);

                if(Status.NORMAL.id.intValue() == deviceCommunicationStatus.getStatus().intValue()){
                    MqttTopicUtil.addTopic(deviceCommunicationStatus.getGwid());
                }
                if(Status.DISABLE.id.intValue() == deviceCommunicationStatus.getStatus().intValue()){
                    MqttTopicUtil.removeTopic(deviceCommunicationStatus.getGwid(),false);
                }
            }
            result = 1;
        } catch (Exception e) {
            log.error("处理网关状态数据失败,message:{}",message,e);
        }
        return result;
    }
}
