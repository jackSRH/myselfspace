package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.BooleanEnum;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.enums.Status;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.StringUtils;
import com.mailian.core.util.TreeParser;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.dao.auto.mapper.DictDataMapper;
import com.mailian.firecontrol.dao.auto.model.DictData;
import com.mailian.firecontrol.dto.web.request.DictDataReq;
import com.mailian.firecontrol.dto.web.response.DictDataResp;
import com.mailian.firecontrol.service.DictDataService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@Service
public class DictDataServiceImpl extends BaseServiceImpl<DictData,DictDataMapper> implements DictDataService {

    @Cacheable(key = "#dictType",value = CommonConstant.SYS_DICT_DATA_KEY)
    @Override
    public List<DictDataResp> queryDataByType(String dictType) {
        if(StringUtils.isEmpty(dictType)){
            throw new RequestException(ResponseCode.FAIL.code,"字典类型不能为空");
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("dictType",dictType);
        List<DictData> dictDataList = selectByMap(queryMap);

        List<DictDataResp> dictDataRespList = new ArrayList<>();
        DictDataResp dictDataResp;
        for (DictData dictData : dictDataList) {
            dictDataResp = new DictDataResp();
            BeanUtils.copyProperties(dictData,dictDataResp);
            dictDataRespList.add(dictDataResp);
        }

        return dictDataRespList;
    }

    @Override
    public List<DictDataResp> queryDataTreeByType(String dictType) {
        if(StringUtils.isEmpty(dictType)){
            throw new RequestException(ResponseCode.FAIL.code,"字典类型不能为空");
        }
        DictDataService dictDataService = (DictDataService) AopContext.currentProxy();
        List<DictDataResp> dictDataRespList = dictDataService.queryDataByType(dictType);
        if(StringUtils.isNotEmpty(dictDataRespList)) {
            Integer parentId = null;
            for (DictDataResp dictDataResp : dictDataRespList) {
                if(StringUtils.isNull(parentId) || parentId>dictDataResp.getParentId()) {
                    parentId = dictDataResp.getParentId();
                }
            }
            if(StringUtils.isNotNull(parentId)){
                dictDataRespList = TreeParser.getTreeList(parentId.toString(),dictDataRespList,true);
            }
        }
        return dictDataRespList;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#dictDataReq.dictType",value = CommonConstant.SYS_DICT_DATA_KEY)
    @Override
    public ResponseResult insertOrUpdate(DictDataReq dictDataReq) {
        if(StringUtils.isEmpty(dictDataReq.getId())){
            return insertByDictDataReq(dictDataReq);
        }else{
            return updateByDictDataReq(dictDataReq);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#dictData.dictType",value = CommonConstant.SYS_DICT_DATA_KEY)
    @Override
    public int deleteByDictData(DictData dictData) {
        if(StringUtils.isNull(dictData)){
            throw new RequestException(ResponseCode.FAIL.code,"字典数据ID不存在");
        }
        DictDataService dictDataService = (DictDataService) AopContext.currentProxy();
        List<DictDataResp> dictDataList = dictDataService.queryDataByType(dictData.getDictType());
        DictDataResp dictDataResp = new DictDataResp();
        BeanUtils.copyProperties(dictData,dictDataResp);

        List<String> dictDataIds = TreeParser.getChildIdList(dictDataList,dictDataResp);
        return deleteBatchIds(dictDataIds);
    }


    private ResponseResult insertByDictDataReq(DictDataReq dictDataReq) {
        if(StringUtils.isEmpty(dictDataReq.getDictLabel())){
            throw new RequestException(ResponseCode.FAIL.code,"字典名称不能为空");
        }
        if(StringUtils.isEmpty(dictDataReq.getDictType())){
            throw new RequestException(ResponseCode.FAIL.code,"字典类型不能为空");
        }
        if(StringUtils.isNotEmpty(dictDataReq.getDictValue())) {
            if (checkUnique(dictDataReq.getDictValue(), dictDataReq.getDictType())) {
                throw new RequestException(ResponseCode.FAIL.code, "字典键值重复");
            }
        }

        DictData dictData = new DictData();
        BeanUtils.copyProperties(dictDataReq,dictData);

        if(StringUtils.isNull(dictData.getStatus())){
            dictData.setStatus(Status.NORMAL.id);
        }
        if(StringUtils.isNull(dictData.getParentId())){
            dictData.setParentId(0);
            dictData.setDictRank(1);
        }else{
            DictData dictDataDb = selectByPrimaryKey(dictData.getParentId());
            if(StringUtils.isNull(dictDataDb)){
                throw new RequestException(ResponseCode.FAIL.code,"父级不存在");
            }

            int rank = dictDataDb.getDictRank();
            dictData.setDictRank(rank+1);
        }
        if(StringUtils.isNull(dictData.getDictSort())){
            dictData.setDictSort(1);
        }
        if(StringUtils.isNull(dictData.getIsDefault())){
            dictData.setIsDefault(BooleanEnum.YES.id);
        }
        int insertResult = this.insert(dictData);
        if(insertResult>0){
            if(StringUtils.isEmpty(dictData.getDictValue())){
                DictData upDictData = new DictData();
                upDictData.setDictValue(dictData.getId().toString());
                upDictData.setId(dictData.getId());
                this.updateByPrimaryKeySelective(upDictData);
            }
        }
        return insertResult>0?ResponseResult.buildOkResult(dictData.getId()):ResponseResult.buildFailResult();
    }


    private ResponseResult updateByDictDataReq(DictDataReq dictDataReq) {
        DictData oldDictData = this.selectByPrimaryKey(dictDataReq.getId());
        if(StringUtils.isNull(oldDictData)){
            throw new RequestException(ResponseCode.FAIL.code,"字典数据ID不存在");
        }

        if(StringUtils.isNotEmpty(dictDataReq.getDictValue()) && !dictDataReq.getDictValue().equals(oldDictData.getDictValue())){
            if(checkUnique(dictDataReq.getDictValue(),dictDataReq.getDictType())){
                throw new RequestException(ResponseCode.FAIL.code,"字典键值重复");
            }
        }

        //系统初始化数据不允许修改
        if(Status.NORMAL.id.byteValue() == oldDictData.getIsDefault().byteValue()){
            throw new RequestException(ResponseCode.FAIL.code,"系统默认数据不允许修改");
        }

        DictData dictData = new DictData();
        BeanUtils.copyProperties(dictDataReq,dictData);
        int updateResult = this.updateByPrimaryKeySelective(dictData);
        return updateResult>0?ResponseResult.buildOkResult(dictData.getId()):ResponseResult.buildFailResult();
    }

    /**
     * 校验同字典类型下是否有键值重复
     * @param dictValue
     * @param dictType
     * @return
     */
    private boolean checkUnique(String dictValue,String dictType){
        boolean result = false;
        if(StringUtils.isNotEmpty(dictValue)) {
            DictDataService dictDataService = (DictDataService) AopContext.currentProxy();
            List<DictDataResp> dictDataList = dictDataService.queryDataByType(dictType);

            for (DictDataResp dictDataResp : dictDataList) {
                if(dictValue.equals(dictDataResp.getDictValue())){
                    result=true;
                    break;
                }
            }
        }
        return result;
    }
}
