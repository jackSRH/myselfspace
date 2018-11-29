package com.mailian.firecontrol.service.impl;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.ItemStype;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dto.SelectDto;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemRealTimeData;
import com.mailian.firecontrol.dto.push.DeviceSub;
import com.mailian.firecontrol.dto.push.YcCalc;
import com.mailian.firecontrol.dto.push.YcStore;
import com.mailian.firecontrol.dto.push.YcTran;
import com.mailian.firecontrol.dto.push.YkTran;
import com.mailian.firecontrol.dto.push.YxCalc;
import com.mailian.firecontrol.dto.push.YxTran;
import com.mailian.firecontrol.dto.web.request.DeviceItemInfo;
import com.mailian.firecontrol.dto.web.request.YcCalcInfo;
import com.mailian.firecontrol.dto.web.request.YcStoreInfo;
import com.mailian.firecontrol.dto.web.request.YcTranInfo;
import com.mailian.firecontrol.dto.web.request.YkTranInfo;
import com.mailian.firecontrol.dto.web.request.YxCalcInfo;
import com.mailian.firecontrol.dto.web.request.YxTranInfo;
import com.mailian.firecontrol.dto.web.response.DeviceConfigItemResp;
import com.mailian.firecontrol.service.DeviceItemOpertionService;
import com.mailian.firecontrol.service.UnitDeviceService;
import com.mailian.firecontrol.service.cache.DeviceCache;
import com.mailian.firecontrol.service.cache.DeviceSubCache;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/7
 * @Description:
 */
@Service
public class DeviceItemOpertionServiceImpl implements DeviceItemOpertionService {
    @Autowired
    private DeviceItemRepository deviceItemRepository;
    @Autowired
    private UnitDeviceService unitDeviceService;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private DeviceSubCache deviceSubCache;


    //和工程人员约定  遥控的描述结构有两种
    //1：制冷 ,1代表下发值，制冷代表状态；
    //1:2:3:制冷,1代表状态值，2代表下发值，3代表是否可控，制冷代表状态
    @Override
    public String getYaokongStatus(String yaokongItemId, String statusItemId){
        String	status = "无";
        DeviceItemRealTimeData realTimeData = deviceItemRepository.getRtDataByItemId(statusItemId);
        DeviceItem yaokongItem = deviceItemRepository.getDeviceItemInfosByItemId(yaokongItemId);
        DeviceItem statusItem = deviceItemRepository.getDeviceItemInfosByItemId(statusItemId);
        if(StringUtils.isNull(realTimeData)|| realTimeData.getVal() == CommonConstant.ITEM_INITIAL_VALUE
            || StringUtils.isNull(yaokongItem) || StringUtils.isNull(statusItem)) {
            return status;
        }

        int stype = statusItem.getStype();
        String ykDesc = yaokongItem.getDesc();
        String[] allType = ykDesc.split(";");
        for(int i = 0; i<allType.length - 1; i++) {
            String[] type = allType[i].split(":");
            if(stype == ItemStype.TRANSPORTYX.id || stype == ItemStype.OPERATIONYX.id) {
                if(type.length == 2) {
                    status = realTimeData.getVal() == 1?statusItem.getDesc1():statusItem.getDesc0();
                }else {
                    if(Integer.valueOf(type[0]) == realTimeData.getVal().intValue()) {
                        status = type[3];
                    }
                }
            }else {
                int rtData = realTimeData.getVal() < 50?0:1;
                if(type.length == 2) {
                    status = rtData == 1?"合":"分";
                }else {
                    if(Integer.valueOf(type[0]) == rtData) {
                        status = type[3];
                    }
                }
            }
        }
        return status;
    }

    @Override
    public List<SelectDto> getYaokongEnumList(String yaokongItemId){
        DeviceItem yaokongItem = deviceItemRepository.getDeviceItemInfosByItemId(yaokongItemId);
        String ykDesc = StringUtils.isNull(yaokongItem)?"":yaokongItem.getDesc();
        String[] allType = ykDesc.split(";");
        List<SelectDto> data = new ArrayList<SelectDto>();

        for(int i = 0; i<allType.length; i++) {
            SelectDto selectDto = new SelectDto();
            String[] type = allType[i].split(":");
            if(type.length == 2) {

                selectDto.setValue(Integer.valueOf(type[0]));
                selectDto.setName(type[1]);
            }else {
                selectDto.setName(type[3]);
                selectDto.setValue(Integer.valueOf(type[1]));
            }
            data.add(selectDto);
        }
        return data;
    }

    @Override
    public Map<String, Object> getConfigItemsByUnitId(Integer unitId) throws ExecutionException, InterruptedException{
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("unitId",unitId);
        List<UnitDevice> unitDevices = unitDeviceService.selectByMap(queryMap);
        if(StringUtils.isEmpty(unitDevices)){
            return data;
        }

        List<String> deviceIds = new ArrayList<>();
        for(UnitDevice unitDevice :unitDevices){
            deviceIds.add(unitDevice.getDeviceId());
        }
        Map<String, List<DeviceItem>> code2CalcImtes = deviceItemRepository.getCalcItemsByDeviceCodes(deviceIds);

        //获取计算库数据
        List<DeviceConfigItemResp> operationYaoce = new ArrayList<>();
        List<DeviceConfigItemResp> operationYaoxin = new ArrayList<>();
        DeviceConfigItemResp deviceConfigItemResp;

        if(StringUtils.isNotEmpty(code2CalcImtes)){
            for(Map.Entry<String, List<DeviceItem>> entry : code2CalcImtes.entrySet()) {
                deviceConfigItemResp = new DeviceConfigItemResp();
                for(DeviceItem calcItem : entry.getValue()) {
                    int stype = calcItem.getStype().intValue();
                    if(ItemStype.OPERATIONYC.id.intValue() == stype) {
                        deviceConfigItemResp.setId(calcItem.getId());
                        deviceConfigItemResp.setDisplayname(calcItem.getDisplayname());
                        deviceConfigItemResp.setShortname(calcItem.getShortname());
                        operationYaoce.add(deviceConfigItemResp);
                    }else if(ItemStype.OPERATIONYX.id.intValue() == stype) {
                        deviceConfigItemResp.setId(calcItem.getId());
                        deviceConfigItemResp.setShortname(calcItem.getShortname());
                        deviceConfigItemResp.setDisplayname(calcItem.getDisplayname());
                        operationYaoxin.add(deviceConfigItemResp);
                    }
                }
            }
        }
        data.put("计算遥测", operationYaoce);
        data.put("计算遥信", operationYaoxin);

        //查找网关信息
        List<Device> deviceList = deviceCache.getDevicesByCodes(deviceIds);
        Map<String,Device> deviceMap = new HashMap<>();
        for (Device device : deviceList) {
            deviceMap.put(device.getCode(),device);
        }

        //获取RTU信息
        Map<String, Map<String, List<DeviceItem>>> code2Subs2Items = deviceItemRepository.getSubsAndItemsBycodes(deviceIds);
        if(StringUtils.isEmpty(code2CalcImtes)){
            return data;
        }

        List<String> subIds = new ArrayList<>();
        for (Map<String, List<DeviceItem>> itemMap : code2Subs2Items.values()) {
            Set<String> subIdSet = itemMap.keySet();
            if(StringUtils.isNotEmpty(subIdSet)){
                subIds.addAll(subIdSet);
            }
        }
        Map<String,DeviceSub> deviceSubMap = deviceSubCache.getDeviceSubsBySubIds(subIds);
        Map<String, Map<String, List<DeviceConfigItemResp>>> sub2Types2Items = new HashMap<>();

        for(Map.Entry<String, Map<String, List<DeviceItem>>> entry : code2Subs2Items.entrySet()) {
            for(Map.Entry<String, List<DeviceItem>> en : entry.getValue().entrySet()) {
                List<DeviceConfigItemResp> transportYaoce = new ArrayList<>();
                List<DeviceConfigItemResp> transportYaoxin = new ArrayList<>();
                List<DeviceConfigItemResp> transportYaokong = new ArrayList<>();
                Map<String, List<DeviceConfigItemResp>> type2Items = new HashMap<>();

                for(DeviceItem deviceItem : en.getValue()) {
                    deviceConfigItemResp = new DeviceConfigItemResp();
                    deviceConfigItemResp.setId(deviceItem.getId());
                    deviceConfigItemResp.setDisplayname(deviceItem.getDisplayname());
                    deviceConfigItemResp.setShortname(deviceItem.getShortname());
                    int stype = deviceItem.getStype().intValue();
                    if(ItemStype.TRANSPORTYC.id.intValue() == stype) {
                        transportYaoce.add(deviceConfigItemResp);
                    }else if(ItemStype.TRANSPORTYX.id.intValue() == stype) {
                        transportYaoxin.add(deviceConfigItemResp);
                    }else if(ItemStype.TRANSPORTYK.id.intValue() == stype) {
                        transportYaokong.add(deviceConfigItemResp);
                    }
                }

                type2Items.put("传输遥测", transportYaoce);
                type2Items.put("传输遥信", transportYaoxin);
                type2Items.put("传输遥控", transportYaokong);

                DeviceSub sub = deviceSubMap.get(en.getKey());
                String subName = en.getKey();
                if(StringUtils.isNotNull(sub)  && StringUtils.isNotEmpty(sub.getRtuname())) {
                    subName = sub.getRtuname()+"("+ en.getKey() +")";
                }
                sub2Types2Items.put(subName, type2Items);
            }
            Device device = deviceMap.get(entry.getKey());
            String deviceName = entry.getKey();
            if(StringUtils.isNotNull(device) &&  StringUtils.isNotEmpty(device.getName())) {
                deviceName = device.getName() +"("+ entry.getKey() +")";
            }
            data.put(deviceName,sub2Types2Items);
        }
        return data;
    }

    @Override
    public List<DeviceItem> getAllItemsByDeviceCodes(List<String> deviceCodes){
        List<DeviceItem> items = new ArrayList<>();
        Map<String, List<DeviceItem>> code2TranItems = deviceItemRepository.getDeviceItemInfosByCodes(deviceCodes);
        if(StringUtils.isNotEmpty(code2TranItems)){
            for(Map.Entry<String, List<DeviceItem>> entry : code2TranItems.entrySet()) {
                items.addAll(entry.getValue());
            }
        }

        Map<String, List<DeviceItem>> code2CalcItems = deviceItemRepository.getCalcItemsByDeviceCodes(deviceCodes);
        if(StringUtils.isNotEmpty(code2CalcItems)){
            for(Map.Entry<String, List<DeviceItem>> entry : code2CalcItems.entrySet()) {
                items.addAll(entry.getValue());
            }
        }
        return items;
    }

    @Override
    public Map<String,  List<DeviceItem>> getType2ItemsData(List<DeviceItem> items){
        Map<String, List<DeviceItem>> type2Items = new HashMap<>();
        type2Items.put("遥测", new ArrayList<>());
        type2Items.put("遥信", new ArrayList<>());
        type2Items.put("遥调", new ArrayList<>());
        type2Items.put("遥控", new ArrayList<>());

        for(DeviceItem deviceItem : items) {
            int stype = deviceItem.getStype().intValue();
            if(ItemStype.TRANSPORTYC.id.intValue() == stype) {
                type2Items.get("遥测").add(deviceItem);
            }else if(ItemStype.TRANSPORTYX.id.intValue() == stype) {
                type2Items.get("遥信").add(deviceItem);
            }else if(ItemStype.TRANSPORTYT.id.intValue() == stype) {
                type2Items.get("遥调").add(deviceItem);
            }else if(ItemStype.TRANSPORTYK.id.intValue() == stype) {
                type2Items.get("遥控").add(deviceItem);
            }
        }
        return type2Items;
    }

    @Override
    public boolean updateYcTranInfos(List<YcTranInfo> ycTranInfos, String rtuIds){
        List<YcTran> ycTrans = new ArrayList<>();
        YcTran  ycTran;
        for(YcTranInfo ycTranInfo : ycTranInfos){
            ycTran = new YcTran();
            BeanUtils.copyProperties(ycTranInfo,ycTran);
            ycTrans.add(ycTran);
        }
        return deviceItemRepository.setYcTranInfos(ycTrans,rtuIds);
    }

    @Override
    public boolean updateYkTranInfos(List<YkTranInfo> ykTranInfos, String rtuIds){
        List<YkTran> ykTrans = new ArrayList<>();
        YkTran ykTran;
        for(YkTranInfo ykTranInfo : ykTranInfos){
            ykTran = new YkTran();
            BeanUtils.copyProperties(ykTranInfo,ykTran);
            ykTrans.add(ykTran);
        }
        return deviceItemRepository.setYkTranInfos(ykTrans,rtuIds);
    }

    @Override
    public boolean updateYxTranInfos(List<YxTranInfo> yxTranInfos, String rtuIds){
        List<YxTran> yxTrans = new ArrayList<>();
        YxTran yxTran;
        for(YxTranInfo yxTranInfo : yxTranInfos){
            yxTran = new YxTran();
            BeanUtils.copyProperties(yxTranInfo,yxTran);
            yxTrans.add(yxTran);
        }
        return deviceItemRepository.setYxTranInfos(yxTrans,rtuIds);
    }

    @Override
    public boolean updateYcStoreInfos(List<YcStoreInfo> ycStoreInfos, String deviceCodes){
        List<YcStore>  ycStores = new ArrayList<>();
        YcStore  ycStore;
        for(YcStoreInfo ycStoreInfo : ycStoreInfos){
            ycStore = new YcStore();
            BeanUtils.copyProperties(ycStoreInfo,ycStore);
            ycStores.add(ycStore);
        }
        return deviceItemRepository.setYcStoreInfos(ycStores,deviceCodes);
    }

    @Override
    public boolean updateYcCalcItem(List<YcCalcInfo> ycCalcInfos, String deviceCodes){
        List<YcCalc> ycCalcs = new ArrayList<>();
        YcCalc ycCalc;
        for(YcCalcInfo ycCalcInfo : ycCalcInfos){
            ycCalc  = new YcCalc();
            BeanUtils.copyProperties(ycCalcInfo,ycCalc);
            ycCalcs.add(ycCalc);
        }
        return deviceItemRepository.updateYcCalcItem(ycCalcs,deviceCodes);
    }

    @Override
    public boolean updateYxCalcItem(List<YxCalcInfo> yxCalcInfos, String deviceCodes){
        List<YxCalc> yxCalcs = new ArrayList<>();
        YxCalc yxCalc;
        for(YxCalcInfo yxCalcInfo : yxCalcInfos){
            yxCalc = new YxCalc();
            BeanUtils.copyProperties(yxCalcInfo,yxCalc);
            yxCalcs.add(yxCalc);
        }
        return deviceItemRepository.updateYxCalcItem(yxCalcs,deviceCodes);
    }

    @Override
    public List<DeviceItemInfo> getCalcItemsByCodes(String deviceCodes){
        Map<String, List<DeviceItem>> code2Items = deviceItemRepository.getCalcItemsByDeviceCodes(Arrays.asList(deviceCodes.split(",")));
        List<DeviceItemInfo> items = new ArrayList<>();
        if(StringUtils.isEmpty(code2Items)){
            return items;
        }

        DeviceItemInfo deviceItemInfo;
        for(Map.Entry<String, List<DeviceItem>> entry : code2Items.entrySet()) {
            for(DeviceItem item : entry.getValue()) {
                deviceItemInfo = new DeviceItemInfo();
                BeanUtils.copyProperties(item,deviceItemInfo);
                items.add(deviceItemInfo);
            }
        }

        Collections.sort(items, new Comparator<DeviceItemInfo>() {
            public int compare(DeviceItemInfo o1, DeviceItemInfo o2) {
                return o1.getCreatetime().compareTo(o2.getCreatetime());
            }
        });
        return items;
    }

    @Override
    public List<DeviceItemInfo> getStoreItemsByCodeAndStype(String deviceCode, Integer stype){
        Map<String, List<DeviceItem>> code2Items = deviceItemRepository.getDeviceItemInfosByCodes(Arrays.asList(deviceCode));
        List<DeviceItemInfo> itemInfos = new ArrayList<>();
        if(StringUtils.isEmpty(code2Items)){
            return itemInfos;
        }

        DeviceItemInfo deviceItemInfo;
        for(Map.Entry<String, List<DeviceItem>> entry : code2Items.entrySet() ){
            for(DeviceItem item : entry.getValue()){
                if(stype.intValue() == item.getStype().intValue() && item.getStore() >= 0) {
                    deviceItemInfo = new DeviceItemInfo();
                    BeanUtils.copyProperties(item,deviceItemInfo);
                    itemInfos.add(deviceItemInfo);
                }
            }
        }
        return itemInfos;
    }

    @Override
    public List<DeviceItemInfo> getAlarmItemsByCodeAndType(String deviceCode, Integer stype){
        List<DeviceItemInfo> itemInfos = new ArrayList<>();
        List<DeviceItem> items = getAllItemsByDeviceCodes(Arrays.asList(deviceCode));
        if(StringUtils.isEmpty(items)){
            return itemInfos;
        }

        DeviceItemInfo deviceItemInfo;
        for(DeviceItem item : items) {
            if(item.getPalarm().intValue() == 1 &&
                    stype.intValue() == item.getStype().intValue()) {
                deviceItemInfo = new DeviceItemInfo();
                BeanUtils.copyProperties(item,deviceItemInfo);
                itemInfos.add(deviceItemInfo);
            }
        }
        return itemInfos;
    }
}
