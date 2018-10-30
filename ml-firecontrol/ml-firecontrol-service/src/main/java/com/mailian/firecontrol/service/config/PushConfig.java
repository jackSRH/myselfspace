package com.mailian.firecontrol.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
@Configuration
@PropertySource(value = "classpath:conf.properties")
public class PushConfig {
    @Value("${system.id}")
    public String SYSTEM_ID;
    @Value("${base.url}")
    public String BASE_URL;
    @Value("${getdevice.uri}")
    public String GET_DEVICE_URI;
    @Value("${setjfpgdate.uri}")
    public String SET_JFPGDATE_URI;
    @Value("${setalarm.uri}")
    public String SET_ALARM_URI;
    @Value("${getalram.uri}")
    public String GET_ALARM_URI;
    @Value("${setyxalarm.uri}")
    public String SET_YXALARM_URI;
    @Value("${getitem.uri}")
    public String GET_ITEM_URI;
    @Value("${getsignal.uri}")
    public String GET_SIGNAL_URI;
    @Value("${setyctran.uri}")
    public String SET_YCTRAN_URI;
    @Value("${setyxtran.uri}")
    public String SET_YXTRAN_URI;
    @Value("${setycstore.uri}")
    public String SET_YCSTORE_URI;
    @Value("${addyccalc.uri}")
    public String ADD_CALCITEM_URI;
    @Value("${updateyccalc.uri}")
    public String UPDATE_YCCALC_URI;
    @Value("${updateyxcalc.uri}")
    public String UPDATE_YXCALC_URI;
    @Value("${getapptotalenergy.uri}")
    public String GET_APPTOTALENERGY_URI;
    @Value("${updateyktran.uri}")
    public String UPDATE_YKTRAN_URI;
    @Value("${setyk.uri}")
    public String SET_YK_URI;
    @Value("${getappJFPG.uri}")
    public String GET_APPTOTALJFPG_URI;
    @Value("${doc.dir}")
    public String DEFAULT_PATH;
    @Value("${addmodel.uri}")
    public String ADD_MODEL;
    @Value("${getsub.uri}")
    public String GET_SUB_URI;
    @Value("${setuploadinterval.uri}")
    public String SET_UPLOAD_INTERVAL_URI;

    public String getUrl(String subUrl){
        return BASE_URL+subUrl;
    }
}
