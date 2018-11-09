package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.FacilitiesServiceStatus;
import com.mailian.firecontrol.dao.auto.mapper.FacilitiesMapper;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dao.manual.model.FaNumGySystem;
import com.mailian.firecontrol.dto.web.FacilitiesInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.FacilitiesListResp;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.UnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FacilitiesServiceImpl extends BaseServiceImpl<Facilities, FacilitiesMapper> implements FacilitiesService {
    @Resource
    private UnitService unitService;
    @Resource
    private ManageManualMapper manageManualMapper;

    @Override
    public PageBean<FacilitiesListResp> getFacilitiesList(SearchReq searchReq){
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        Map<String,Object> queryMap = new HashMap<>();
        Page page = PageHelper.startPage(currentPage,pageSize);
        page.setOrderBy("update_time desc");
        queryMap.put("unitId",searchReq.getUnitId());
        List<Facilities> facilitiess =  super.selectByMap(queryMap);
        if(StringUtils.isEmpty(facilitiess)){
            return new PageBean<>();
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
            BeanUtils.copyProperties(facilities,facilitiesListResp);
            facilitiesListResp.setUnitName(unitId2Name.get(facilities.getUnitId()));
            facilitiesListResp.setServiceStatusDesc(FacilitiesServiceStatus.getValue(facilities.getServiceStatus()));
            //TODO 通过设施型号id获取型号描述
            //facilitiesListResp.setFaTypeDesc("");
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
                facilities.setUnitName(unit.getUnitName());
                facilities.setUnitPhone(unit.getContactPhone());
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


}
