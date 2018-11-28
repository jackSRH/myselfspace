package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.FaSystemType;
import com.mailian.firecontrol.common.enums.FacilitiesServiceStatus;
import com.mailian.firecontrol.common.enums.StructType;
import com.mailian.firecontrol.dao.auto.mapper.DiagramItemMapper;
import com.mailian.firecontrol.dao.auto.mapper.DiagramStructMapper;
import com.mailian.firecontrol.dao.auto.mapper.FacilitiesMapper;
import com.mailian.firecontrol.dao.auto.model.DiagramStruct;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dao.manual.model.FaNumGySystem;
import com.mailian.firecontrol.dto.web.FacilitiesInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.DictDataResp;
import com.mailian.firecontrol.dto.web.response.FacilitiesListResp;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.component.DictDataComponent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FacilitiesServiceImpl extends BaseServiceImpl<Facilities, FacilitiesMapper> implements FacilitiesService {
    @Resource
    private UnitService unitService;
    @Resource
    private ManageManualMapper manageManualMapper;
    @Resource
    private DiagramStructMapper diagramStructMapper;
    @Resource
    private DiagramItemMapper diagramItemMapper;
    @Autowired
    private DictDataComponent dictDataComponent;

    @Override
    public PageBean<FacilitiesListResp> getFacilitiesList(SearchReq searchReq, DataScope dataScope){
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Map<String,Object> queryMap = new HashMap<>();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("update_time desc");
        queryMap.put("unitId",searchReq.getUnitId());
        queryMap.put("precinctScope",dataScope);
        List<Facilities> facilitiess =  super.selectByMap(queryMap);
        if(StringUtils.isEmpty(facilitiess)){
            return new PageBean<>(currentPage,pageSize,0,new ArrayList<>());
        }

        Set<Integer> faUnitIds = new HashSet<>();
        for(Facilities facilities:facilitiess){
            faUnitIds.add(facilities.getUnitId());
        }
        List<Unit> units = unitService.selectBatchIds(faUnitIds);
        Map<Integer,String> unitId2Name = new HashMap<>();
        for(Unit unit : units){
            unitId2Name.put(unit.getId(),unit.getUnitName());
        }

        List<FacilitiesListResp> facilitiesListResps = new ArrayList<>();
        FacilitiesListResp facilitiesListResp;
        for(Facilities facilities:facilitiess){
            facilitiesListResp = new FacilitiesListResp();
            facilitiesListResp.setId(facilities.getId());
            facilitiesListResp.setFaTypeId(facilities.getFaTypeId());
            facilitiesListResp.setUnitName(unitId2Name.get(facilities.getUnitId()));
            facilitiesListResp.setServiceStatus(facilities.getServiceStatus());
            facilitiesListResp.setFaNumber(facilities.getFaNumber());
            facilitiesListResp.setFaName(facilities.getFaName());
            facilitiesListResp.setUnitId(facilities.getUnitId());

            facilitiesListResp.setServiceStatusDesc(FacilitiesServiceStatus.getValue(facilities.getServiceStatus()));
            if(StringUtils.isNotEmpty(facilities.getFaSystemId()) && StringUtils.isNotEmpty(facilities.getFaTypeId())) {
                String dictType = FaSystemType.getCodeById(facilities.getFaSystemId());
                if(StringUtils.isNotEmpty(dictType)) {
                    DictDataResp dictDataResp = dictDataComponent.getDictDataByTypeAndValue(dictType, facilities.getFaTypeId().toString());
                    if(StringUtils.isNotNull(dictDataResp)) {
                        facilitiesListResp.setFaTypeDesc(dictDataResp.getDictLabel());
                    }
                }
            }
            facilitiesListResps.add(facilitiesListResp);
        }
        PageBean<FacilitiesListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),facilitiesListResps);
        return pageBean;
    }

    @Override
    public Boolean insertOrUpdate(FacilitiesInfo facilitiesInfo){
        Facilities facilities = new Facilities();
        BeanUtils.copyProperties(facilitiesInfo,facilities);
        Integer unitId = facilitiesInfo.getUnitId();
        if(StringUtils.isNotEmpty(unitId)){
            Unit unit = unitService.selectByPrimaryKey(unitId);
            if(StringUtils.isNotNull(unit)){
                facilities.setPrecinctId(unit.getPrecinctId());
            }
        }
        if(StringUtils.isEmpty(facilities.getId())){
            return super.insert(facilities) > 0;
        }else{
            if(StringUtils.isNotNull(facilities.getServiceStatus())){
                facilities.setUpstatusTime(new Date());
            }
            return super.updateByPrimaryKeySelective(facilities) > 0;
        }
    }

    @Override
    public List<FaNumGySystem> countFaNumGySystem(List<Integer> unitIds){
       return manageManualMapper.countFaNumGySystem(unitIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delFacilitiesById(Integer faId) {
        //删除已配置数据项
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("facilities_id",faId);
        queryMap.put("structType",StructType.FACILITY.id);
        List<DiagramStruct> diagramStructs = diagramStructMapper.selectByMap(queryMap);
        List<Integer> structIds = new ArrayList<>();
        for (DiagramStruct diagramStruct : diagramStructs) {
            structIds.add(diagramStruct.getId());
        }
        if(StringUtils.isNotEmpty(structIds)){
            queryMap.clear();
            queryMap.put("dsIds",structIds);
            manageManualMapper.deleteDiagramItemByMap(queryMap);
            diagramStructMapper.deleteBatchIds(structIds);
        }

        return deleteByPrimaryKey(faId);
    }


}
