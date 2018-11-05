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
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.dao.auto.model.Precinct;
import com.mailian.firecontrol.dto.web.response.AreaResp;
import com.mailian.firecontrol.service.AreaService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/31
 * @Description:
 */
@Service
public class AreaServiceImpl extends BaseServiceImpl<Area,AreaMapper> implements AreaService {

    @Resource
    private PrecinctMapper precinctMapper;

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
        CollectionUtil.filter(areaResps, new Filter<AreaResp>() {
            @Override
            public boolean accept(AreaResp areaResp) {
                return AreaRank.AREA.id.equals(areaResp.getAreaRank());
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

        if(StringUtils.isNotNull(dataScope)){
            List<Precinct> precincts = precinctMapper.selectBatchIds(dataScope.getDataIds());
            appendPrecincts(areaResps, precincts);
        }else{
            List<Precinct> precincts = precinctMapper.selectByMap(null);
            appendPrecincts(areaResps, precincts);
        }
        return TreeParser.getTreeListByFilter("0",areaResps,true,areaName);
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
            areaResp.setAreaRank(AreaRank.OTHER.id);
            areaResp.setOrderNum(i);
            areaResp.setParentId(precinct.getAreaId());
            areaResp.setDisStr("P");
            areaResps.add(areaResp);
            i++;
        }
    }
}
