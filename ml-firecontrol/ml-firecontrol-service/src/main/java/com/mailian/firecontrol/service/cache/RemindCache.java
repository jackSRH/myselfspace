package com.mailian.firecontrol.service.cache;

import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.AreaRank;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dto.AlarmRemindInfo;
import com.mailian.firecontrol.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/15
 * @Description:
 */
@Component
public class RemindCache {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private AreaService areaService;

    //提醒失效时间10s
    private Integer EXPIRE_TIME = 10;

    /**
     * 添加提醒
     * @param alarmRemindInfo
     */
    @Async
    public void addRemind(AlarmRemindInfo alarmRemindInfo){
        redisUtils.leftPush(CommonConstant.ALARM_REMIND,alarmRemindInfo,EXPIRE_TIME);
    }

    /**
     * 添加多个提醒
     * @param alarmRemindInfos
     */
    @Async
    public void addReminds(List<AlarmRemindInfo> alarmRemindInfos){
        redisUtils.leftPushAll(CommonConstant.ALARM_REMIND,alarmRemindInfos,EXPIRE_TIME);
    }

    /**
     * 移除提醒
     * @param alarmId
     */
    @Async
    public void removeRemind(Integer alarmId){
        List<Object> objects = redisUtils.listRange(CommonConstant.ALARM_REMIND,0,-1);
        if(StringUtils.isNotEmpty(objects)) {
            for (int i = 0; i < objects.size(); i++) {
                AlarmRemindInfo alarmRemindInfo = (AlarmRemindInfo) objects.get(i);
                if(alarmRemindInfo.getAlarmId().equals(alarmId)){
                    redisUtils.deleteListValue(CommonConstant.ALARM_REMIND, 1, alarmRemindInfo);
                    return;
                }
            }
        }
    }

    /**
     * 获取最新提醒(根据区域)
     */
    public AlarmRemindInfo getFristRemindByAreaId(Integer areaId,List<Integer> precinctIds){
        List<Object> objects = redisUtils.listRange(CommonConstant.ALARM_REMIND,0,-1);

        AlarmRemindInfo result = null;
        if(StringUtils.isNotEmpty(objects)) {
            if(StringUtils.isNull(areaId)) {
                //result = (AlarmRemindInfo) objects.get(0);
                if(StringUtils.isNotNull(precinctIds)){
                    for (Object object : objects) {
                        AlarmRemindInfo alarmRemindInfo = (AlarmRemindInfo) object;
                        if(precinctIds.contains(alarmRemindInfo.getPrecinctId())){
                            result = alarmRemindInfo;
                            break;
                        }
                    }
                }else{
                    result = (AlarmRemindInfo) objects.get(0);
                }
            }else {
                Area area = areaService.getAreaById(areaId);
                Integer areaRank = area.getAreaRank();
                for (Object object : objects) {
                    AlarmRemindInfo alarmRemindInfo = (AlarmRemindInfo) object;

                    if (AreaRank.PROVINCE.id.equals(areaRank) && areaId.equals(alarmRemindInfo.getProvinceId())) {
                        if(StringUtils.isNotNull(precinctIds)){
                            if(precinctIds.contains(alarmRemindInfo.getPrecinctId())){
                                result = alarmRemindInfo;
                                break;
                            }
                        }else {
                            result = alarmRemindInfo;
                            break;
                        }
                    } else if (AreaRank.CITY.id.equals(areaRank) && areaId.equals(alarmRemindInfo.getCityId())) {
                        if(StringUtils.isNotNull(precinctIds)){
                            if(precinctIds.contains(alarmRemindInfo.getPrecinctId())){
                                result = alarmRemindInfo;
                                break;
                            }
                        }else {
                            result = alarmRemindInfo;
                            break;
                        }
                    } else {
                        if(StringUtils.isNotNull(precinctIds)){
                            if(precinctIds.contains(alarmRemindInfo.getPrecinctId())){
                                result = alarmRemindInfo;
                                break;
                            }
                        }else {
                            result = alarmRemindInfo;
                            break;
                        }
                    }
                }
            }
        }
        if(StringUtils.isNotNull(result)){
            removeRemind(result.getAlarmId());
        }
        return result;
    }

    /**
     * 获取最新提醒(根据管辖区)
     */
    public AlarmRemindInfo getFristRemindByPrecinct(List<Integer> precinctIds){
        List<Object> objects = redisUtils.listRange(CommonConstant.ALARM_REMIND,0,-1);
        AlarmRemindInfo result = null;
        if(StringUtils.isNotEmpty(objects)) {
            for (Object object : objects) {
                AlarmRemindInfo alarmRemindInfo = (AlarmRemindInfo) object;

                if(precinctIds.contains(alarmRemindInfo.getPrecinctId())){
                    result = alarmRemindInfo;
                    break;
                }
            }
        }
        if(StringUtils.isNotNull(result)){
            removeRemind(result.getAlarmId());
        }
        return result;
    }

    /**
     * 获取最新提醒(根据管辖区)
     */
    public AlarmRemindInfo getFristRemindByUnit(Integer unitId){
        List<Object> objects = redisUtils.listRange(CommonConstant.ALARM_REMIND,0,-1);
        AlarmRemindInfo result = null;
        if(StringUtils.isNotEmpty(objects)) {
            for (Object object : objects) {
                AlarmRemindInfo alarmRemindInfo = (AlarmRemindInfo) object;

                if(StringUtils.isNotEmpty(unitId) && alarmRemindInfo.getUnitId().equals(unitId)){
                    result = alarmRemindInfo;
                    break;
                }
            }
        }
        if(StringUtils.isNotNull(result)){
            removeRemind(result.getAlarmId());
        }
        return result;
    }

}
