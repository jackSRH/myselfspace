package com.mailian.firecontrol.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.db.DataScope;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.request.SearchReq;
import com.mailian.firecontrol.dto.web.response.UnitListResp;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.UnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UnitServiceImpl extends BaseServiceImpl<Unit, UnitMapper> implements UnitService {
    @Resource
    private PrecinctMapper precinctMapper;
    @Resource
    private AreaService areaService;



    @Override
    public PageBean<UnitListResp> getUnitList(DataScope dataScope,SearchReq searchReq) {
        Integer currentPage = searchReq.getCurrentPage();
        Integer pageSize = searchReq.getPageSize();
        String unitName = searchReq.getUnitName();
        Page page = PageHelper.offsetPage(currentPage,pageSize);
        page.setOrderBy("update_time desc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitNameLike",unitName);
        }
        List<Unit> units = super.selectByMap(queryMap);
        if(StringUtils.isEmpty(units)){
            return new PageBean<>();
        }

        //查找管辖区名称
        Set<Integer> precinctIds = new HashSet<>();
        for(Unit unit : units){
            precinctIds.add(unit.getPrecinctId());
        }
        List<Precinct> precincts = precinctMapper.selectBatchIds(precinctIds);
        Map<Integer,String> precinetId2Name = new HashMap<>();
        for(Precinct precinct : precincts){
            precinetId2Name.put(precinct.getId(),precinct.getPrecinctName());
        }

        //查找地址信息
        Set<Integer> areaIds = new HashSet<>();
        for(Unit unit:units){
            Integer areaId = unit.getAreaId();
            Integer provinceId = unit.getProvinceId();
            Integer cityId = unit.getCityId();
            if(StringUtils.isNotEmpty(areaId)){
                areaIds.add(areaId);
            }
            if(StringUtils.isNotEmpty(provinceId)){
                areaIds.add(provinceId);
            }
            if(StringUtils.isNotEmpty(cityId)){
                areaIds.add(cityId);
            }
        }
        List<Area> areas = areaService.selectBatchIds(areaIds);
        Map<Integer,String> areaId2Name = new HashMap<>();
        for(Area area : areas){
            areaId2Name.put(area.getId(),area.getAreaName());
        }

        List<UnitListResp> unitListResps = new ArrayList<>();
        UnitListResp unitListResp;
        StringBuffer areaInfo = new StringBuffer();
        Integer areaId = 0 ,provinceId = 0,cityId = 0 ;
        for(Unit unit:units){
            unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit,unitListResp);
            unitListResp.setPrecinct(precinetId2Name.get(unit.getPrecinctId()));

            areaId = unit.getAreaId();
            provinceId = unit.getProvinceId();
            cityId = unit.getCityId();
            areaInfo.setLength(0);
            if(StringUtils.isNotEmpty(provinceId) && areaId2Name.containsKey(provinceId)){
                areaInfo.append(areaId2Name.get(provinceId));
            }
            if(StringUtils.isNotEmpty(cityId) && areaId2Name.containsKey(cityId)){
                areaInfo.append(areaId2Name.get(cityId));
            }
            if(StringUtils.isNotEmpty(areaId) && areaId2Name.containsKey(areaId)){
                areaInfo.append(areaId2Name.get(areaId));
            }
            unitListResp.setAreaInfo(areaInfo.toString());
            unitListResps.add(unitListResp);
        }

        PageBean<UnitListResp> pageBean = new PageBean<>(currentPage,pageSize,(int)page.getTotal(),unitListResps);
        return pageBean;
    }

    @Override
    public Boolean insertOrUpdate(UnitInfo unitInfo){
        Unit unit = new Unit();
        BeanUtils.copyProperties(unitInfo,unit);
        if(StringUtils.isEmpty(unit.getId())){
            unit.setStatus(Status.NORMAL.id.intValue());
            return super.insert(unit) > 0;
        }else {
            return super.updateByPrimaryKeySelective(unit) > 0;
        }
    }

}
