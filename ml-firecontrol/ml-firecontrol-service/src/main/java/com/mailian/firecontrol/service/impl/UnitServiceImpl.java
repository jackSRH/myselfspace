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
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.web.UnitInfo;
import com.mailian.firecontrol.dto.web.response.UnitListResp;
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
    private UnitMapper unitMapper;
    @Resource
    private PrecinctMapper precinctMapper;


    @Override
    public PageBean<UnitListResp> getUnitList(List<Integer> precinctIds, String unitName, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.offsetPage(pageNo,pageSize);
        page.setOrderBy("update_time desc");
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("precinctScope",new DataScope("precinct_id",precinctIds));
        if(StringUtils.isNotEmpty(unitName)){
            queryMap.put("unitName",unitName);
        }
        List<Unit> units = unitMapper.selectByMap(queryMap);

        List<Precinct> precincts = precinctMapper.selectBatchIds(precinctIds);
        Map<Integer,String> precinetId2Name = new HashMap<>();
        for(Precinct precinct : precincts){
            precinetId2Name.put(precinct.getId(),precinct.getPrecinctName());
        }

        //TODO 通过节点找所有父节点信息
        Set<Integer> areaIds = new HashSet<>();
        for(Unit unit:units){
            areaIds.add(unit.getAreaId());
        }

        List<UnitListResp> unitListResps = new ArrayList<>();
        UnitListResp unitListResp;
        for(Unit unit:units){
            unitListResp = new UnitListResp();
            BeanUtils.copyProperties(unit,unitListResp);
            unitListResp.setPrecinct(precinetId2Name.get(unit.getPrecinctId()));
            unitListResps.add(unitListResp);
        }

        PageBean<UnitListResp> pageBean = new PageBean<>(pageNo,pageSize,(int)page.getTotal(),unitListResps);
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
