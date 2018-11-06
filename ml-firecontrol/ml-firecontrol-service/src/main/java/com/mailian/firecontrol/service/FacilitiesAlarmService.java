package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAlarmListResp;
import com.mailian.firecontrol.dto.web.response.FireAutoAlarmListResp;

import java.util.List;
import java.util.Map;

public interface FacilitiesAlarmService extends BaseService<FacilitiesAlarm> {

    /**
     * 查找告警受理列表
     * @param dataScope
     * @param searchReq
     * @return
     */
    PageBean<FacilitiesAlarmListResp> getFacilitiesAlarmList(DataScope dataScope, SearchReq searchReq);


    /**
     * 查找非误告火灾告警列表
     * @param dataScope
     * @param searchReq
     * @return
     */
    PageBean<FireAlarmListResp> getFireAlarmList(DataScope dataScope, SearchReq searchReq);

    /**
     * 查找所有火灾告警列表
     * @param dataScope
     * @param searchReq
     * @return
     */
    PageBean<FireAutoAlarmListResp> getFireAutoAlarmList(DataScope dataScope, SearchReq searchReq);

    /**
     * 查找告警列表
     * @param queryMap
     * @return
     */
    List<FacilitiesAlarm> selectFacilitiesAlarmByMap(Map<String,Object> queryMap);

}
