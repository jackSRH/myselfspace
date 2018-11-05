package com.mailian.firecontrol.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.BooleanEnum;
import com.mailian.core.enums.Status;
import com.mailian.core.util.*;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.*;
import com.mailian.firecontrol.common.util.SmsUtils;
import com.mailian.firecontrol.dao.auto.model.DiagramStruct;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.SysConfig;
import com.mailian.firecontrol.dao.manual.ManageManualMapper;
import com.mailian.firecontrol.dto.UnitRedisInfo;
import com.mailian.firecontrol.dto.app.NoticeInfo;
import com.mailian.firecontrol.dto.push.Alarm;
import com.mailian.firecontrol.dto.push.Device;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.service.AlarmOpertionService;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.SysConfigService;
import com.mailian.firecontrol.service.cache.DeviceCache;
import com.mailian.firecontrol.service.cache.NoticeCache;
import com.mailian.firecontrol.service.cache.UnitDeviceCache;
import com.mailian.firecontrol.service.repository.AlarmRepository;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@Service
public class AlarmOpertionServiceImpl implements AlarmOpertionService {
    private static final Logger log = LoggerFactory.getLogger(AlarmOpertionServiceImpl.class);

    @Autowired
    private UnitDeviceCache unitDeviceCache;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private NoticeCache noticeCache;
    @Autowired
    private FacilitiesAlarmService facilitiesAlarmService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private DeviceItemRepository deviceItemRepository;
    @Autowired
    private ExecutorService executorService;
    @Resource
    private ManageManualMapper manageManualMapper;
    @Autowired
    private AlarmRepository alarmRepository;

    private Template template;
    private static final String FACILITIESNAME = "facilitiesName";
    private static final String UNITNAME = "unitName";
    private static Date lastSynTime = null;

    @PostConstruct
    public void init() {
        SysConfig sysConfig = sysConfigService.getConfigByType(SysConfigType.ALARM_INFO);
        template = FreeMarkerUtil.getStrTemplate(CommonConstant.ALARM_INFO,sysConfig.getConfigValue());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dealRealTimeAlarm(List<Alarm> alarms){
        List<String> deviceIdList = new ArrayList<>();
        Map<Integer,Alarm> alarmMap = new HashMap<>();
        String gwid;
        for(Alarm alarm : alarms) {
            gwid = alarm.getGwid();
            if(StringUtils.isNotEmpty(gwid)) {
                deviceIdList.add(gwid);
            }

            alarmMap.put(alarm.getAlarmid(),alarm);
        }
        /*清除告警list*/
        alarms.clear();

        // 根据告警找到对应单位
        Map<String,UnitRedisInfo> deviceUnitMap = unitDeviceCache.getUnitMapByDeviceIds(deviceIdList);
        if(StringUtils.isEmpty(deviceUnitMap)){
            log.error("网关{},没有关联单位。",deviceIdList);
        }

        /* 获取已存在的告警列表 */
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("alarmScope",new DataScope("alarm_id",CollectionUtil.newArrayList(alarmMap.keySet())));
        List<FacilitiesAlarm> facilitiesAlarmList = facilitiesAlarmService.selectByMap(queryMap);

        List<Alarm> upAlarmList = new ArrayList<>();
        Map<Integer,FacilitiesAlarm> facilitiesAlarmMap = new HashMap<>();
        if(StringUtils.isNotEmpty(facilitiesAlarmList)) {
            Alarm alarm;
            for (FacilitiesAlarm facilitiesAlarm : facilitiesAlarmList) {
                alarm = alarmMap.get(facilitiesAlarm.getAlarmId());
                if(!alarm.getHashCode().equals(facilitiesAlarm.getHashCode())){
                    upAlarmList.add(alarm);
                    facilitiesAlarmMap.put(alarm.getAlarmid(),facilitiesAlarm);
                }
                alarmMap.remove(alarm);
            }
        }
        facilitiesAlarmList.clear();

        Collection<Alarm> addAlarmList = alarmMap.values();
        List<FacilitiesAlarm> addFacilitiesAlarmList = new ArrayList<>();
        List<FacilitiesAlarm> upFacilitiesAlarmList = new ArrayList<>();
        /* 处理新增告警 */
        if(StringUtils.isNotEmpty(addAlarmList)) {
            UnitRedisInfo unitRedisInfo;
            for (Alarm alarm : addAlarmList) {
                /* 获取对应单位 */
                unitRedisInfo = deviceUnitMap.get(alarm.getGwid());
                if(StringUtils.isNull(unitRedisInfo)){
                    continue;
                }

                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put(UNITNAME,unitRedisInfo.getUnitName());

                String itemId = alarm.getId();
                String etime = alarm.getEtime().compareTo(alarm.getStime()) < 0?"":alarm.getEtime();
                AlarmStatus alarmStatus = StringUtils.isEmpty(etime)?AlarmStatus.ALARMING:AlarmStatus.ALARMED;
                AlarmLevel alarmLevel = AlarmLevel.getAlarmLevel(alarm.getLevel());
                String alarmItemName = "";
                String eqAlarmContent = "";
                StringBuffer alarmContent = new StringBuffer();
                Integer btype = 0;
                boolean sendSms = false;
                boolean sendEmail = false;

                /* 获取数据项 */
                if(!itemId.contains("dat") && !itemId.contains("ysyc")) {
                    log.warn("告警itemid{}，非数据项告警",itemId);
                    continue;
                }
                DeviceItem deviceItem = deviceItemRepository.getDeviceItemInfosByItemId(itemId);
                if(StringUtils.isNotNull(deviceItem)){
                    alarmItemName = StringUtils.nvl(deviceItem.getShortname(),"");
                    btype = deviceItem.getBtype();

                    paramMap.put(FACILITIESNAME,alarmItemName);
                    eqAlarmContent = FreeMarkerUtil.genStrByTemplate(template,paramMap);

                    //推送消息到第三方
                    if(StringUtils.isNotEmpty(deviceItem.getAlarmurl())){
                        notify(alarmStatus.value,unitRedisInfo.getUnitName(),itemId,alarmItemName,eqAlarmContent,deviceItem.getAlarmurl());
                    }
                    if(BooleanEnum.YES.id.equals(deviceItem.getAlarmsms())) {
                        sendSms = true;
                    }
                    if(BooleanEnum.YES.id.equals(deviceItem.getAlarmemail())) {
                        sendEmail = true;
                    }
                }

                if(btype.intValue() == ItemBtype.NOATTRIBUTE.id.intValue()){
                    log.warn("告警itemid{}，非烟感告警或漏电预警",itemId);
                    continue;
                }
                ItemBtype itemBtype = ItemBtype.getItemBtype(btype);
                if(StringUtils.isNull(itemBtype)){
                    log.warn("告警itemid{}，非烟感告警或漏电预警",itemId);
                    continue;
                }

                AlarmType alarmType = AlarmType.EARLY_WARNING;
                if(itemBtype.equals(ItemBtype.SMOKE)){
                    alarmType = AlarmType.ALARM;
                }

                DiagramStruct diagramStruct = manageManualMapper.selectDiagramStructByAlarmItemId(itemId,unitRedisInfo.getId());

                FacilitiesAlarm facilitiesAlarm = new FacilitiesAlarm();
                facilitiesAlarm.setUnitId(unitRedisInfo.getId());
                facilitiesAlarm.setStructAddress(diagramStruct.getStructAddress());
                facilitiesAlarm.setFacilitiesId(diagramStruct.getFacilitiesId());
                facilitiesAlarm.setAlarmId(alarm.getAlarmid());
                facilitiesAlarm.setAlarmItemId(itemId);
                facilitiesAlarm.setAlarmContent(alarmItemName+alarmType.desc);
                facilitiesAlarm.setAlarmLevel(alarmLevel.id);
                facilitiesAlarm.setAlarmStatus(alarmStatus.id);
                facilitiesAlarm.setAlarmType(alarmType.id);
                facilitiesAlarm.setAlarmWay(AlarmWay.AUTO.id);

                Date alarmSTime = ThreadLocalDateUtil.parse(alarm.getStime());
                facilitiesAlarm.setAlarmTime(alarmSTime);

                if(StringUtils.isNotEmpty(etime)){
                    facilitiesAlarm.setAlarmEndTime(ThreadLocalDateUtil.parse(etime));
                    facilitiesAlarm.setHandleStatus(AlarmHandleStatus.COMPLETED.id);
                    facilitiesAlarm.setHandleTime(new Date());
                }else{
                    facilitiesAlarm.setHandleStatus(AlarmHandleStatus.UNTREATED.id);
                    /* 处理发邮件短信 */
                    //发送短信
                    dealSmsAndEmail(unitRedisInfo, alarm, alarmItemName, eqAlarmContent, sendSms, sendEmail);
                    //推送或者删除app消息
                    pushAlarmInfoToApp(alarm.getAlarmid(), alarmSTime, eqAlarmContent, alarm.getEtime(), unitRedisInfo.getPrecinctId());
                }

                facilitiesAlarm.setStatus(Status.NORMAL.id);
                facilitiesAlarm.setCreateBy("system");
                facilitiesAlarm.setCreateTime(new Date());
                facilitiesAlarm.setUpdateBy("system");
                facilitiesAlarm.setUpdateTime(new Date());
                addFacilitiesAlarmList.add(facilitiesAlarm);
            }
        }

        /*处理修改报警*/
        if(StringUtils.isNotEmpty(upAlarmList)){
            FacilitiesAlarm upFacilitiesAlarm;
            UnitRedisInfo unitRedisInfo;
            for (Alarm alarm : upAlarmList) {
                FacilitiesAlarm facilitiesAlarm = facilitiesAlarmMap.get(alarm.getAlarmid());
                String etime = alarm.getEtime().compareTo(alarm.getStime()) < 0?"":alarm.getEtime();
                AlarmStatus alarmStatus = StringUtils.isEmpty(etime)?AlarmStatus.ALARMING:AlarmStatus.ALARMED;
                if(StringUtils.isNotEmpty(etime) && StringUtils.isNull(facilitiesAlarm.getAlarmEndTime())){
                    upFacilitiesAlarm = new FacilitiesAlarm();
                    upFacilitiesAlarm.setId(facilitiesAlarm.getId());
                    upFacilitiesAlarm.setAlarmStatus(AlarmStatus.ALARMED.id);
                    if(AlarmHandleStatus.UNTREATED.id.equals(facilitiesAlarm.getHandleStatus())){
                        upFacilitiesAlarm.setHandleStatus(AlarmHandleStatus.COMPLETED.id);
                    }
                    upFacilitiesAlarm.setAlarmEndTime(ThreadLocalDateUtil.parse(etime));
                    upFacilitiesAlarm.setUpdateTime(new Date());
                    upFacilitiesAlarm.setUpdateBy("system");
                    upFacilitiesAlarmList.add(upFacilitiesAlarm);

                    /* 获取对应单位 */
                    unitRedisInfo = deviceUnitMap.get(alarm.getGwid());
                    if(StringUtils.isNull(unitRedisInfo)){
                        continue;
                    }

                    Map<String,Object> paramMap = new HashMap<>();
                    paramMap.put(UNITNAME,unitRedisInfo.getUnitName());
                    String itemId = alarm.getId();
                    String alarmItemName = "";
                    String eqAlarmContent = "";
                    boolean sendSms = false;
                    boolean sendEmail = false;
                    DeviceItem deviceItem = deviceItemRepository.getDeviceItemInfosByItemId(itemId);
                    if(StringUtils.isNotNull(deviceItem)) {
                        alarmItemName = StringUtils.nvl(deviceItem.getShortname(), "");
                        paramMap.put(FACILITIESNAME, alarmItemName);
                        eqAlarmContent = FreeMarkerUtil.genStrByTemplate(template, paramMap);
                        //推送消息到第三方
                        if(StringUtils.isNotEmpty(deviceItem.getAlarmurl())){
                            notify(alarmStatus.value,unitRedisInfo.getUnitName(),itemId,alarmItemName,eqAlarmContent,deviceItem.getAlarmurl());
                        }
                        if(BooleanEnum.YES.id.equals(deviceItem.getAlarmsms())) {
                            sendSms = true;
                        }
                        if(BooleanEnum.YES.id.equals(deviceItem.getAlarmemail())) {
                            sendEmail = true;
                        }
                    }
                    /* 处理发邮件短信 */
                    dealSmsAndEmail(unitRedisInfo, alarm, alarmItemName, eqAlarmContent, sendSms, sendEmail);

                    //推送或者删除app消息
                    pushAlarmInfoToApp(alarm.getAlarmid(), facilitiesAlarm.getAlarmTime(), eqAlarmContent, alarm.getEtime(), unitRedisInfo.getPrecinctId());
                }
            }
        }

        if(StringUtils.isNotEmpty(addFacilitiesAlarmList)){
            facilitiesAlarmService.insertBatch(addFacilitiesAlarmList);
        }
        if(StringUtils.isNotEmpty(upFacilitiesAlarmList)){
            manageManualMapper.updateFaAlarmBatch(upFacilitiesAlarmList);
        }

    }

    @Override
    public void completionEquipmentAlarm() {
        /*获取关联的网关*/
        List<String> deviceList = unitDeviceCache.getDevices();

        //设置开始结束时间
        Date now = new Date();
        String etime;
        String stime;
        if(StringUtils.isNull(lastSynTime)){
            etime = ThreadLocalDateUtil.formatDate(now);
            stime = ThreadLocalDateUtil.formatDate(DateUtil.getDateAfterHour(now,new BigDecimal(-1)));
        }else{
            stime = ThreadLocalDateUtil.formatDate(DateUtil.addMinute(lastSynTime,-5));
            etime = ThreadLocalDateUtil.formatDate(now);
        }

        //查找后台告警信息
        List<Alarm> alarms = alarmRepository.getAlarmInfoByDiviceCodesAndType(
                CollectionUtil.join(deviceList,","),stime,etime,0,2);
        AlarmOpertionService alarmOpertionService = (AlarmOpertionService) AopContext.currentProxy();
        alarmOpertionService.dealRealTimeAlarm(alarms);

        lastSynTime = now;
    }

    /**
     * 处理短信邮件发送
     * @param unitRedisInfo
     * @param alarm
     * @param alarmItemName
     * @param eqAlarmContent
     * @param sendSms
     * @param sendEmail
     */
    private void dealSmsAndEmail(UnitRedisInfo unitRedisInfo, Alarm alarm, String alarmItemName, String eqAlarmContent, boolean sendSms, boolean sendEmail) {
        //发送短信
        if (sendSms) {
            String dateStr = StringUtils.isEmpty(alarm.getEtime())?alarm.getStime():alarm.getEtime();
            Device device = deviceCache.getDeviceByCode(alarm.getGwid());
            String deviceName = "";
            if (StringUtils.isNotNull(device)) {
                deviceName = device.getName();
            }
            sendAlarmInfoSms(unitRedisInfo.getContactPhone(), deviceName, eqAlarmContent, dateStr, alarmItemName);
        }

        //发送告警邮件(暂不实现)
        if (sendEmail) {
            /*List<String> emails = userService.selectEmailBySid(stationVo.getId());
            sendAlarmInfoEmail(emails, alarmInfo);*/
        }
    }


    //根据url推送信息
    private void notify(String status,String stationName,String itemSubName,
                        String itemName,String msg,final String url){
        final Map<String,Object> datas = new HashMap<String,Object>();
        datas.put("status", status);
        datas.put("stationName", stationName);
        datas.put("itemSubName", itemSubName);
        datas.put("itemName", itemName);
        datas.put("msg", msg);
        datas.put("date", new Date());
        executorService.execute(new Runnable(){
            @Override
            public void run() {
                try{
                    HttpClientUtil.postBody(url, datas, null);
                }catch(Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    /* 发送短信 */
    private void sendAlarmInfoSms(final String phone,final String deviceName,final String alarmInfo,final String date,final String equipmentName){
        if(StringUtils.isNotEmpty(phone)) {
            Matcher matcher = CommonConstant.phonePattern.matcher(phone);
            if (matcher.find()) {
                executorService.execute(new Runnable(){
                    @Override
                    public void run() {
                        int blue = SmsUtils.sendSms(phone, deviceName, alarmInfo, date, equipmentName);
                        if (blue != 0)
                        {
                            SmsUtils.sendSms(phone, deviceName, alarmInfo, date, equipmentName);
                        }
                    }
                });
            }
        }
    }

    /*  推送至APP */
    private void pushAlarmInfoToApp(Integer alarmId,Date alarmStime,String alarmInfo,String alarmEtime,Integer precinctId) {
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setId(alarmId);
        noticeInfo.setTime(alarmStime);
        noticeInfo.setTitle("物联告警");
        noticeInfo.setType(NoticeType.ALARM.id);
        noticeInfo.setStatus(Status.NORMAL.id);
        noticeInfo.setRealType(RealNoticeType.ALARM_NOTICE.id);

        //告警新增通知，告警结束删除通知
        if(StringUtils.isEmpty(alarmEtime)){
            noticeInfo.setContent(alarmInfo);
            noticeInfo.setPrecinctId(precinctId);
            noticeInfo.setRead(BooleanEnum.NO.id);
            noticeCache.addNotice(noticeInfo);
        }else{
            noticeCache.removeNotice(noticeInfo);
        }
    }

}
