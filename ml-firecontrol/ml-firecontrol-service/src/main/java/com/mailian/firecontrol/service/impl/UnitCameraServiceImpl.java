package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.UnitCameraMapper;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.auto.model.UnitCamera;
import com.mailian.firecontrol.dto.web.CameraInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.CameraListResp;
import com.mailian.firecontrol.service.UnitCameraService;
import com.mailian.firecontrol.service.UnitService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnitCameraServiceImpl extends BaseServiceImpl<UnitCamera, UnitCameraMapper> implements UnitCameraService {

    @Resource
    private UnitService unitService;

    @Override
    public Boolean insertOrUpdate(CameraInfo cameraInfo){
        UnitCamera unitCamera = new UnitCamera();
        BeanUtils.copyProperties(cameraInfo,unitCamera);
        Unit unit = unitService.selectByPrimaryKey(cameraInfo.getUnitId());
        if(StringUtils.isNotNull(unit)){
            unitCamera.setPrecinctId(unit.getPrecinctId());
        }

        if(StringUtils.isEmpty(cameraInfo.getId())){
            return super.insert(unitCamera) > 0;
        }else{
            return super.updateByPrimaryKeySelective(unitCamera) > 0;
        }
    }

    @Override
    public List<CameraListResp> getCameraList(DataScope dataScope, SearchReq searchReq){
        String unitName = searchReq.getUnitName();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitName",unitName);
        }
        List<UnitCamera> unitCameras = super.selectByMap(queryMap);
        if(StringUtils.isEmpty(unitCameras)){
            return new ArrayList<>();
        }

        //查找单位信息
        Set<Integer> unitIds = new HashSet<>();
        for(UnitCamera unitCamera : unitCameras){
            unitIds.add(unitCamera.getUnitId());
        }
        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        for(Unit unit : units){
            unitId2Name.put(unit.getId(),unit.getUnitName());
        }

        List<CameraListResp> cameraListResps = new ArrayList<>();
        CameraListResp cameraListResp;
        for(UnitCamera unitCamera : unitCameras){
            cameraListResp = new CameraListResp();
            BeanUtils.copyProperties(unitCamera,cameraListResp);
            cameraListResp.setUnitName(unitId2Name.get(unitCamera.getUnitId()));
            cameraListResps.add(cameraListResp);
        }
        return cameraListResps;
    }


}