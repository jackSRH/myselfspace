package com.mailian.firecontrol.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Filter;
import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.core.util.TreeParser;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.AreaRank;
import com.mailian.firecontrol.dao.auto.mapper.AreaMapper;
import com.mailian.firecontrol.dao.auto.mapper.PrecinctMapper;
import com.mailian.firecontrol.dao.auto.mapper.UnitMapper;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.web.response.AreaResp;
import com.mailian.firecontrol.service.AreaService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<Area,AreaMapper> implements AreaService {

    @Resource
    private PrecinctMapper precinctMapper;
    @Resource
    private UnitMapper unitMapper;

    @Cacheable(value = CommonConstant.SYS_AREA_KEY)
    @Override
    public List<AreaResp> selectAll() {
        List<Area> areaList = selectByMap(null);

        List<AreaResp> areaResps = new ArrayList<>();
        AreaResp areaResp;
        for (Area area : areaList) {
            areaResp = new AreaResp();
            BeanUtils.copyProperties(area,areaResp);
            areaResps.add(areaResp);
        }
        return areaResps;
    }

    @Override
    public List<AreaResp> selectProvinceAndCityList(String areaName) {
        AreaService areaService = (AreaService) AopContext.currentProxy();
        List<AreaResp> areaResps = areaService.selectAll();
        areaResps = (List<AreaResp>)CollectionUtil.filter(areaResps, new Filter<AreaResp>() {
            @Override
            public boolean accept(AreaResp areaResp) {
                return AreaRank.PROVINCE.id.equals(areaResp.getAreaRank()) || AreaRank.CITY.id.equals(areaResp.getAreaRank());
            }
        });
        return TreeParser.getTreeListByFilter("0",areaResps,true,areaName);
    }

    @Cacheable(value = CommonConstant.SYS_AREA_KEY,key = "#areaId")
    @Override
    public Area getAreaById(Integer areaId) {
        return selectByPrimaryKey(areaId);
    }

    @Override
    public List<AreaResp> selectAreaAndPrecinctList(String areaName, DataScope dataScope) {
        AreaService areaService = (AreaService) AopContext.currentProxy();
        List<AreaResp> areaResps = areaService.selectAll();

        List<Precinct> precincts = StringUtils.isNull(dataScope)?precinctMapper.selectByMap(null):precinctMapper.selectBatchIds(dataScope.getDataIds());
        appendPrecincts(areaResps, precincts);
        return TreeParser.getTreeListByFilter("0",areaResps,true,areaName);
    }

    @Override
    public List<AreaResp> selectAreaAndUnitList(String areaName, DataScope dataScope,Integer showRank) {
        AreaService areaService = (AreaService) AopContext.currentProxy();
        List<AreaResp> areaResps = areaService.selectAll();
        Map<Integer,AreaResp> areaRespMap = new HashMap<>();
        for (AreaResp areaResp : areaResps) {
            if(areaResp.getAreaRank()>=showRank) {
                areaRespMap.put(areaResp.getId(), areaResp);
            }
        }
        
        List<AreaResp> newAreaList = new ArrayList<>();
        if(StringUtils.isNotNull(dataScope)){
            List<Integer> dataIds = dataScope.getDataIds();
            if(StringUtils.isNotEmpty(dataIds)) {
                if ("unit_id".equals(dataScope.getScopeName())) {
                    Unit unit = unitMapper.selectByPrimaryKey(dataIds.get(0));
                    Precinct precinct = precinctMapper.selectByPrimaryKey(unit.getPrecinctId());
                    appendPrecinctsExclude(newAreaList,Arrays.asList(precinct),areaRespMap);

                    appendUnits(newAreaList, Arrays.asList(unit));
                }else {
                    List<Precinct> precincts = precinctMapper.selectBatchIds(dataIds);
                    appendPrecinctsExclude(newAreaList, precincts, areaRespMap);

                    //添加对应单位
                    Map<String, Object> queryMap = new HashMap<>();
                    queryMap.put("precinctScope", dataScope);
                    List<Unit> units = unitMapper.selectByMap(queryMap);
                    appendUnits(newAreaList, units);
                }
            }
        }else{
            List<Precinct> precincts = precinctMapper.selectByMap(null);
            appendPrecinctsExclude(newAreaList, precincts, areaRespMap);

            List<Unit> units = unitMapper.selectByMap(null);
            appendUnits(newAreaList, units);
        }

        newAreaList = (List<AreaResp>)CollectionUtil.filter(newAreaList, new Filter<AreaResp>() {
            @Override
            public boolean accept(AreaResp areaResp) {
                return areaResp.getAreaRank().intValue()>=showRank;
            }
        });
        Integer minRank = null;
        for (AreaResp areaResp : newAreaList) {
            if(StringUtils.isNull(minRank) || areaResp.getAreaRank().intValue() < minRank.intValue()){
                minRank = areaResp.getAreaRank();
            }
        }
        if(StringUtils.isNotNull(minRank)){
            return TreeParser.getRankTreeListByFilter(minRank,newAreaList,true,areaName);
        }
        return new ArrayList<>();
    }

    private void appendPrecinctsExclude(List<AreaResp> newAreaList, List<Precinct> precincts, Map<Integer,AreaResp> areaRespMap) {
        AreaResp areaResp;
        int i=1;
        for (Precinct precinct : precincts) {
            areaResp = new AreaResp();
            areaResp.setId(precinct.getId());
            areaResp.setAreaName(precinct.getPrecinctName());
            areaResp.setAreaRank(AreaRank.PRECINCT.id);
            areaResp.setOrderNum(i);
            areaResp.setParentId(precinct.getAreaId());
            areaResp.setDisStr("P");
            if(areaRespMap.containsKey(precinct.getAreaId())){
                newAreaList.add(areaRespMap.get(precinct.getAreaId()));
                areaRespMap.remove(precinct.getAreaId());
            }

            if(areaRespMap.containsKey(precinct.getProvinceId())){
                newAreaList.add(areaRespMap.get(precinct.getProvinceId()));
                areaRespMap.remove(precinct.getProvinceId());
            }
            if(areaRespMap.containsKey(precinct.getCityId())){
                newAreaList.add(areaRespMap.get(precinct.getCityId()));
                areaRespMap.remove(precinct.getCityId());
            }
            newAreaList.add(areaResp);
            i++;
        }
    }

    /**
     * 追加单位至区域
     * @param areaResps
     * @param units
     */
    private void appendUnits(List<AreaResp> areaResps, List<Unit> units) {
        AreaResp areaResp;
        int i=1;
        String pDisStr = "P";
        String disStr = "U";
        for (Unit unit : units) {
            areaResp = new AreaResp();
            areaResp.setId(unit.getId());
            areaResp.setAreaName(unit.getUnitName());
            areaResp.setAreaRank(AreaRank.UNIT.id);
            areaResp.setOrderNum(i);
            areaResp.setParentId(unit.getPrecinctId());
            areaResp.setDisStr(disStr);
            areaResp.setParentDisStr(pDisStr);
            areaResps.add(areaResp);
            i++;
        }
    }

    /**
     * 追加管辖区至区域
     * @param areaResps
     * @param precincts
     */
    private void appendPrecincts(List<AreaResp> areaResps, List<Precinct> precincts) {
        AreaResp areaResp;
        int i=1;
        for (Precinct precinct : precincts) {
            areaResp = new AreaResp();
            areaResp.setId(precinct.getId());
            areaResp.setAreaName(precinct.getPrecinctName());
            areaResp.setAreaRank(AreaRank.PRECINCT.id);
            areaResp.setOrderNum(i);
            areaResp.setParentId(precinct.getAreaId());
            areaResp.setDisStr("P");
            areaResps.add(areaResp);
            i++;
        }
    }
}
