package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.StructType;
import com.mailian.firecontrol.common.util.FileNameUtils;
import com.mailian.firecontrol.dto.web.request.DiagramStructReq;
import com.mailian.firecontrol.dto.web.response.DiagramStructResp;
import com.mailian.firecontrol.service.DiagramStructService;
import com.mailian.firecontrol.service.component.UploadComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/management/diagram")
@Api(description = "系统图相关接口")
@WebAPI
public class DiagramController extends BaseController {

    @Resource
    private DiagramStructService diagramStructService;
    @Resource
    private UploadComponent uploadComponent;

    @Log(title = "配置管理",action = "新增系统图模块")
    @ApiOperation(value = "新增系统图模块", httpMethod = "POST")
    @RequestMapping(value="/insert",method = RequestMethod.POST)
    public ResponseResult insert(DiagramStructReq diagramStructReq,
                          @ApiParam(value = "单位图片") @RequestParam(value = "attachFile",required = false) MultipartFile attachFile) throws Exception {
        if(StringUtils.isNull(diagramStructReq)){
            return error("参数不能为空");
        }
        String filePath = null;
        if(StringUtils.isNotNull(attachFile)){
            if(!FileNameUtils.isImg(attachFile.getOriginalFilename())){
                return error("仅支持图片格式:"+ CoreCommonConstant.IMAGE_TYPE +"上传");
            }
            filePath = uploadComponent.upload(CommonConstant.UPLOAD_IMG_DIR, true, attachFile,null);
        }
        Boolean insertRes = diagramStructService.insert(diagramStructReq,filePath);
        return insertRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "删除系统图模块")
    @ApiOperation(value = "删除系统图模块", httpMethod = "DELETE")
    @RequestMapping(value="/delete/{dsId}",method = RequestMethod.DELETE)
    public ResponseResult delete(@ApiParam(value = "模块id",required = true) @PathVariable("dsId") Integer dsId){
        if(StringUtils.isEmpty(dsId)){
            return error("id不能为空");
        }
        Boolean deleteRes = diagramStructService.delete(dsId);
        return deleteRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }


    @ApiOperation(value = "查找系统图模块", httpMethod = "GET")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseResult<List<DiagramStructResp>> find(@ApiParam(value = "设施id",required = true) @RequestParam("faId") Integer faId) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("facilitiesId",faId);
        queryMap.put("structType",StructType.FACILITY.id);
        List<DiagramStructResp> diagramStructInfos = diagramStructService.getDiagramStructByMap(queryMap);
        return ResponseResult.buildOkResult(diagramStructInfos);
    }

    @Log(title = "配置管理",action = "更新系统图模块")
    @ApiOperation(value = "更新系统图模块", httpMethod = "POST")
    @RequestMapping(value="/update",method = RequestMethod.POST)
    public ResponseResult delete(DiagramStructReq diagramStructReq,
                                 @ApiParam(value = "单位图片") @RequestParam(value = "attachFile",required = false) MultipartFile attachFile) throws Exception {
        if(StringUtils.isNull(diagramStructReq)){
            return  error("参数不能为空");
        }
        if(StringUtils.isEmpty(diagramStructReq.getId())){
            return  error("id不能为空");
        }
        String filePath = null;
        if(StringUtils.isNotNull(attachFile)){
            if(!FileNameUtils.isImg(attachFile.getOriginalFilename())){
                return error("仅支持图片格式:"+ CoreCommonConstant.IMAGE_TYPE +"上传");
            }
            filePath = uploadComponent.upload(CommonConstant.UPLOAD_IMG_DIR, true, attachFile,null);
        }
        Boolean updateRes = diagramStructService.update(diagramStructReq,filePath);
        return updateRes?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }
}
