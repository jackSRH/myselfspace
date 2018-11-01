package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.enums.Status;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.DictDataMapper;
import com.mailian.firecontrol.dao.auto.mapper.DictTypeMapper;
import com.mailian.firecontrol.dao.auto.model.DictData;
import com.mailian.firecontrol.dao.auto.model.DictType;
import com.mailian.firecontrol.dto.web.request.DictTypeReq;
import com.mailian.firecontrol.dto.web.request.DictTypeSearchReq;
import com.mailian.firecontrol.dto.web.response.DictTypeResp;
import com.mailian.firecontrol.service.DictTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class DictTypeServiceImpl extends BaseServiceImpl<DictType,DictTypeMapper> implements DictTypeService {
    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public PageBean<DictTypeResp> queryListByPage(DictTypeSearchReq searchReq) {
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Page page = PageHelper.startPage(currentPage, pageSize);

        Map<String,Object> queryMap = new HashMap<>();
        if(StringUtils.isNotEmpty(searchReq.getDictName())) {
            queryMap.put("dictName", searchReq.getDictName());
        }
        if(StringUtils.isNotEmpty(searchReq.getDictType())) {
            queryMap.put("dictType", searchReq.getDictType());
        }
        queryMap.put("status",searchReq.getStatus());

        List<DictType> dictTypeList = selectByMap(queryMap);

        List<DictTypeResp> dictTypeRespList = new ArrayList<>();
        for (DictType dictType : dictTypeList) {
            DictTypeResp dictTypeResp = new DictTypeResp();
            BeanUtils.copyProperties(dictType,dictTypeResp);
            dictTypeRespList.add(dictTypeResp);
        }

        PageBean<DictTypeResp> dictTypeInfoPageBean = new PageBean<>(currentPage, pageSize, (int)page.getTotal(),dictTypeRespList);
        return dictTypeInfoPageBean;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult insertOrUpdate(DictTypeReq dictTypeReq) {
        if(StringUtils.isEmpty(dictTypeReq.getId())){
            return insertByDictTypeReq(dictTypeReq);
        }else{
            return updateByDictTypeReq(dictTypeReq);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(Integer dictTypeId) {
        //删除字典数据
        DictType dictType = this.selectByPrimaryKey(dictTypeId);

        if(StringUtils.isNull(dictType)){
            throw new RequestException(ResponseCode.FAIL.code,"字典类型ID不存在");
        }

        Map<String,Object> delMap = new HashMap<>();
        delMap.put("dictType",dictType.getDictType());
        List<DictData> dictDataList = dictDataMapper.selectByMap(delMap);
        List<Integer> ids = new ArrayList<>();
        for (DictData dictData : dictDataList) {
            //系统初始化数据不允许修改
            if(Status.NORMAL.id.byteValue() == dictData.getIsDefault().byteValue()){
                throw new RequestException(ResponseCode.FAIL.code,"该类型下包含系统默认数据!不允许删除该类型");
            }

            ids.add(dictData.getId());
        }
        if(StringUtils.isNotEmpty(ids)) {
            dictDataMapper.deleteBatchIds(ids);
        }
        return super.deleteByPrimaryKey(dictTypeId);
    }

    private ResponseResult updateByDictTypeReq(DictTypeReq dictTypeReq) {
        DictType oldDictType = this.selectByPrimaryKey(dictTypeReq.getId());
        if(StringUtils.isNull(oldDictType)){
            throw new RequestException(ResponseCode.FAIL.code,"字典类型ID不存在");
        }

        String dictTypeStr = dictTypeReq.getDictType();
        if(StringUtils.isNotEmpty(dictTypeStr) && !dictTypeStr.equals(oldDictType.getDictType())){
            if(checkUnique(dictTypeStr)){
                throw new RequestException(ResponseCode.FAIL.code,"字典类型不能重复");
            }
        }

        DictType dictType = new DictType();
        BeanUtils.copyProperties(dictTypeReq,dictType);
        int updateResult = this.updateByPrimaryKeySelective(dictType);
        return updateResult>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    private ResponseResult insertByDictTypeReq(DictTypeReq dictTypeReq) {
        if(checkUnique(dictTypeReq.getDictType())){
            throw new RequestException(ResponseCode.FAIL.code,"字典类型不能重复");
        }

        DictType dictType = new DictType();
        BeanUtils.copyProperties(dictTypeReq,dictType);

        if(StringUtils.isNull(dictType.getStatus())){
            dictType.setStatus(Status.NORMAL.id);
        }
        int insertResult = this.insert(dictType);
        return insertResult>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    /**
     * 校验字典类型唯一
     * @param dictType
     * @return
     */
    private boolean checkUnique(String dictType){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("dictType",dictType);

        List<DictType> dictTypeList = this.selectByMap(queryMap);

        if(StringUtils.isNotEmpty(dictTypeList)){
            return true;
        }
        return false;
    }
}
