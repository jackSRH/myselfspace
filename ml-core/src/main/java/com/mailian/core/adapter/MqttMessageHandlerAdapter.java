package com.mailian.core.adapter;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
public interface MqttMessageHandlerAdapter {
    /**
     * 校验处理器
     * @param topic
     * @return
     */
    boolean supports(String topic);

    /**
     * 具体处理
     */
    int handle(String message);
}
