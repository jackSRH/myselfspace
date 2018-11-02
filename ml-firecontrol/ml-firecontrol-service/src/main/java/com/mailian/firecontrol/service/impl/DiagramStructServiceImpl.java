package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.SerialNumModule;
import com.mailian.firecontrol.common.enums.StructType;
import com.mailian.firecontrol.dao.auto.mapper.DiagramStructMapper;
import com.mailian.firecontrol.dao.auto.model.DiagramItem;
import com.mailian.firecontrol.dao.auto.model.DiagramStruct;
import com.mailian.firecontrol.dao.auto.model.Facilities;
import com.mailian.firecontrol.dao.auto.model.Unit;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.web.request.DiagramStructReq;
import com.mailian.firecontrol.dto.web.response.DiagramItemResp;
import com.mailian.firecontrol.dto.web.response.DiagramStructResp;
import com.mailian.firecontrol.service.DiagramItemService;
import com.mailian.firecontrol.service.DiagramStructService;
import com.mailian.firecontrol.service.FacilitiesService;
import com.mailian.firecontrol.service.SerialNumberService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiagramStructServiceImpl extends BaseServiceImpl<DiagramStruct, DiagramStructMapper> implements DiagramStructService {

    @Resource
    private SerialNumberService serialNumberService;
    @Resource
    private DiagramItemService diagramItemService;
    @Resource
    private DeviceItemRepository deviceItemRepository;
    @Resource
    private FacilitiesService facilitiesService;
    @Resource
    private UnitService unitService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insert(DiagramStructReq diagramStructReq,String picPath){
        DiagramStruct diagramStruct = new DiagramStruct();
        BeanUtils.copyProperties(diagramStructReq,diagramStruct);

        //监控设施和遥控设置 分别设置不同的数据
        Integer faId = diagramStructReq.getFacilitiesId();
        if(StringUtils.isNotEmpty(faId)){
            diagramStruct.setStructType(StructType.FACILITY.id);
            Facilities facilities = facilitiesService.selectByPrimaryKey(faId);
            if(StringUtils.isNotNull(facilities)){
                diagramStruct.setUnitId(facilities.getUnitId());
                diagramStruct.setPrecinctId(facilities.getPrecinctId());
                diagramStruct.setFaSystemId(facilities.getFaSystemId());
                diagramStruct.setStructAddress(diagramStructReq.getStructName());
            }
        }else{
            diagramStruct.setStructType(StructType.REMOTE.id);
            if(StringUtils.isNotEmpty(diagramStructReq.getUnitId())){
                Unit unit = unitService.selectByPrimaryKey(diagramStruct.getUnitId());
                if(StringUtils.isNotNull(unit)){
                    diagramStruct.setPrecinctId(unit.getPrecinctId());
                }
            }
        }

        //添加图片
        if(StringUtils.isNotEmpty(picPath)){
            diagramStruct.setStructPic(picPath);
        }
        String structIdStr = serialNumberService.generateSerialNumberByModelCode(SerialNumModule.SYS_DIAGRAM_STRUCT);
        Integer structId = Integer.valueOf(structIdStr);
        if(StringUtils.isNotEmpty(diagramStructReq.getDiagramItems())){
           Boolean itemInsertRes = diagramItemService.insert(diagramStructReq.getDiagramItems(),structId);
           if(!itemInsertRes){
               return false;
           }
        }
        diagramStruct.setId(structId);
        return super.insert(diagramStruct) > 0;
    }

    @Override
    public List<DiagramStructResp> getDiagramStructByMap(Map<String,Object> queryMap){
        List<DiagramStruct> diagramStructs = super.selectByMap(queryMap);
        List<DiagramStructResp> diagramStructResps = new ArrayList<>();
        if(StringUtils.isEmpty(diagramStructs)){
            return diagramStructResps;
        }

        List<Integer> structIds = new ArrayList<>();
        Map<Integer,List<DiagramItemResp>> dsId2DiagramItems = new HashMap<>();
        for(DiagramStruct diagramStruct : diagramStructs){
            structIds.add(diagramStruct.getId());
            dsId2DiagramItems.put(diagramStruct.getId(),new ArrayList<>());
        }

        //查找模块底下的数据项
        List<DiagramItem> diagramItems = diagramItemService.getItemsByStructIds(structIds);
        if(StringUtils.isNotEmpty(diagramItems)){
            List<String> itemIds = new ArrayList<>();
            for(DiagramItem diagramItem : diagramItems){
                itemIds.add(diagramItem.getItemId());
            }
            Map<String, DeviceItem> deviceItemMap = deviceItemRepository.getDeviceItemInfosByItemIds(itemIds);

            List<DiagramItemResp> diagramItemResps;
            DiagramItemResp diagramItemResp;
            DeviceItem deviceItem;
            for(DiagramItem diagramItem : diagramItems){
                diagramItemResps = dsId2DiagramItems.get(diagramItem.getDsId());
                if(StringUtils.isNotNull(diagramItemResps)){
                    diagramItemResp = new DiagramItemResp();
                    diagramItemResp.setItemId(diagramItem.getItemId());
                    diagramItemResp.setDisplay(diagramItem.getDisplay());
                    deviceItem = deviceItemMap.get(diagramItem.getItemId());
                    if(StringUtils.isNotEmpty(deviceItemMap) && StringUtils.isNotNull(deviceItem)){
                        diagramItemResp.setItemName(deviceItem.getShortname());
                    }
                    diagramItemResps.add(diagramItemResp);
                }
            }
        }

        DiagramStructResp diagramStructResp;
        for(DiagramStruct diagramStruct:diagramStructs){
            diagramStructResp = new DiagramStructResp();
            BeanUtils.copyProperties(diagramStruct,diagramStructResp);
            diagramStructResp.setDiagramItems(dsId2DiagramItems.get(diagramStruct.getId()));
            diagramStructResps.add(diagramStructResp);
        }
        return diagramStructResps;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean delete(Integer dsId){
        diagramItemService.deleteByDsId(dsId);
        return super.deleteByPrimaryKey(dsId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(DiagramStructReq diagramStructReq,String picPath){
        Boolean deleteRes = delete(diagramStructReq.getId());
        if(!deleteRes){
            return false;
        }
        return insert(diagramStructReq,picPath);
    }


}
