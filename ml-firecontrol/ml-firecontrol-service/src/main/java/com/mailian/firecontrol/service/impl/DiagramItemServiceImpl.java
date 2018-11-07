package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.DiagramItemMapper;
import com.mailian.firecontrol.dao.auto.model.DiagramItem;
import com.mailian.firecontrol.dao.manual.mapper.ManageManualMapper;
import com.mailian.firecontrol.dto.web.request.DiagramItemReq;
import com.mailian.firecontrol.service.DiagramItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiagramItemServiceImpl extends BaseServiceImpl<DiagramItem, DiagramItemMapper> implements DiagramItemService {

    @Resource
    private ManageManualMapper manageManualMapper;

    @Override
    public List<DiagramItem> getItemsByStructIds(List<Integer> structIds){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("dsScope",new DataScope("ds_id",structIds));
        return super.selectByMap(queryMap);
    }

    @Override
    public Boolean insert(List<DiagramItemReq> diagramItemReqs,Integer dsId){
        List<DiagramItem> diagramItems = new ArrayList<>();
        DiagramItem diagramItem;
        for(DiagramItemReq diagramItemReq : diagramItemReqs){
            diagramItem = new DiagramItem();
            BeanUtils.copyProperties(diagramItemReq,diagramItem);
            diagramItem.setDsId(dsId);
            super.setUserNameAndTime(diagramItem,false);
            diagramItems.add(diagramItem);
        }
        return super.insertBatch(diagramItems) > 0;
    }

    @Override
    public void deleteByDsId(Integer dsId){
        if(StringUtils.isEmpty(dsId)){
            return ;
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("dsId",dsId);
        manageManualMapper.deleteDiagramItemByMap(queryMap);
    }

}
