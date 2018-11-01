package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.model.DictData;
import com.mailian.firecontrol.dao.auto.model.DictType;
import com.mailian.firecontrol.dto.web.request.DictDataReq;
import com.mailian.firecontrol.dto.web.request.DictTypeReq;
import com.mailian.firecontrol.dto.web.request.DictTypeSearchReq;
import com.mailian.firecontrol.dto.web.response.DictDataResp;
import com.mailian.firecontrol.dto.web.response.DictTypeResp;
import com.mailian.firecontrol.service.DictDataService;
import com.mailian.firecontrol.service.DictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/31
 * @Description:
 */
@RestController
@RequestMapping("/system/dict")
@Api(description = "数据字典相关接口")
@WebAPI
public class DictController extends BaseController {
    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private DictDataService dictDataService;


    @ApiOperation(value = "获取字典类型列表", httpMethod = "GET",notes = "字典类型列表，支持分页")
    @RequestMapping(value="/getListByPage",method = RequestMethod.GET)
    public ResponseResult<PageBean<DictTypeResp>> getListByPage(@ApiParam(value = "查询参数") DictTypeSearchReq searchReq){
        return ResponseResult.buildOkResult(dictTypeService.queryListByPage(searchReq));
    }

    @ApiOperation(value = "字典类型详情", httpMethod = "GET",notes = "根据字典类型id获取详细信息")
    @RequestMapping(value="/dictTypeDetail/{dictTypeId}",method = RequestMethod.GET)
    public ResponseResult<DictTypeResp> dictTypeDetail(@ApiParam(name="dictTypeId",value = "字典类型ID",required = true) @PathVariable("dictTypeId") Integer dictTypeId) {
        DictType dictType = dictTypeService.selectByPrimaryKey(dictTypeId);

        if(StringUtils.isNull(dictType)){
            return error("字典类型ID不存在");
        }

        DictTypeResp dictTypeResp = new DictTypeResp();
        BeanUtils.copyProperties(dictType,dictTypeResp);

        return ResponseResult.buildOkResult(dictTypeResp);
    }

    @ApiOperation(value = "数据字典类型保存", httpMethod = "POST",notes = "支持新增修改")
    @RequestMapping(value="/saveDictType",method = RequestMethod.POST)
    public ResponseResult saveDictType(@RequestBody @Validated DictTypeReq dictTypeReq){
        return dictTypeService.insertOrUpdate(dictTypeReq);
    }


    @ApiOperation(value = "删除字典类型", httpMethod = "GET",notes = "根据字典类型id删除")
    @RequestMapping(value="/deleteDictType/{dictTypeId}",method = RequestMethod.GET)
    public ResponseResult deleteDictType(@ApiParam(name="dictTypeId",value = "字典类型id",required = true) @PathVariable("dictTypeId") Integer dictTypeId) {
        int result = dictTypeService.deleteById(dictTypeId);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }


    @ApiOperation(value = "获取字典数据", httpMethod = "GET",notes = "根据字典类型获取所有字典数据")
    @RequestMapping(value="/getDictDataByType",method = RequestMethod.GET)
    public ResponseResult<List<DictDataResp>> getDictDataByType(@ApiParam(value = "字典类型") @RequestParam(name = "dictType") String dictType){
        return ResponseResult.buildOkResult(dictDataService.queryDataTreeByType(dictType));
    }


    @ApiOperation(value = "字典数据详情", httpMethod = "GET",notes = "根据字典数据id获取详细信息")
    @RequestMapping(value="/dictDataDetail/{dictDataId}",method = RequestMethod.GET)
    public ResponseResult<DictDataResp> dictDataDetail(@ApiParam(name="dictDataId",value = "字典数据ID",required = true) @PathVariable("dictDataId") Integer dictDataId) {
        DictData dictData = dictDataService.selectByPrimaryKey(dictDataId);

        if(StringUtils.isNull(dictData)){
            return error("字典数据ID不存在");
        }

        DictDataResp dictDataInfo = new DictDataResp();
        BeanUtils.copyProperties(dictData,dictDataInfo);

        if(StringUtils.isNotEmpty(dictData.getParentId())){
            DictDataResp parentDictDataInfo = new DictDataResp();
            dictData = dictDataService.selectByPrimaryKey(dictData.getParentId());
            BeanUtils.copyProperties(dictData,parentDictDataInfo);
            dictDataInfo.setParentDictData(dictDataInfo);
        }

        return ResponseResult.buildOkResult(dictDataInfo);
    }


    @ApiOperation(value = "字典数据保存", httpMethod = "POST",notes = "支持新增修改")
    @RequestMapping(value="/saveDictData",method = RequestMethod.POST)
    public ResponseResult saveDictData(@ApiParam(value = "字典数据参数") @RequestBody @Validated DictDataReq dictDataReq){
        return dictDataService.insertOrUpdate(dictDataReq);
    }

    @ApiOperation(value = "删除字典数据", httpMethod = "GET",notes = "根据字典数据id删除")
    @RequestMapping(value="/deleteDictData/{dictDataId}",method = RequestMethod.GET)
    public ResponseResult deleteDictData(@ApiParam(name="dictDataId",value = "字典数据id",required = true) @PathVariable("dictDataId") Integer dictDataId) {
        DictData dictData = dictDataService.selectByPrimaryKey(dictDataId);

        if(StringUtils.isNull(dictData)){
            return error("字典数据ID不存在");
        }

        //系统初始化数据不允许修改
        if(Status.NORMAL.id.byteValue() == dictData.getIsDefault().byteValue()){
            return error("系统默认数据不允许删除");
        }

        int result = dictDataService.deleteByDictData(dictData);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

}
