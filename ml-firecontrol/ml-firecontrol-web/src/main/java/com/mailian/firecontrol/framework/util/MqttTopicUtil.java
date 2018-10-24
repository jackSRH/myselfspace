package com.mailian.firecontrol.framework.util;

import com.mailian.core.bean.SpringContext;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.PushMessageTopic;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description: MQTT 订阅主题工具类
 */
public class MqttTopicUtil {
    /**
     * 添加订阅主题
     * @param deviceId
     */
    public static void addTopic(String deviceId){
        if(StringUtils.isEmpty(deviceId)){
            return;
        }
        List<String> topicList = PushMessageTopic.getTopicsByDeviceId(deviceId);
        MqttPahoMessageDrivenChannelAdapter adapter = (MqttPahoMessageDrivenChannelAdapter) SpringContext.getBean("mqttInbound");

        List<String> existsTopic = Arrays.asList(adapter.getTopic());
        for (String topic : topicList) {
            if(!existsTopic.contains(topic)) {
                adapter.addTopic(topic);
            }
        }

        if(!adapter.isRunning() && StringUtils.isNotEmpty(adapter.getTopic())){
            adapter.start();
        }
    }

    /**
     * 添加默认订阅主题
     * @param deviceId
     */
    public static void addDefaultTopic(String deviceId){
        if(StringUtils.isEmpty(deviceId)){
            return;
        }
        String defaultTopic = String.format(String.format(PushMessageTopic.GWSTATE.topic,deviceId));
        MqttPahoMessageDrivenChannelAdapter adapter = (MqttPahoMessageDrivenChannelAdapter) SpringContext.getBean("mqttInbound");

        List<String> existsTopic = Arrays.asList(adapter.getTopic());
        if(!existsTopic.contains(defaultTopic)) {
            adapter.addTopic(defaultTopic);
        }

        if(!adapter.isRunning() && StringUtils.isNotEmpty(adapter.getTopic())){
            adapter.start();
        }
    }

    /**
     * 更新订阅主题
     * @param oldDeviceId
     * @param newDeviceId
     */
    public static void updateTopic(String oldDeviceId,String newDeviceId){
        removeTopic(oldDeviceId,true);
        addTopic(newDeviceId);
    }

    /**
     * 移除订阅主题
     * @param deviceId
     * @param removeAll 移除所有
     */
    public static void removeTopic(String deviceId,boolean removeAll){
        if(StringUtils.isEmpty(deviceId)){
            return;
        }

        List<String> topicList = PushMessageTopic.getTopicsByDeviceId(deviceId);
        MqttPahoMessageDrivenChannelAdapter adapter = (MqttPahoMessageDrivenChannelAdapter) SpringContext.getBean("mqttInbound");

        String defaultTopic = String.format(String.format(PushMessageTopic.GWSTATE.topic,deviceId));
        for (String topic : topicList) {
            if(removeAll || !defaultTopic.equals(topic)){
                adapter.removeTopic(topic);
            }
        }

        if(adapter.isRunning() && StringUtils.isEmpty(adapter.getTopic())){
            adapter.stop();
        }
    }

    /**
     * 获取已订阅的主题
     * @return
     */
    public List<String> getTopics(){
        MqttPahoMessageDrivenChannelAdapter adapter = (MqttPahoMessageDrivenChannelAdapter) SpringContext.getBean("mqttInbound");
        return Arrays.asList(adapter.getTopic());
    }
}
