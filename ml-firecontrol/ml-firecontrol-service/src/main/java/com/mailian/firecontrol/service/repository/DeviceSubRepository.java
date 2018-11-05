package com.mailian.firecontrol.service.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mailian.core.util.HttpClientUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.util.PushResponseUtil;
import com.mailian.firecontrol.dto.push.DeviceSub;
import com.mailian.firecontrol.service.config.PushConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/25
 * @Description:
 */
@Repository
public class DeviceSubRepository {
    @Autowired
    private PushConfig pushConfig;

    /**
     * 根据网关code 获取rtu信息
     * @param codes
     * @return
     */
    public List<DeviceSub> getDeviceSubsByCodes(List<String> codes){
        if(StringUtils.isEmpty(codes)){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("gwids", CollectionUtil.join(codes,","));
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_SUB_URI), params, null,false);

        List<DeviceSub> subList = PushResponseUtil.processResponseListData(res,DeviceSub.class);
        return subList;
    }

    /**
     * rtu数据上传
     * @param params
     * @return
     */
    public boolean setRtuUploadInterval(Map<String, Object> params){
        params.put("appid", pushConfig.SYSTEM_ID);
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_UPLOAD_INTERVAL_URI), data, null,false);
        return PushResponseUtil.processSuccess(res);
    }

    /**
     * 根据rtuid列表 获取rtu数据
     * @param rtuIds
     * @return
     */
    public List<DeviceSub> getDeviceSubsBySubIds(List<String> rtuIds){
        if(StringUtils.isEmpty(rtuIds)){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("rtuidcbs", CollectionUtil.join(rtuIds,","));
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_SUB_URI), params, null,false);
        List<DeviceSub> subList = PushResponseUtil.processResponseListData(res,DeviceSub.class);
        return subList;
    }
}
