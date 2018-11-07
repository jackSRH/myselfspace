package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.BasePage;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.app.response.AppUnitDetailResp;
import com.mailian.firecontrol.dto.app.response.AppUnitResp;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.AreaUnitMapResp;
import com.mailian.firecontrol.dto.web.response.DeviceResp;
import com.mailian.firecontrol.dto.web.response.PieResp;
import com.mailian.firecontrol.dto.web.response.UnitListResp;
import com.mailian.firecontrol.dto.web.response.UnitSwitchResp;

import java.util.List;

public interface UnitService extends BaseService<Unit> {

    /**
     * 查找单位列表
     * @param dataScope 管辖区id列表
     * @param searchReq
     * @return
     */
    PageBean<UnitListResp> getUnitList(DataScope dataScope,SearchReq searchReq);

    /**
     * 新增或者更新单位信息
     * @param unitInfo
     * @return
     */
    Boolean insertOrUpdate(UnitInfo unitInfo);

    /**
     * 获取未分配的设备列表
     * @return
     * @param unitId
     */
    List<DeviceResp> getUnallotDevice(Integer unitId);

    /**
     * 获取单位分布
     * @param areaId
     * @return
     */
    PieResp getUnitSpreadByAreaAndScope(Integer areaId,DataScope dataScope);

    /**
     * 获取单位地图统计信息
     * @param areaId
     * @param unitType
     * @param dataScope
     * @return
     */
    AreaUnitMapResp getUnitMapDataByAreaAndScope(Integer areaId, Integer unitType, DataScope dataScope);

    /**
     * 根据名称筛选
     * @param name
     * @return
     */
    List<AppUnitResp> selectByNameAndPageScope(String name, BasePage basePage,DataScope dataScope);

    /**
     * 获取app单位详情
     * @param unitId
     * @return
     */
    AppUnitDetailResp getAppUnitDetailById(Integer unitId);

    /**
     * 获取单位开关列表
     * @param dataScope 管辖区id列表
     * @param searchReq
     * @return
     */
    PageBean<List<UnitSwitchResp>> getUnitSwitchList(DataScope dataScope,SearchReq searchReq);

}
