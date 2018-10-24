package com.mailian.firecontrol.framework.config;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.framework.handler.MessageBaseHandler;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/29
 * @Description: MQTT配置类
 */
@Configuration
@IntegrationComponentScan
public class MqttConfig {
    private static final Logger log = LoggerFactory.getLogger(MqttConfig.class);

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.keepAliveInterval}")
    private Integer keepAliveInterval;

    @Value("${spring.mqtt.timeOut}")
    private Integer timeOut;

    /*@Value("${spring.mqtt.default.topic}")
    private String defaultTopic;*/

    @Autowired
    private MessageBaseHandler messageBaseHandler;


    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(keepAliveInterval);
        mqttConnectOptions.setConnectionTimeout(timeOut);
        //是否持久订阅  为true是非持久订阅
        mqttConnectOptions.setCleanSession(true);
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }
    /*@Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory) {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(clientId, mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
    */

    @Bean
    public MessageProducer mqttInbound(MqttPahoClientFactory mqttPahoClientFactory,MessageChannel mqttInputChannel) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,mqttPahoClientFactory);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel);
        adapter.setAutoStartup(false);
        //默认重连时间10s
        adapter.setRecoveryInterval(10000);
        return adapter;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String data = (String) message.getPayload();
                MessageHeaders messageHeaders = message.getHeaders();
                if(messageHeaders.containsKey(MqttHeaders.RECEIVED_TOPIC)) {
                    String topic = StringUtils.nvl(messageHeaders.get(MqttHeaders.RECEIVED_TOPIC),"").toString();
                    String messageId = message.getHeaders().getId().toString();
                    log.debug("消息id:{},处理消息开始:{}",messageId,data);
                    long startTime = System.currentTimeMillis();
                    int result = messageBaseHandler.doDispatch(topic,data);
                    long times = System.currentTimeMillis() - startTime;
                    log.debug("消息id:{}，处理完成结果:{}!耗时:{}",messageId,result>0?"成功":"失败",times);
                }
            }

        };
    }
}
