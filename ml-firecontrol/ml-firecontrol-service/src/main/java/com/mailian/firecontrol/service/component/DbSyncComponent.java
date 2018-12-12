package com.mailian.firecontrol.service.component;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.manual.mapper.SystemManualMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/12/12
 * @Description:
 */
@Component
public class DbSyncComponent {
    @Resource
    private SystemManualMapper systemManualMapper;
    @Resource
    private PrecinctMapper precinctMapper;

    @Async
    public void syncPrecinctId(Integer unitId,Integer precinctId){
        systemManualMapper.updateFacilitiesPrecinctIdByUnitId(precinctId,unitId);
        systemManualMapper.updateStructPrecinctIdByUnitId(precinctId,unitId);
        Precinct precinct = precinctMapper.selectByPrimaryKey(precinctId);
        if(StringUtils.isNotNull(precinct)) {
            FacilitiesAlarm facilitiesAlarm = new FacilitiesAlarm();
            facilitiesAlarm.setUnitId(unitId);
            facilitiesAlarm.setPrecinctId(precinctId);
            facilitiesAlarm.setAreaId(precinct.getAreaId());
            facilitiesAlarm.setProvinceId(precinct.getProvinceId());
            facilitiesAlarm.setCityId(precinct.getCityId());
            systemManualMapper.updateAlarmInfoByUnitId(facilitiesAlarm);
        }
        systemManualMapper.updateCameraPrecinctIdByUnitId(precinctId,unitId);
        systemManualMapper.updatePatrolPrecinctIdByUnitId(precinctId,unitId);
    }

    @Async
    public void syncUnitType(Integer unitId, Integer unitType) {
        FacilitiesAlarm facilitiesAlarm = new FacilitiesAlarm();
        facilitiesAlarm.setUnitId(unitId);
        facilitiesAlarm.setUnitType(unitType);
        systemManualMapper.updateAlarmInfoByUnitId(facilitiesAlarm);
    }

    @Async
    public void syncPrecinctArea(Precinct precinct) {
        FacilitiesAlarm facilitiesAlarm = new FacilitiesAlarm();
        facilitiesAlarm.setPrecinctId(precinct.getId());
        facilitiesAlarm.setAreaId(precinct.getAreaId());
        facilitiesAlarm.setProvinceId(precinct.getProvinceId());
        facilitiesAlarm.setCityId(precinct.getCityId());
        systemManualMapper.updateAlarmInfoByPrecinctId(facilitiesAlarm);
    }
}
