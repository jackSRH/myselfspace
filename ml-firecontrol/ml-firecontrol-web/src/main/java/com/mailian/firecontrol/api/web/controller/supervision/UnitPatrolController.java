package com.mailian.firecontrol.api.web.controller.supervision;

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
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.common.util.FileNameUtils;
import com.mailian.firecontrol.dao.auto.model.UnitPatrol;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.request.UnitPatrolReq;
import com.mailian.firecontrol.dto.web.response.UnitPatrolListResp;
import com.mailian.firecontrol.dto.web.response.UnitPatrolResp;
import com.mailian.firecontrol.service.UnitPatrolService;
import com.mailian.firecontrol.service.component.UploadComponent;
import io.swagger.annotations.Api;
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
@RequestMapping("/supervision/unitpatrol")
@Api(description = "巡查管理相关接口")
@WebAPI
public class UnitPatrolController extends BaseController {

    @Resource
    private UnitPatrolService unitPatrolService;
    @Resource
    private UploadComponent uploadComponent;


    @Log(title = "单位监控",action = "新增巡查记录")
    @ApiOperation(value = "新增巡查记录", httpMethod = "POST")
    @RequestMapping(value="/insert",method = RequestMethod.POST)
    public ResponseResult insert(@CurUser ShiroUser shiroUser, UnitPatrolReq unitPatrolReq,
                                 @ApiParam(value = "巡查截图") @RequestParam(value = "attachFile",required = false) MultipartFile attachFile) throws Exception {
        if(StringUtils.isNull(unitPatrolReq)){
            return error("参数不能为空");
        }
        if(StringUtils.isEmpty(unitPatrolReq.getCameraId())){
            return error("摄像头id不能为空");
        }

        if(StringUtils.isNotNull(attachFile)){
            if(!FileNameUtils.isImg(attachFile.getOriginalFilename())){
                return error("仅支持图片格式:"+ CoreCommonConstant.IMAGE_TYPE +"上传");
            }
            String filePath = uploadComponent.upload(CommonConstant.UPLOAD_IMG_DIR, true, attachFile,null);
            unitPatrolReq.setPatrolPic(filePath);
        }
        Boolean insertRes = unitPatrolService.insert(shiroUser.getId(),unitPatrolReq);
        return insertRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "单位监控",action = "查找巡查记录列表")
    @ApiOperation(value = "查找巡查记录列表", httpMethod = "GET")
    @RequestMapping(value="/getUnitPatrolList",method = RequestMethod.GET)
    public ResponseResult<PageBean<UnitPatrolListResp>> getUnitPatrolList(@CurUser ShiroUser shiroUser, SearchReq searchReq){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            dataScope = new DataScope("precinct_id", shiroUser.getPrecinctIds());
        }
        return ResponseResult.buildOkResult(unitPatrolService.getUnitPatrolList(dataScope,searchReq));
    }

    @Log(title = "单位监控",action = "查找巡查记录详情")
    @ApiOperation(value = "查找巡查记录详情", httpMethod = "GET")
    @RequestMapping(value="/getUnitPatrolInfoById/{patrolId}",method = RequestMethod.GET)
    public ResponseResult getUnitPatrolInfoById(@ApiParam(value = "巡查记录id") @PathVariable("patrolId") Integer patrolId){
        if(StringUtils.isEmpty(patrolId)){
            return error("巡查记录id不能为空");
        }
        UnitPatrol unitPatrol = unitPatrolService.selectByPrimaryKey(patrolId);
        if(StringUtils.isNull(unitPatrol)){
            return error("该巡查记录不存在");
        }
        UnitPatrolResp unitPatrolResp = new UnitPatrolResp();
        BeanUtils.copyProperties(unitPatrol,unitPatrolResp);
        return ResponseResult.buildOkResult(unitPatrolResp);
    }


}
