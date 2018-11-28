package com.mailian.firecontrol.service.repository;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mailian.core.util.HttpClientUtil;
import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.ItemStype;
import com.mailian.firecontrol.common.util.PushResponseUtil;
import com.mailian.firecontrol.dto.push.*;
import com.mailian.firecontrol.service.cache.DeviceSubCache;
import com.mailian.firecontrol.service.config.PushConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/25
 * @Description:
 */
@Repository
public class DeviceItemRepository {
    private static final Logger log = LoggerFactory.getLogger(DeviceItemRepository.class);

    @Autowired
    private DeviceSubCache deviceSubCache;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PushConfig pushConfig;
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 通过RTU列表获取数据项信息
     * @param subs
     * @return
     */
    public List<DeviceItem> getItemInfosBySubs(List<String> subs){
        if(StringUtils.isEmpty(subs)) {
            return  null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("rtuidcbs", CollectionUtil.join(subs,","));
        String param = JSON.toJSONString(params);
        //请求后台接口获取数据
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_ITEM_URI), param, null,false);
        return PushResponseUtil.processResponseListData(res,DeviceItem.class);
    }

    /**
     * 通过数据项id列表获取数据项信息
     * @param itemIds
     * @return
     */
    public List<DeviceItem> getItemInfosByItemIds(String itemIds){
        if(StringUtils.isEmpty(itemIds)){
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("itemids", itemIds);
        String param = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_ITEM_URI), param, null,false);
        return PushResponseUtil.processResponseListData(res,DeviceItem.class);
    }


    /**
     * 通过数据项id列表(多个id用，隔开)，开始时间，结束时间，时间间隔类型获取历史数据
     * @param itemIds 数据项id列表，type 1：按小时 2：按天 3：按月 4：按年
     * @param startTime
     * @param endTime
     * @param type
     * @return
     * @throws ParseException
     */
    public List<DeviceItemHistoryData> getItemDataByItemIdAndTime(String itemIds,String startTime,String endTime,Integer type)  {
        final JSONObject postData = new JSONObject();
        postData.put("appid", pushConfig.SYSTEM_ID);
        postData.put("itemids", itemIds);
        postData.put("stime",startTime);
        postData.put("etime",endTime);
        postData.put("cycle",type);

        String res = restTemplate.postForEntity(pushConfig.getUrl(pushConfig.GET_SIGNAL_URI),postData, String.class).getBody();
        List<DeviceItemHistoryData> historyDatas = PushResponseUtil.processResponseListData(res,DeviceItemHistoryData.class);

        /*if(DeviceItemCycle.HOURE.id.equals(type)) {
            for(DeviceItemHistoryData historyData : historyDatas) {
                Date tm = DateUtil.offsetHour(historyData.getTm(),1);
                historyData.setTm(tm);
            }
        }*/
        return historyDatas;
    }


    /**
     * 设置遥测传输参数
     * @param ycTrans
     * @param rtuIds
     * @return
     */
    public Boolean setYcTranInfos(List<YcTran> ycTrans, String rtuIds) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", ycTrans);
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_YCTRAN_URI), data, null,false);

        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            updateCacheAfterUpdateTranItems(Arrays.asList(rtuIds.split(",")));
        }
        return updateRes;
    }

    /**
     * 设置遥控值
     * @param itemId
     * @param val
     * @return
     */
    public Boolean setYk(String itemId,Integer val){
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("itemid", itemId);
        params.put("val", val);

        String param = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_YK_URI), param, null,false);
        return PushResponseUtil.processSuccess(res);
    }

    /**
     * 设置遥控传输参数
     * @param ykTrans
     * @param rtuIds
     * @return
     */
    public Boolean setYkTranInfos(List<YkTran> ykTrans, String rtuIds) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", ykTrans);
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.UPDATE_YKTRAN_URI), data, null,false);

        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            updateCacheAfterUpdateTranItems(Arrays.asList(rtuIds.split(",")));
        }
        return updateRes;
    }

    /**
     * 设置遥信传输参数
     * @param yxTrans
     * @param rtuIds
     * @return
     */
    public Boolean setYxTranInfos(List<YxTran> yxTrans, String rtuIds) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", yxTrans);
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_YXTRAN_URI), data, null,false);

        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            updateCacheAfterUpdateTranItems(Arrays.asList(rtuIds.split(",")));
        }
        return updateRes;
    }

    /**
     * 设置遥测传输参数
     * @param ycStores
     * @param deviceCodes
     * @return
     */
    public Boolean setYcStoreInfos(List<YcStore> ycStores,String deviceCodes) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", ycStores);
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.SET_YCSTORE_URI), data, null,false);

        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            upateCacheAfterUpdateAlarmStoreItems(Arrays.asList(deviceCodes.split(",")));
        }
        return updateRes;
    }

    /**
     * 添加运算库数据项
     * @param deviceItem
     * @param deviceCodes
     * @return
     */
    public Boolean addCalcItem(DeviceItem deviceItem,String deviceCodes) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("id", deviceItem.getId());
        params.put("stype", deviceItem.getStype());

        params.put("formula",deviceItem.getGetval());
        params.put("gwid",deviceItem.getGwid());
        params.put("relid",deviceItem.getRelid());
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.ADD_CALCITEM_URI), data, null,false);

        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            updateCacheAfterUpdateCalcItems(Arrays.asList(deviceCodes.split(",")));
        }
        return updateRes;
    }

    /**
     * 修改遥测运算库数据项
     * @param ycCalcs
     * @param deviceCodes
     * @return
     */
    public Boolean updateYcCalcItem(List<YcCalc> ycCalcs,String deviceCodes) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", ycCalcs);
        String data = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.UPDATE_YCCALC_URI), data, null,false);

        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            updateCacheAfterUpdateCalcItems(Arrays.asList(deviceCodes.split(",")));
        }
        return updateRes;
    }

    /**
     * 修改遥信运算库数据项
     * @param yxCalcs
     * @param deviceCodes
     * @return
     */
    public Boolean updateYxCalcItem(List<YxCalc> yxCalcs,String deviceCodes) {
        final Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("paras", yxCalcs);
        String data = JSON.toJSONString(params);

        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.UPDATE_YXCALC_URI), data, null,false);
        boolean updateRes = PushResponseUtil.processSuccess(res);
        if(updateRes) {
            updateCacheAfterUpdateCalcItems(Arrays.asList(deviceCodes.split(",")));
        }
        return updateRes;
    }

    /**
     *
     * @param deviceCodes
     * @param types
     * @return
     */
    public List<DeviceItem> getCalcItemsByCodesAndType(String deviceCodes,String types){
        if(StringUtils.isEmpty(deviceCodes) && StringUtils.isEmpty(types)){
            return null;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("gwids", deviceCodes);
        params.put("stypes", types);

        String param = JSON.toJSONString(params);
        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_ITEM_URI), param, null,false);
        return PushResponseUtil.processResponseListData(res,DeviceItem.class);
    }

    public Float getTotalEnergyByItems(String itemIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("itemids",itemIds);
        String param = JSON.toJSONString(params);

        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_APPTOTALENERGY_URI), param, null,false);
        Map dataMap = PushResponseUtil.processResponseMapData(res);
        if(StringUtils.isNotEmpty(dataMap)  && dataMap.containsKey("total")){
            return Float.parseFloat(dataMap.get("total")+"");
        }
        return null;
    }

    /**
     *
     * @param itemIds
     * @return
     */
    public List<AppTotalJFPG> getAppTotalJFGPByItems(String itemIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", pushConfig.SYSTEM_ID);
        params.put("itemids",itemIds);
        String param = JSON.toJSONString(params);

        String res = HttpClientUtil.postBody(pushConfig.getUrl(pushConfig.GET_APPTOTALJFPG_URI), param, null,false);
        List<AppTotalJFPG> jfpg = PushResponseUtil.processResponseListData(res,AppTotalJFPG.class);
        return jfpg;
    }

    /**
     * 更新RTU底下的数据项信息
     * @param deviceItemList
     */
    public Map<String,List<DeviceItem>> updateDeviceItemInfosOfSub(List<DeviceItem> deviceItemList) {
        Map<String,List<DeviceItem>> deviceSubId2ItemInfos = new HashMap<>();
        if(StringUtils.isNotEmpty(deviceItemList)) {
            for (DeviceItem deviceItem : deviceItemList) {
                String rtuidcb = deviceItem.getRtuidcb();
                if (deviceSubId2ItemInfos.containsKey(rtuidcb)) {
                    deviceSubId2ItemInfos.get(rtuidcb).add(deviceItem);
                } else {
                    List<DeviceItem> deviceItems = new ArrayList<>();
                    deviceItems.add(deviceItem);
                    deviceSubId2ItemInfos.put(rtuidcb, deviceItems);
                }

            }

            for (Map.Entry<String, List<DeviceItem>> deviceItemEntry : deviceSubId2ItemInfos.entrySet()) {
                CollectionUtil.sort(deviceItemEntry.getValue(), new Comparator<DeviceItem>() {
                    @Override
                    public int compare(DeviceItem o1, DeviceItem o2) {
                        return o1.getSid().compareTo(o2.getSid());
                    }
                });
            }

            updateId2ItemInfo(deviceItemList);
            redisUtils.addAllHashValue(CommonConstant.DEVICE_SUB_ITEM, deviceSubId2ItemInfos, CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        }
        return deviceSubId2ItemInfos;
    }

    /**
     * 更新数据项id和数据项信息
     * @param deviceItems
     */
    public Map<String,DeviceItem> updateId2ItemInfo(List<DeviceItem> deviceItems) {
        if(StringUtils.isEmpty(deviceItems)){
            return null;
        }
        Map<String,DeviceItem> idItemMap = new HashMap<>();
        for(DeviceItem item : deviceItems) {
            idItemMap.put(item.getId(), item);
        }
        redisUtils.addAllHashValue(CommonConstant.ID_ITEM,idItemMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        return idItemMap;
    }

    /**
     * 通过RTUId列表获取数据项信息
     * @param deviceSubIds
     * @return
     */
    public Map<String,List<DeviceItem>> getDeviceItemInfosBySubIds(List<String> deviceSubIds){
        Map<String,List<DeviceItem>> deviceItemMap = redisUtils.entries(CommonConstant.DEVICE_SUB_ITEM);
        Map<String,List<DeviceItem>> subId2Items = new HashMap<>();
        if(StringUtils.isEmpty(deviceItemMap)){
            deviceItemMap = new HashMap<>();
        }

        List<String> needFindSubId = new ArrayList<>();
        for(String subId:deviceSubIds){
            if(deviceItemMap.containsKey(subId)){
                subId2Items.put(subId,deviceItemMap.get(subId));
            }else{
                needFindSubId.add(subId);
            }
        }

        if(StringUtils.isNotEmpty(needFindSubId)) {
            List<DeviceItem> apiDeviceItemList = getItemInfosBySubs(needFindSubId);
            setItemsVal(apiDeviceItemList);
            Map<String,List<DeviceItem>> deviceItemMapResult = updateDeviceItemInfosOfSub(apiDeviceItemList);
            if(StringUtils.isNotEmpty(deviceItemMapResult)){
                subId2Items.putAll(deviceItemMapResult);
            }
        }
        return subId2Items;
    }

    /**
     * 通过通信机code列表获取数据项信息
     * @param deviceCodes
     * @return
     */
    public Map<String,List<DeviceItem>> getDeviceItemInfosByCodes(List<String> deviceCodes){
        Map<String,List<DeviceItem>> code2Infos = new HashMap<>();
        Map<String, List<DeviceSub>> codeSubMap = deviceSubCache.getSubsByCodes(deviceCodes);
        List<DeviceItem> infos;
        List<String> subIds;
        for(Map.Entry<String, List<DeviceSub>> entry : codeSubMap.entrySet()) {
            subIds = new ArrayList<>();
            infos = new ArrayList<>();

            for(DeviceSub sub : entry.getValue()) {
                subIds.add(sub.getRtuidcb());
            }
            Map<String,List<DeviceItem>> sub2Items = getDeviceItemInfosBySubIds(subIds);

            for(Map.Entry<String, List<DeviceItem>> en : sub2Items.entrySet()) {
                infos.addAll(en.getValue());
            }
            code2Infos.put(entry.getKey(), infos);
        }
        return code2Infos;
    }

    /**
     * 通过通信机code列表获取传输库数据项实时数据
     * @param deviceCodes
     * @return
     */
    public Map<String, List<DeviceItemRealTimeData>> getDeviceItemRealTimeDataByCodes(List<String> deviceCodes){
        Map<String, List<DeviceItemRealTimeData>> code2Datas = new HashMap<>();
        List<DeviceItemRealTimeData> realTimeDatas;
        List<String> subIds;

        Map<String, List<DeviceSub>> code2Subs = deviceSubCache.getSubsByCodes(deviceCodes);
        if(StringUtils.isEmpty(code2Subs)){
            code2Subs = new HashMap<>();
        }
        for(Map.Entry<String, List<DeviceSub>> entry : code2Subs.entrySet()) {
            subIds = new ArrayList<>();
            realTimeDatas = new ArrayList<>();

            for(DeviceSub sub : entry.getValue()) {
                subIds.add(sub.getRtuidcb());
            }

            List<String> itemIds = new ArrayList<>();
            Map<String, List<DeviceItem>> subId2Items = getDeviceItemInfosBySubIds(subIds);
            for(Map.Entry<String, List<DeviceItem>> en: subId2Items.entrySet()) {
                for(DeviceItem item : en.getValue()) {
                    itemIds.add(item.getId());
                }
            }

            Map<String, DeviceItemRealTimeData> id2RealTimeData = getDeviceItemRealTimeDatasByItemIds(itemIds);
            for(Map.Entry<String, DeviceItemRealTimeData> en : id2RealTimeData.entrySet()) {
                if(null != en.getValue()) {
                    realTimeDatas.add(en.getValue());
                }
            }
            code2Datas.put(entry.getKey(), realTimeDatas);
        }

        return code2Datas;
    }

    /**
     * 通过通信机code列表获取code下所有RTU，以及RTU下所有数据项
     * @param deviceCodes
     * @return
     */
    public Map<String, Map<String, List<DeviceItem>>> getSubsAndItemsBycodes(List<String> deviceCodes){
        Map<String, Map<String, List<DeviceItem>>> code2Subs2Items = new HashMap<String, Map<String, List<DeviceItem>>>();
        List<String> subIds;

        Map<String, List<DeviceSub>> code2Subs = deviceSubCache.getSubsByCodes(deviceCodes);
        if(StringUtils.isEmpty(code2Subs)){
            code2Subs = new HashMap<String, List<DeviceSub>>();
        }
        for(Map.Entry<String, List<DeviceSub>> entry : code2Subs.entrySet()) {
            subIds = new ArrayList<String>();
            for(DeviceSub sub : entry.getValue()) {
                subIds.add(sub.getRtuidcb());
            }
            Map<String,List<DeviceItem>> sub2Items = getDeviceItemInfosBySubIds(subIds);
            code2Subs2Items.put(entry.getKey(), sub2Items);
        }

        return code2Subs2Items;
    }

    /**
     * 通过数据项id获取数据项信息
     * @param itemId
     * @return
     */
    public DeviceItem getDeviceItemInfosByItemId(String itemId) {
        DeviceItem itemInfo = redisUtils.getHashValue(CommonConstant.ID_ITEM,itemId);
        if(null == itemInfo) {
            List<DeviceItem> deviceItems = getItemInfosByItemIds(itemId);
            if(StringUtils.isEmpty(deviceItems)) {
                return null;
            }else {
                itemInfo = deviceItems.get(0);
                redisUtils.addHashValue(CommonConstant.ID_ITEM,itemId,itemInfo,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
            }
        }

        DeviceItemRealTimeData rtData = getRtDataByItemId(itemId);
        Float val = StringUtils.isNull(rtData)?CommonConstant.ITEM_INITIAL_VALUE:rtData.getVal();
        itemInfo.setVal(val);
        return itemInfo;
    }

    /**
     * 从缓存中获取，没有获取到则把id保存下来
     * @param itemIds
     * @return
     */
    public Map<String, DeviceItem> getDeviceItemInfosByItemIds(List<String> itemIds){
        Map<String,DeviceItem> idDeviceItemMap = redisUtils.entries(CommonConstant.ID_ITEM);

        Map<String,DeviceItem> resultMap = new HashMap<>();
        if(StringUtils.isEmpty(idDeviceItemMap)){
            idDeviceItemMap = new HashMap<>();
        }

        Map<String,DeviceItemRealTimeData> rtDataMap = getRtDataMapByItemIds(itemIds);
        List<String> needFindIds = new ArrayList<String>();
        for(String itemId : itemIds) {
            if(idDeviceItemMap.containsKey(itemId)) {
                DeviceItem itemInfo = idDeviceItemMap.get(itemId);
                DeviceItemRealTimeData rtData = rtDataMap.get(itemId);
                Float val = null == rtData?CommonConstant.ITEM_INITIAL_VALUE:rtData.getVal();
                itemInfo.setVal(val);
                resultMap.put(itemId,itemInfo);
            }else {
                needFindIds.add(itemId);
            }
        }

        //缓存没有则去数据库中查找
        if(StringUtils.isNotEmpty(needFindIds)) {
            List<DeviceItem> deviceItems = getItemInfosByItemIds(CollectionUtil.join(needFindIds,","));
            setItemsVal(deviceItems);
            Map<String,DeviceItem> deviceItemMap = updateId2ItemInfo(deviceItems);
            if(StringUtils.isNotEmpty(deviceItemMap)){
                resultMap.putAll(deviceItemMap);
            }
        }
        return resultMap;
    }

    /**
     * 通过通信机code获取运算库数据
     * @param deviceCodes
     * @return
     */
    public Map<String, List<DeviceItem>> getCalcItemsByDeviceCodes(List<String> deviceCodes){
        List<String> needFindCodes = new ArrayList<>();
        Map<String, List<DeviceItem>> calcItemMap = redisUtils.entries(CommonConstant.DEVICE_CODE_CALC_ITEM);

        Map<String, List<DeviceItem>> resultMap = new HashMap<>();
        if(StringUtils.isNull(calcItemMap)){
            calcItemMap = new HashMap<>();
        }
        for(String code : deviceCodes) {
            List<DeviceItem> calcItems = calcItemMap.get(code);
            if(StringUtils.isEmpty(calcItems)) {
                needFindCodes.add(code);
            }else {
                setItemsVal(calcItems);
                resultMap.put(code,calcItems);
            }
        }

        if(StringUtils.isNotEmpty(needFindCodes)) {
            List<DeviceItem> calcItems = getCalcItemsByCodesAndType(CollectionUtil.join(needFindCodes,","), ItemStype.OPERATIONYC.id+","+ItemStype.OPERATIONYX.id);
            setItemsVal(calcItems);

            Map<String, List<DeviceItem>> calcItemResultMap = updateCalcItem(calcItems);
            if(StringUtils.isNotEmpty(calcItemResultMap)){
                resultMap.putAll(calcItemResultMap);
            }
        }
        return resultMap;
    }

    /**
     * 更新运算库
     * @param calcItems
     * @return
     */
    public Map<String, List<DeviceItem>> updateCalcItem(List<DeviceItem> calcItems) {
        if(StringUtils.isEmpty(calcItems)){
            return null;
        }

        updateId2ItemInfo(calcItems);

        Map<String, List<DeviceItem>> calcItemMap = new HashMap<>();
        for (DeviceItem calcItem : calcItems) {
            String gwid = calcItem.getGwid();
            List<DeviceItem> items = calcItemMap.containsKey(gwid)?calcItemMap.get(gwid) :new ArrayList<>();
            items.add(calcItem);
            calcItemMap.put(gwid, items);
        }
        redisUtils.addAllHashValue(CommonConstant.DEVICE_CODE_CALC_ITEM,calcItemMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
        return calcItemMap;
    }


    /**
     * 通过数据项id获取数据项实时数据
     * @param itemIds
     * @return
     */
    public Map<String, DeviceItemRealTimeData> getDeviceItemRealTimeDatasByItemIds(List<String> itemIds){
        Map<String, DeviceItemRealTimeData> realTimeDataMap = getRtDataMapByItemIds(itemIds);
        return realTimeDataMap;
    }

    /**
     * 修改传输库数据项后更新缓存
     * @param rtuIds
     */
    public void updateCacheAfterUpdateTranItems(List<String> rtuIds) {
        if(StringUtils.isEmpty(rtuIds)){
            return;
        }
        List<DeviceItem> rtuList = getItemInfosBySubs(rtuIds);
        updateDeviceItemInfosOfSub(rtuList);
    }

    /**
     * 修改运算库数据项后更新缓存
     * @param deviceCodes
     */
    public void updateCacheAfterUpdateCalcItems(List<String> deviceCodes) {
        if(StringUtils.isEmpty(deviceCodes)){
            return;
        }
        List<DeviceItem> calcItems = getCalcItemsByCodesAndType(CollectionUtil.join(deviceCodes,","), ItemStype.OPERATIONYC.id+","+ItemStype.OPERATIONYX.id);

        updateCalcItem(calcItems);
    }

    /**
     * 修改存储库，告警库数据项后更新缓存
     * @param deviceCodes
     */
    public void upateCacheAfterUpdateAlarmStoreItems(List<String> deviceCodes) {
        Map<String, List<DeviceSub>> code2Subs = deviceSubCache.getSubsByCodes(deviceCodes);
        if(StringUtils.isEmpty(code2Subs)){
            code2Subs = new HashMap<String, List<DeviceSub>>();
        }

        List<String> rtuIds = new ArrayList<String>();
        for(Map.Entry<String, List<DeviceSub>> entry : code2Subs.entrySet()) {
            for(DeviceSub sub : entry.getValue()) {
                rtuIds.add(sub.getRtuidcb());
            }
        }

        updateCacheAfterUpdateTranItems(rtuIds);
        updateCacheAfterUpdateCalcItems(deviceCodes);
    }

    /**
     * 在数据项中添加实时数据
     * @param items
     */
    private void setItemsVal(List<DeviceItem> items){
        if(StringUtils.isEmpty(items)){
            return;
        }
        List<String> itemIdList = new ArrayList<>();
        for (DeviceItem item : items) {
            itemIdList.add(item.getId());
        }

        Map<String,DeviceItemRealTimeData> rtDataMap = getRtDataMapByItemIds(itemIdList);

        for(DeviceItem item : items){
            DeviceItemRealTimeData rtData = rtDataMap.get(item.getId());
            Float val = null == rtData?CommonConstant.ITEM_INITIAL_VALUE:rtData.getVal();
            item.setVal(val);
        }
    }

    /**
     * 更新实时数据
     * @param rtData
     */
    public void updateRealTime(DeviceItemRealTimeData rtData) {
        redisUtils.addHashValue(CommonConstant.DEVICE_ITEM_REAL_TIME_DATA,rtData.getId(),rtData,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }

    /**
     * 更新实时数据
     */
    public void updateRealTime(List<DeviceItemRealTimeData> rtDataList) {
        Map<String,DeviceItemRealTimeData> rtDataMap = new HashMap<>();
        Date lastUpdatetime = new Date();
        for (DeviceItemRealTimeData deviceItemRealTimeData : rtDataList) {
            deviceItemRealTimeData.setLastUpdateTime(lastUpdatetime);
            rtDataMap.put(deviceItemRealTimeData.getId(),deviceItemRealTimeData);
        }

        redisUtils.addAllHashValue(CommonConstant.DEVICE_ITEM_REAL_TIME_DATA,rtDataMap,CommonConstant.PUSH_REDIS_DEFAULT_EXPIRE);
    }


    /**
     * 根据数据项id获取实时数据
     * @param itemId
     * @return
     */
    public DeviceItemRealTimeData getRtDataByItemId(String itemId) {
        DeviceItemRealTimeData rtData = redisUtils.getHashValue(CommonConstant.DEVICE_ITEM_REAL_TIME_DATA,itemId);
        return rtData;
    }

    /**
     * 根据数据项id列表获取实时数据
     * @param itemIds
     * @return
     */
    public List<DeviceItemRealTimeData> getRtDataByItemIds(List<String> itemIds){
        List<DeviceItemRealTimeData> rtDataList = new ArrayList<>();
        List<DeviceItemRealTimeData> rtDatas = redisUtils.getHashMultiValue(CommonConstant.DEVICE_ITEM_REAL_TIME_DATA,itemIds);
        for (DeviceItemRealTimeData deviceItemRealTimeData : rtDatas) {
            if(StringUtils.isNotNull(deviceItemRealTimeData)){
                rtDataList.add(deviceItemRealTimeData);
            }
        }
        return rtDataList;
    }

    /**
     * 根据数据项id列表获取实时数据
     * @param itemIds
     * @return
     */
    public Map<String,DeviceItemRealTimeData> getRtDataMapByItemIds(List<String> itemIds){
        List<DeviceItemRealTimeData> rtDataList = redisUtils.getHashMultiValue(CommonConstant.DEVICE_ITEM_REAL_TIME_DATA,itemIds);

        Map<String,DeviceItemRealTimeData> rtDataMap = new HashMap<>();
        for (DeviceItemRealTimeData deviceItemRealTimeData : rtDataList) {
            if(StringUtils.isNotNull(deviceItemRealTimeData)){
                rtDataMap.put(deviceItemRealTimeData.getId(),deviceItemRealTimeData);
            }
        }
        return rtDataMap;
    }

    public  Map<Integer, Map<String, List<DeviceSub>>> getSubByDeviceCode(String deviceCode){
        Map<Integer, Map<String, List<DeviceSub>>> data = new HashMap<>();
        Map<String,List<DeviceSub>> code2Sub = deviceSubCache.getSubsByCodes(Arrays.asList(deviceCode));
        if(StringUtils.isEmpty(code2Sub)){
            return data;
        }

        Map<String,  List<DeviceSub>> model2Subs;
        List<DeviceSub> deviceSubs;
        List<DeviceSub> subs = code2Sub.get(deviceCode);
        for(DeviceSub sub : subs) {
            Integer comId = sub.getComid();
            String modelName = sub.getMname();
            if(data.containsKey(comId)){
                deviceSubs = data.get(comId).containsKey(modelName)?data.get(comId)
                        .get(modelName):new ArrayList<>();
                deviceSubs.add(sub);
                data.get(comId).put(modelName, deviceSubs);
            }else {
                deviceSubs = new ArrayList<>();
                deviceSubs.add(sub);
                model2Subs = new HashMap<>();
                model2Subs.put(modelName, deviceSubs);
                data.put(comId, model2Subs);
            }
        }
        return data;
    }
}
