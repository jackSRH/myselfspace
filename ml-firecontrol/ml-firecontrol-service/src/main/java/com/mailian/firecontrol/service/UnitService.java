package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.DeviceResp;
import com.mailian.firecontrol.dto.web.response.PieResp;
import com.mailian.firecontrol.dto.web.response.UnitListResp;

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
     */
    List<DeviceResp> getUnallotDevice();

    /**
     * 获取单位分布
     * @param areaId
     * @return
     */
    PieResp getUnitSpreadByAreaAndScope(Integer areaId,DataScope dataScope);
}
