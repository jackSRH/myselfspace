package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.constants.CommonConstant;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.util.FileNameUtils;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.response.UnitListResp;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.component.UploadComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/management/unit")
@Api(description = "单位信息相关接口")
@WebAPI
public class UnitController extends BaseController {
    @Resource
    private UnitService unitService;
    @Resource
    private UploadComponent uploadComponent;

    @ApiOperation(value = "新增或者更新单位", httpMethod = "POST")
    @RequestMapping(value="/insertOrUpdate",method = RequestMethod.POST)
    public ResponseResult insertOrUpdate(UnitInfo unitInfo,
                                         @ApiParam(value = "单位图片") @RequestParam(value = "attachFile",required = false) MultipartFile attachFile) throws Exception {

        if(StringUtils.isNotNull(attachFile)){
            if(!FileNameUtils.isImg(attachFile.getOriginalFilename())){
                return error("仅支持图片格式:"+ CommonConstant.IMAGE_TYPE +"上传");
            }
            String filePath = uploadComponent.upload(CommonConstant.UPLOAD_IMG_DIR, true, attachFile,null);
            unitInfo.setUnitPic(filePath);
        }
        Boolean insertOrUpdateRes = unitService.insertOrUpdate(unitInfo);
        return insertOrUpdateRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @ApiOperation(value = "修改单位状态", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "unitId", value = "单位id", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value="/changeUnitStatus",method = RequestMethod.POST)
    public ResponseResult changeUnitStatus(Integer unitId,Integer status){
        if(StringUtils.isEmpty(unitId)){
            return error("单位id不能为空");
        }
        if(StringUtils.isEmpty(status)){
            return error("状态不能为空");
        }

        Unit unit = unitService.selectByPrimaryKey(unitId);
        if(StringUtils.isNull(unit)){
            return error("该单位不存在");
        }
        unit.setStatus(status);
        boolean changeRes = unitService.updateByPrimaryKeySelective(unit) > 0;
        return changeRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @ApiOperation(value = "获取单位列表", httpMethod = "GET",notes = "支持分页")
    @RequestMapping(value="/getUnitList",method = RequestMethod.GET)
    public ResponseResult<PageBean<UnitListResp>> getUnitList(@CurUser ShiroUser shiroUser,
                                                              @ApiParam(value = "单位名称") @RequestParam(required = false) String unitName,
                                                              @ApiParam(name = "页数") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                                              @ApiParam(name = "每页条数") @RequestParam(required = false,defaultValue = "10") Integer pageSize){

        if(StringUtils.isEmpty(shiroUser.getPrecinctIds())){
            return error("当前用户无管辖区权限");
        }
        PageBean<UnitListResp> res = unitService.getUnitList(shiroUser.getPrecinctIds(),unitName,pageNo,pageSize);
        return ResponseResult.buildOkResult(res);
    }

    @ApiOperation(value = "获取单位详情", httpMethod = "GET")
    @RequestMapping(value="/getUnitInfoById/{unitId}",method = RequestMethod.GET)
    public ResponseResult<UnitInfo> getUnitInfoById(@ApiParam(value = "单位id") @PathVariable("unitId") Integer unitId){
        Unit unit = unitService.selectByPrimaryKey(unitId);
        if(StringUtils.isNull(unit)){
            return error("该单位不存在");
        }
        UnitInfo unitInfo = new UnitInfo();
        BeanUtils.copyProperties(unit,unitInfo);
        return ResponseResult.buildOkResult(unitInfo);
    }
}
