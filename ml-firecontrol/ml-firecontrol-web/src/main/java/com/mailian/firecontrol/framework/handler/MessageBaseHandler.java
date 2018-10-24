package com.mailian.firecontrol.framework.handler;

import com.mailian.core.adapter.MqttMessageHandlerAdapter;
import com.mailian.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description:
 */
@Component
public class MessageBaseHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageBaseHandler.class);

    private static List<MqttMessageHandlerAdapter> handlerAdapters = new ArrayList<>();

    public MessageBaseHandler(){
        handlerAdapters.add(new GwstateMessageHandler());
    }

    public int doDispatch(String topic,String message){
        int result = 0;
        if(StringUtils.isEmpty(topic)){
            return result;
        }

        //得到对应适配器
        MqttMessageHandlerAdapter adapter = getHandler(topic);
        if(StringUtils.isNotNull(adapter) && StringUtils.isNotEmpty(message)) {
            //通过适配器执行对应的处理方法
            result = adapter.handle(message);
        }
        return result;
    }

    public MqttMessageHandlerAdapter getHandler(String topic){
        for(MqttMessageHandlerAdapter adapter: handlerAdapters){
            if(adapter.supports(topic)){
                return adapter;
            }
        }
        return null;
    }
}
