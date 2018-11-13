package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.FacilitiesAlarm;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.*;

import java.util.Date;
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
     * 警情信息统计
     * @param areaId
     * @param dataScope
     * @return
     */
    AlarmNumResp getAlarmNumByAreaAndScope(Integer areaId, DataScope dataScope);
    /**
     * 查找告警列表
     * @param queryMap
     * @return
     */
    List<FacilitiesAlarm> selectFacilitiesAlarmByMap(Map<String,Object> queryMap);

    /**
     * 警情分析
     * @param areaId
     * @param dataScope
     * @return
     */
    AlarmAnalysisResp getAlarmAnalysisByAreaAndScope(Integer areaId,Integer dateType,Integer misreport, DataScope dataScope);

    /**
     * 火灾统计
     * @param areaId
     * @return
     */
    FireAlarmCountResp getFireAlarmCountByArea(Integer areaId);

    /**
     * 告警统计
     * @param unitId
     * @return
     */
    Map<Integer,Integer> countAlarmNumByUnitId(Integer unitId);

    /**
     * 获取行业告警占比
     * @param alarmType
     * @return
     */
    List<AlarmIndustryShareResp> getAlarmIndustryShare(Integer areaId,DataScope dataScope,Integer alarmType);

    /**
     * 获取告警趋势
     * @param alarmType
     * @return
     */
    List<AlarmStatisticsResp> getAlarmTrend(Integer areaId,DataScope dataScope,Integer alarmType);

    List<CurAlarmResp> getCurAlarm(Integer areaId,DataScope dataScope);

    /**
     * 报警误报
     * @param uid
     * @param userName
     * @param roleName
     * @param alarmId
     * @return
     */
    int misreportAlarm(Integer uid,String userName,String roleName, Integer alarmId);

    /**
     * 有效报警
     * @param uid
     * @param userName
     * @param dutyName
     * @param roleName
     * @param alarmId
     * @param isComplete
     * @return
     */
    int effectiveAlarm(Integer uid, String userName, String dutyName, String roleName, Integer alarmId, boolean isComplete,Date handleEndTime,String handleResult);

    /**
     * 完成告警
     * @param uid
     * @param userName
     * @param roleName
     * @param alarmId
     * @param handleEndTime
     * @param handleResult
     * @return
     */
    int completeAlarm(Integer uid, String userName, String roleName,Integer alarmId,Date handleEndTime,String handleResult);
}
