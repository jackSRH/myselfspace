package com.mailian.firecontrol.api.web.controller.management;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.StructType;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.common.util.FileNameUtils;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.DiagramStructReq;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.DeviceResp;
import com.mailian.firecontrol.dto.web.response.DiagramStructResp;
import com.mailian.firecontrol.dto.web.response.UnitListResp;
import com.mailian.firecontrol.service.DiagramStructService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.component.UploadComponent;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/management/unit")
@Api(description = "单位信息相关接口")
@WebAPI
public class UnitController extends BaseController {
    @Resource
    private UnitService unitService;
    @Resource
    private UploadComponent uploadComponent;
    @Resource
    private DiagramStructService diagramStructService;


    @Log(title = "配置管理",action = "新增更新单位")
    @ApiOperation(value = "新增或者更新单位", httpMethod = "POST")
    @RequestMapping(value="/insertOrUpdate",method = RequestMethod.POST)
    public ResponseResult insertOrUpdate(UnitInfo unitInfo,
                                         @ApiParam(value = "单位图片") @RequestParam(value = "attachFile",required = false) MultipartFile attachFile) throws Exception {
        if(StringUtils.isNull(unitInfo)){
            return error("参数不能为空");
        }
        if(StringUtils.isNotNull(attachFile)){
            if(!FileNameUtils.isImg(attachFile.getOriginalFilename())){
                return error("仅支持图片格式:"+ CoreCommonConstant.IMAGE_TYPE +"上传");
            }
            String filePath = uploadComponent.upload(CommonConstant.UPLOAD_IMG_DIR, true, attachFile,null);
            unitInfo.setUnitPic(filePath);
        }
        Boolean insertOrUpdateRes = unitService.insertOrUpdate(unitInfo);
        return insertOrUpdateRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "修改单位状态")
    @ApiOperation(value = "修改单位状态", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "unitId", value = "单位id", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "状态 0正常 1停用", required = true, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value="/changeUnitStatus",method = RequestMethod.POST)
    public ResponseResult changeUnitStatus(Integer unitId,Integer status){
        if(StringUtils.isEmpty(unitId)){
            return error("单位id不能为空");
        }
        if(StringUtils.isNull(status)){
            return error("状态不能为空");
        }

        Unit unit = unitService.selectByPrimaryKey(unitId);
        if(StringUtils.isNull(unit)){
            return error("该单位不存在");
        }
        Unit updateUnit = new Unit();
        updateUnit.setId(unitId);
        updateUnit.setStatus(status);
        boolean changeRes = unitService.updateByPrimaryKeySelective(updateUnit) > 0;
        return changeRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "获取单位列表")
    @ApiOperation(value = "获取单位列表", httpMethod = "GET",notes = "支持分页")
    @RequestMapping(value="/getUnitList",method = RequestMethod.GET)
    public ResponseResult<PageBean<UnitListResp>> getUnitList(@CurUser ShiroUser shiroUser,SearchReq searchReq){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new PageBean<>());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        PageBean<UnitListResp> res = unitService.getUnitList(dataScope,searchReq);
        return ResponseResult.buildOkResult(res);
    }

    @Log(title = "配置管理",action = "获取单位详情")
    @ApiOperation(value = "获取单位详情", httpMethod = "GET")
    @RequestMapping(value="/getUnitInfoById/{unitId}",method = RequestMethod.GET)
    public ResponseResult<UnitInfo> getUnitInfoById(@ApiParam(value = "单位id") @PathVariable("unitId") Integer unitId){
        if(StringUtils.isEmpty(unitId)){
            return error("单位id不能为空");
        }
        Unit unit = unitService.selectByPrimaryKey(unitId);
        if(StringUtils.isNull(unit)){
            return error("该单位不存在");
        }
        UnitInfo unitInfo = new UnitInfo();
        BeanUtils.copyProperties(unit,unitInfo);
        return ResponseResult.buildOkResult(unitInfo);
    }

    @Log(title = "配置管理",action = "获取单位遥控配置列表")
    @ApiOperation(value = "获取单位遥控配置列表", httpMethod = "GET")
    @RequestMapping(value="/getYcStructs",method = RequestMethod.GET)
    public ResponseResult<PageBean<DiagramStructResp>> getYcStructs(SearchReq searchReq){
        Integer unitId = searchReq.getUnitId();
        if(StringUtils.isEmpty(unitId)){
            return error("单位id不能为空");
        }
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage,pageSize);
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("unitId",unitId);
        queryMap.put("structType", StructType.REMOTE.id);
        List<DiagramStructResp> diagramStructResps = diagramStructService.getDiagramStructByMap(queryMap);
        PageBean<DiagramStructResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),diagramStructResps);
        return ResponseResult.buildOkResult(pageBean);
    }

    @Log(title = "配置管理",action = "删除单位遥控配置")
    @ApiOperation(value = "删除单位遥控配置", httpMethod = "DELETE")
    @RequestMapping(value="/deleteYcStructByDsId/{dsId}",method = RequestMethod.DELETE)
    public ResponseResult deleteYcStructByDsId(@ApiParam(value = "模块id") @PathVariable("dsId") Integer dsId){
        if(StringUtils.isEmpty(dsId)){
            return error("id不能为空");
        }
        Boolean deleteRes = diagramStructService.delete(dsId);
        return deleteRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "新增单位遥控配置")
    @ApiOperation(value = "新增单位遥控配置", httpMethod = "POST")
    @RequestMapping(value="/insertYcStruct",method = RequestMethod.POST)
    public ResponseResult insertYcStruct(@RequestBody DiagramStructReq diagramStructReq){
        if(StringUtils.isNull(diagramStructReq)){
            return error("参数不能空");
        }
       Boolean insertRes = diagramStructService.insert(diagramStructReq,null);
       return insertRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "更新单位遥控配置")
    @ApiOperation(value = "更新单位遥控配置", httpMethod = "POST")
    @RequestMapping(value="/updateYcStruct",method = RequestMethod.POST)
    public ResponseResult updateYcStruct(@RequestBody DiagramStructReq diagramStructReq){
        if(StringUtils.isNull(diagramStructReq)){
            return error("参数不能空");
        }
        if(StringUtils.isEmpty(diagramStructReq.getId())){
            return error("id不能为空");
        }
        Boolean updateRes = diagramStructService.update(diagramStructReq,null);
        return updateRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "获取未分配网关")
    @ApiOperation(value = "获取未分配网关", httpMethod = "GET")
    @GetMapping(value = "getUnallotDevice")
    public ResponseResult<List<DeviceResp>> getUnallotDevice(){
        return ResponseResult.buildOkResult(unitService.getUnallotDevice());
    }
}
