package com.mailian.firecontrol.api.web.controller.supervision;

import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.CameraInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.CameraListResp;
import com.mailian.firecontrol.dto.web.response.CameraNavigationResp;
import com.mailian.firecontrol.dto.web.response.UnitCameraListResp;
import com.mailian.firecontrol.service.UnitCameraService;
import com.mailian.firecontrol.service.UnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/supervision/unitcamera")
@Api(description = "监控设备相关接口")
@WebAPI
public class UnitCameraController extends BaseController {

    @Resource
    private UnitCameraService unitCameraService;
    @Resource
    private UnitService unitService;

    @Log(title = "单位监控",action = "新增更新摄像头")
    @ApiOperation(value = "新增更新摄像头", httpMethod = "POST")
    @RequestMapping(value="/insertOrUpdate",method = RequestMethod.POST)
    public ResponseResult insertOrUpdate(@RequestBody CameraInfo cameraInfo){
        if(StringUtils.isNull(cameraInfo)){
            return error("参数不能为空");
        }
        Boolean insertOrUpdateRes = unitCameraService.insertOrUpdate(cameraInfo);
        return insertOrUpdateRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "单位监控",action = "查找摄像头列表")
    @ApiOperation(value = "查找摄像头列表", httpMethod = "GET")
    @RequestMapping(value="/getCameraList",method = RequestMethod.GET)
    public ResponseResult<PageBean<UnitCameraListResp>> getCameraList(@CurUser ShiroUser shiroUser, SearchReq searchReq){
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            dataScope = new DataScope("precinct_id", shiroUser.getPrecinctIds());
        }
        List<CameraListResp> cameraListResps =unitCameraService.getCameraList(dataScope,searchReq);
        if(StringUtils.isEmpty(cameraListResps)){
            return ResponseResult.buildOkResult(new PageBean<>());
        }
        Map<Integer,List<CameraListResp>> unitId2CameraList = new HashMap<>();
        List<CameraListResp> cameraList;
        for(CameraListResp cameraListResp : cameraListResps){
            Integer unitId = cameraListResp.getUnitId();
            cameraList = unitId2CameraList.containsKey(unitId)?unitId2CameraList.get(unitId):new ArrayList<>();
            cameraList.add(cameraListResp);
            unitId2CameraList.put(unitId,cameraList);
        }

        List<UnitCameraListResp> unitCameraListResps = new ArrayList<>();
        UnitCameraListResp unitCameraListResp;
        for(Map.Entry<Integer,List<CameraListResp>> entry : unitId2CameraList.entrySet()){
            unitCameraListResp = new UnitCameraListResp();
            unitCameraListResp.setUnitId(entry.getKey());
            unitCameraListResp.setCameraLists(entry.getValue());
            unitCameraListResps.add(unitCameraListResp);
        }

        PageBean<UnitCameraListResp> pageBean = new PageBean<>(currentPage,pageSize,unitCameraListResps.size(),unitCameraListResps);
        return ResponseResult.buildOkResult(pageBean);
    }

    @Log(title = "单位监控",action = "删除摄像头")
    @ApiOperation(value = "删除摄像头", httpMethod = "DELETE")
    @RequestMapping(value="/delete/{caremaId}",method = RequestMethod.DELETE)
    public ResponseResult delete(@ApiParam(value = "摄像头",required = true) @PathVariable("caremaId") Integer caremaId){
        if(StringUtils.isEmpty(caremaId)){
            return error("摄像头id不能为空");
        }
        Boolean deleteRes = unitCameraService.deleteByPrimaryKey(caremaId) > 0;
        return deleteRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "单位监控",action = "摄像头列表导航")
    @ApiOperation(value = "摄像头列表导航", httpMethod = "GET")
    @RequestMapping(value="/getCameraNavigation",method = RequestMethod.GET)
    public ResponseResult<List<CameraNavigationResp>> getCameraNavigation(@CurUser ShiroUser shiroUser, SearchReq searchReq){
        List<CameraNavigationResp> cameraNavigationResps = new ArrayList<>();
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            dataScope = new DataScope("precinct_id", shiroUser.getPrecinctIds());
        }
        List<CameraListResp> cameraListResps =unitCameraService.getCameraList(dataScope,searchReq);
        if(StringUtils.isEmpty(cameraListResps)){
            return ResponseResult.buildOkResult(cameraNavigationResps);
        }

        Map<Integer,List<CameraListResp>> unitId2CameraList = new HashMap<>();
        List<CameraListResp> cameraList;
        Set<Integer> unitIds = new HashSet<>();
        for(CameraListResp cameraListResp : cameraListResps){
            Integer unitId = cameraListResp.getUnitId();
            cameraList = unitId2CameraList.containsKey(unitId)?unitId2CameraList.get(unitId):new ArrayList<>();
            cameraList.add(cameraListResp);
            unitId2CameraList.put(unitId,cameraList);
            unitIds.add(unitId);
        }

        List<Unit> units = unitService.selectBatchIds(unitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        if(StringUtils.isNotEmpty(units)){
            for(Unit unit : units){
                unitId2Name.put(unit.getId(),unit.getUnitName());
            }
        }
        CameraNavigationResp cameraNavigationResp;
        for(Map.Entry<Integer,List<CameraListResp>> entry : unitId2CameraList.entrySet()){
            cameraNavigationResp = new CameraNavigationResp();
            cameraNavigationResp.setUnitName(unitId2Name.get(entry.getKey()));
            cameraNavigationResp.setCameraLists(entry.getValue());
            cameraNavigationResps.add(cameraNavigationResp);
        }
        return ResponseResult.buildOkResult(cameraNavigationResps);
    }
}
