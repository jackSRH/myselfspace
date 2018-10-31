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
    public PageBean<UnitListResp> getUnitList(DataScope dataScope, String unitName, Integer currentPage, Integer pageSize) {
        Page page = PageHelper.offsetPage(currentPage,pageSize);
        page.setOrderBy("update_time desc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope", dataScope);
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitName",unitName);
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
            areaIds.add(unit.getAreaId());
            areaIds.add(unit.getProvinceId());
            areaIds.add(unit.getCityId());
        }
        List<Area> areas = areaService.selectBatchIds(areaIds);
        Map<Integer,String> areaId2Name = new HashMap<>();
        for(Area area : areas){
            areaId2Name.put(area.getId(),area.getAreaName());
        }

        List<UnitListResp> unitListResps = new ArrayList<>();
        UnitListResp unitListResp;
        for(Unit unit:units){
            unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit,unitListResp);
            unitListResp.setPrecinct(precinetId2Name.get(unit.getPrecinctId()));
            unitListResp.setAreaInfo(areaId2Name.get(unit.getProvinceId()) + areaId2Name.get(unit.getCityId()) + areaId2Name.get(unit.getAreaId()));
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
