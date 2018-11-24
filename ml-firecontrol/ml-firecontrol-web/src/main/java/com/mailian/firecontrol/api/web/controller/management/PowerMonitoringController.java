package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.model.PowerMonitoring;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.web.request.PowerMonitoringReq;
import com.mailian.firecontrol.dto.web.response.DeviceItemResp;
import com.mailian.firecontrol.dto.web.response.LoadComparedResp;
import com.mailian.firecontrol.dto.web.response.PowerMonitoringResp;
import com.mailian.firecontrol.dto.web.response.VoltageComparedResp;
import com.mailian.firecontrol.service.PowerMonitoringService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/management/powerMonitoring")
@Api(description = "用电监测配置相关接口")
@WebAPI
public class PowerMonitoringController extends BaseController {

    @Resource
    private PowerMonitoringService powerMonitoringService;
    @Resource
    private DeviceItemRepository deviceItemRepository;

    @Log(title = "配置管理",action = "新增更新用电监测配置")
    @ApiOperation(value = "新增更新用电监测配置", httpMethod = "POST")
    @RequestMapping(value="/insertOrUpdate",method = RequestMethod.POST)
    public ResponseResult insertOrUpdate(@RequestBody PowerMonitoringReq powerMonitoringReq){
        if(StringUtils.isNull(powerMonitoringReq)){
            return error("参数不能为空");
        }

        return powerMonitoringService.insertOrUpdate(powerMonitoringReq);
    }

    @Log(title = "配置管理",action = "通过单位id获取用电监测配置")
    @ApiOperation(value = "通过单位id获取用电监测配置", httpMethod = "GET")
    @RequestMapping(value="/getByUnitId",method = RequestMethod.GET)
    public ResponseResult<List<PowerMonitoringResp>> getByUnitId(@ApiParam(value = "单位id",required = true) @RequestParam(value = "unitId") Integer unitId){
        if(StringUtils.isEmpty(unitId)){
            return error("单位id不能为空");
        }

        Map<String,Object> query = new HashMap<>();
        query.put("unitId",unitId);
        List<PowerMonitoring> powerMonitorings = powerMonitoringService.selectByMap(query);
        List<PowerMonitoringResp> powerMonitoringResps = new ArrayList<>();
        if(StringUtils.isEmpty(powerMonitorings)){
            return ResponseResult.buildOkResult(powerMonitoringResps);
        }

        List<String> itemIds = new ArrayList<>();
        PowerMonitoringResp powerMonitoringResp;
        for(PowerMonitoring powerMonitoring : powerMonitorings){
            powerMonitoringResp = new PowerMonitoringResp();
            BeanUtils.copyProperties(powerMonitoring,powerMonitoringResp);

            if(StringUtils.isNotEmpty(powerMonitoringResp.getLoadItemIds())){
                itemIds.addAll(powerMonitoringResp.getLoadItemIds());
            }
            if(StringUtils.isNotEmpty(powerMonitoringResp.getVoltageItemIds())){
                itemIds.addAll(powerMonitoringResp.getVoltageItemIds());
            }
            if(StringUtils.isNotEmpty(powerMonitoringResp.getEcItemIds())){
                itemIds.addAll(powerMonitoringResp.getEcItemIds());
            }
            if(StringUtils.isNotEmpty(powerMonitoringResp.getPfItemIds())){
                itemIds.addAll(powerMonitoringResp.getPfItemIds());
            }
            if(StringUtils.isNotEmpty(powerMonitoringResp.getCtItemIds())){
                itemIds.addAll(powerMonitoringResp.getCtItemIds());
            }
            if(StringUtils.isNotEmpty(powerMonitoringResp.getLcItemIds())){
                itemIds.addAll(powerMonitoringResp.getLcItemIds());
            }
        }

        Map<String, DeviceItem> itemId2Item = deviceItemRepository.getDeviceItemInfosByItemIds(itemIds);

        DeviceItemResp itemResp;
        DeviceItem deviceItem;
        Map<String,DeviceItemResp> itemId2ItemResp = new HashMap<>();
        for(Map.Entry<String, DeviceItem> entry : itemId2Item.entrySet()){
            itemResp = new DeviceItemResp();
            deviceItem = entry.getValue();
            itemResp.setId(deviceItem.getId());
            itemResp.setName(deviceItem.getShortname());
            itemResp.setShortname(deviceItem.getDisplayname());
            itemId2ItemResp.put(entry.getKey(),itemResp);
        }

        List<DeviceItemResp> items;
        for(PowerMonitoring powerMonitoring : powerMonitorings){
            powerMonitoringResp = new PowerMonitoringResp();
            BeanUtils.copyProperties(powerMonitoring,powerMonitoringResp);

            if(StringUtils.isNotEmpty(powerMonitoringResp.getLoadItemIds())){
                items = new ArrayList<>();
                for(String itemId:powerMonitoringResp.getLoadItemIds()){
                    if(StringUtils.isNotNull(itemId2ItemResp.get(itemId))){
                        items.add(itemId2ItemResp.get(itemId));
                    }
                }
                powerMonitoringResp.setLoadItemInfos(items);
            }

            if(StringUtils.isNotEmpty(powerMonitoringResp.getVoltageItemIds())){
                items = new ArrayList<>();
                for(String itemId:powerMonitoringResp.getVoltageItemIds()){
                    if(StringUtils.isNotNull(itemId2ItemResp.get(itemId))){
                        items.add(itemId2ItemResp.get(itemId));

                    }
                }
                powerMonitoringResp.setVoltageItemInfos(items);
            }

            if(StringUtils.isNotEmpty(powerMonitoringResp.getEcItemIds())){
                items = new ArrayList<>();
                for(String itemId:powerMonitoringResp.getEcItemIds()){
                    if(StringUtils.isNotNull(itemId2ItemResp.get(itemId))){
                        items.add(itemId2ItemResp.get(itemId));
                    }
                }
                powerMonitoringResp.setEcItemInfos(items);
            }

            if(StringUtils.isNotEmpty(powerMonitoringResp.getPfItemIds())){
                items = new ArrayList<>();
                for(String itemId:powerMonitoringResp.getPfItemIds()){
                    if(StringUtils.isNotNull(itemId2ItemResp.get(itemId))){
                        items.add(itemId2ItemResp.get(itemId));
                    }
                }
                powerMonitoringResp.setPfItemInfos(items);
            }

            if(StringUtils.isNotEmpty(powerMonitoringResp.getCtItemIds())){
                items = new ArrayList<>();
                for(String itemId:powerMonitoringResp.getCtItemIds()){
                    if(StringUtils.isNotNull(itemId2ItemResp.get(itemId))){
                        items.add(itemId2ItemResp.get(itemId));
                    }
                }
                powerMonitoringResp.setCtItemInfos(items);
            }

            if(StringUtils.isNotEmpty(powerMonitoringResp.getLcItemIds())){
                items = new ArrayList<>();
                for(String itemId:powerMonitoringResp.getLcItemIds()){
                    if(StringUtils.isNotNull(itemId2ItemResp.get(itemId))){
                        items.add(itemId2ItemResp.get(itemId));
                    }
                }
                powerMonitoringResp.setLcItemInfos(items);
            }
            powerMonitoringResps.add(powerMonitoringResp);
        }
        return ResponseResult.buildOkResult(powerMonitoringResps);
    }

    @Log(title = "配置管理",action = "删除用电监测配置")
    @ApiOperation(value = "删除用电监测配置", httpMethod = "DELETE")
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    public ResponseResult delete(@ApiParam(value = "用电监测id",required = true) @RequestParam("id") Integer id){
        if(StringUtils.isEmpty(id)){
            return error("id不能为空");
        }
        PowerMonitoring powerMonitoring = powerMonitoringService.selectByPrimaryKey(id);
        if(StringUtils.isNull(powerMonitoring)){
            return error("用电监控配置不存在，请检查");
        }
        int deleteRes = powerMonitoringService.deleteByPrimaryKey(id);
        return deleteRes > 0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @Log(title = "配置管理",action = "获取（负荷、功率因数、漏电电流）今昨对比")
    @ApiOperation(value = "获取（负荷、功率因数、漏电电流）今昨对比", httpMethod = "GET")
    @RequestMapping(value="/getLoadCompared",method = RequestMethod.GET)
    public ResponseResult<LoadComparedResp> getLoadCompared(@ApiParam(value = "数据项id",required = true) @RequestParam("itemIds") String itemIds){
        if(StringUtils.isEmpty(itemIds)){
            return ResponseResult.buildOkResult(null);
        }
        List<String> itemIdList = Arrays.asList(itemIds.split(","));
        if(1 != itemIdList.size()){
            return error("数据项超过一个，请联系配置人员修改");
        }

        LoadComparedResp loadComparedResp = powerMonitoringService.getLoadCompared(itemIdList.get(0));
        return ResponseResult.buildOkResult(loadComparedResp);
    }

    @Log(title = "配置管理",action = "获取（电压、电流、线缆温度）今昨对比")
    @ApiOperation(value = "获取（电压、电流、线缆温度）今昨对比", httpMethod = "GET")
    @RequestMapping(value="/getVoltageCompared",method = RequestMethod.GET)
    public ResponseResult<VoltageComparedResp> getVoltageCompared(@ApiParam(value = "数据项id",required = true) @RequestParam("itemIds") String itemIds){
        if(StringUtils.isEmpty(itemIds)){
            return ResponseResult.buildOkResult(null);
        }

        VoltageComparedResp voltageComparedResp = powerMonitoringService.getVoltageCompared(itemIds);
        return ResponseResult.buildOkResult(voltageComparedResp);
    }





}
