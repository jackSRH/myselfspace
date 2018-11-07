package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.util.FileNameUtils;
import com.mailian.firecontrol.dto.web.request.DiagramStructReq;
import com.mailian.firecontrol.service.DiagramStructService;
import com.mailian.firecontrol.service.component.UploadComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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
