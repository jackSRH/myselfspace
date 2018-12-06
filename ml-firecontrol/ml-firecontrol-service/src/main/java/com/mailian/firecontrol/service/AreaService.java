package com.mailian.firecontrol.service;

import com.mailian.core.base.service.BaseService;
import com.mailian.core.db.DataScope;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dto.web.response.AreaResp;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
public interface AreaService extends BaseService<Area> {

    /**
     * 获取所有省份城市区域
     * @return
     */
    List<AreaResp> selectAll();

    /**
     * 过滤后的省份城市
     * @param areaName
     * @return
     */
    List<AreaResp> selectProvinceAndCityList(String areaName);

    /**
     * 根据区域id 获取区域信息
     * @param areaId
     * @return
     */
    Area getAreaById(Integer areaId);

    /**
     * 管辖区列表
     * @param areaName
     * @return
     */
    List<AreaResp> selectAreaAndPrecinctList(String areaName, DataScope dataScope);

    /**
     * 单位树形结构
     * @param areaName
     * @param dataScope
     * @return
     */
    List<AreaResp> selectAreaAndUnitList(String areaName, DataScope dataScope,Integer showRank);
}
