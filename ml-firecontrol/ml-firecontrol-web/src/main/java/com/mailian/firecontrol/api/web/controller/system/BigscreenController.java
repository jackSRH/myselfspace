package com.mailian.firecontrol.api.web.controller.system;

import com.google.common.base.Joiner;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.BigDecimalUtil;
import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.ItemBtype;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.UnitDevice;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemHistoryData;
import com.mailian.firecontrol.dto.web.request.BgSearchReq;
import com.mailian.firecontrol.dto.web.response.AlarmNumResp;
import com.mailian.firecontrol.dto.web.response.AreaResp;
import com.mailian.firecontrol.dto.web.response.AreaUnitMapResp;
import com.mailian.firecontrol.dto.web.response.BgUnitTrendListResp;
import com.mailian.firecontrol.dto.web.response.BgUnitTrendResp;
import com.mailian.firecontrol.dto.web.response.PieResp;
import com.mailian.firecontrol.service.AreaService;
import com.mailian.firecontrol.service.FacilitiesAlarmService;
import com.mailian.firecontrol.service.UnitDeviceService;
import com.mailian.firecontrol.service.UnitService;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import com.mailian.firecontrol.service.util.BuildDefaultResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@RestController
@RequestMapping("/system/bigscreen")
@Api(description = "大屏相关接口")
@WebAPI
public class BigscreenController {
    private static final List<Integer> UNIT_TREND_TYPES = Arrays.asList(
            ItemBtype.VOLTAGE.id,ItemBtype.ELECTRICCURRENT.id,
            ItemBtype.LEAKAGE.id,ItemBtype.CABLE_TEMPERATURE.id);
    @Autowired
    private UnitService unitService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private FacilitiesAlarmService facilitiesAlarmService;
    @Autowired
    private UnitDeviceService unitDeviceService;
    @Autowired
    private DeviceItemRepository deviceItemRepository;

    @ApiOperation(value = "获取省份城市级别，用于大屏运营商展示", httpMethod = "GET")
    @GetMapping(value = "getProvinceAndCityList")
    public ResponseResult<List<AreaResp>> getProvinceAndCityList(@ApiParam(value="区域名") @RequestParam(value = "areaName",required = false) String areaName){
        return ResponseResult.buildOkResult(areaService.selectProvinceAndCityList(areaName));
    }

    @ApiOperation(value = "获取管辖区数结构，用于大屏运营商展示", httpMethod = "GET")
    @GetMapping(value = "getAreaAndPrecinctList")
    public ResponseResult<List<AreaResp>> getAreaAndPrecinctList(@CurUser ShiroUser shiroUser,
                                                                 @ApiParam(value="区域名") @RequestParam(value = "areaName",required = false) String areaName){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult();
            }
            dataScope = new DataScope(precinctIds);
        }
        return ResponseResult.buildOkResult(areaService.selectAreaAndPrecinctList(areaName,dataScope));
    }


    @ApiOperation(value = "获取单位分布(运营商)", httpMethod = "GET")
    @RequestMapping(value="/getUnitDataByArea",method = RequestMethod.GET)
    public ResponseResult<PieResp> getUnitDataByArea(@ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId){
        return ResponseResult.buildOkResult(unitService.getUnitSpreadByAreaAndScope(areaId,null));
    }

    @ApiOperation(value = "获取单位分布(管辖区)", httpMethod = "GET")
    @RequestMapping(value="/getUnitDataByPrecinct",method = RequestMethod.GET)
    public ResponseResult<PieResp> getUnitDataByPrecinct(@CurUser ShiroUser shiroUser,
                                                     @ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                     @ApiParam(value = "管辖区id") @RequestParam(value = "precinctId",required = false) Integer precinctId){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isNotEmpty(precinctId)) {
                /*无数据权限*/
                if (!precinctIds.contains(precinctId)) {
                    return ResponseResult.buildOkResult(BuildDefaultResultUtil.buildDefaultPieResp(null));
                }
            }
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(BuildDefaultResultUtil.buildDefaultPieResp(null));
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        if(StringUtils.isNotEmpty(precinctId)){
            dataScope = new DataScope("precinct_id", Arrays.asList(precinctId));
        }
        return ResponseResult.buildOkResult(unitService.getUnitSpreadByAreaAndScope(areaId,dataScope));
    }


    @ApiOperation(value = "获取警情信息(运营商)", httpMethod = "GET")
    @RequestMapping(value="/getAlarmDataByArea",method = RequestMethod.GET)
    public ResponseResult<AlarmNumResp> getAlarmDataByArea(@ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId){
        return ResponseResult.buildOkResult(facilitiesAlarmService.getAlarmNumByAreaAndScope(areaId,null));
    }

    @ApiOperation(value = "获取警情信息(管辖区)", httpMethod = "GET")
    @RequestMapping(value="/getAlarmDataByPrecinct",method = RequestMethod.GET)
    public ResponseResult<AlarmNumResp> getAlarmDataByPrecinct(@CurUser ShiroUser shiroUser,
                                                     @ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                     @ApiParam(value = "管辖区id") @RequestParam(value = "precinctId",required = false) Integer precinctId){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isNotEmpty(precinctId)) {
                /*无数据权限*/
                if (!precinctIds.contains(precinctId)) {
                    return ResponseResult.buildOkResult(new AlarmNumResp());
                }
            }
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new AlarmNumResp());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        if(StringUtils.isNotEmpty(precinctId)){
            dataScope = new DataScope("precinct_id", Arrays.asList(precinctId));
        }
        return ResponseResult.buildOkResult(facilitiesAlarmService.getAlarmNumByAreaAndScope(areaId,dataScope));
    }


    @ApiOperation(value = "获取单位地图信息(运营商)", httpMethod = "GET")
    @RequestMapping(value="/getUnitMapDataByArea",method = RequestMethod.GET)
    public ResponseResult<AreaUnitMapResp> getUnitMapDataByArea(@ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                                @ApiParam(value = "单位类型") @RequestParam(value = "unitType",required = false) Integer unitType){
        return ResponseResult.buildOkResult(unitService.getUnitMapDataByAreaAndScope(areaId,unitType,null));
    }

    @ApiOperation(value = "获取单位地图信息(管辖区)", httpMethod = "GET")
    @RequestMapping(value="/getUnitMapDataByPrecinct",method = RequestMethod.GET)
    public ResponseResult<AreaUnitMapResp> getUnitMapDataByPrecinct(@CurUser ShiroUser shiroUser,
                                                               @ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                               @ApiParam(value = "管辖区id") @RequestParam(value = "precinctId",required = false) Integer precinctId,
                                                                 @ApiParam(value = "单位类型") @RequestParam(value = "unitType",required = false) Integer unitType){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isNotEmpty(precinctId)) {
                /*无数据权限*/
                if (!precinctIds.contains(precinctId)) {
                    return ResponseResult.buildOkResult(new AreaUnitMapResp());
                }
            }
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new AreaUnitMapResp());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        if(StringUtils.isNotEmpty(precinctId)){
            dataScope = new DataScope("precinct_id", Arrays.asList(precinctId));
        }
        return ResponseResult.buildOkResult(unitService.getUnitMapDataByAreaAndScope(areaId,unitType,dataScope));
    }


    @ApiOperation(value = "获取单位走势(单位)", httpMethod = "GET")
    @RequestMapping(value="/getUnitTrendByUnitId",method = RequestMethod.GET)
    public ResponseResult<List<BgUnitTrendListResp>> getUnitTrendByUnitId(BgSearchReq bgSearchReq) throws ParseException {
        List<BgUnitTrendListResp> bgUnitTrendListResps = new ArrayList<>();
        Integer dateType = bgSearchReq.getDateType();
        Date startDate = bgSearchReq.getStartDate();
        Date endDate = bgSearchReq.getEndDate();

        //获取时间点
        List<String> dates = new ArrayList<>();
        switch (dateType){
            case 1:
                dates = DateUtil.getHoursBetween(startDate,endDate);
                break;
            case 2:
                dates = DateUtil.getDaysBetween(startDate,endDate);
                break;
            case 3:
                dates = DateUtil.getMonthsBetween(startDate,endDate);
                break;
        }

        //初始化数据
        BgUnitTrendListResp bgUnitTrendListResp;
        List<BgUnitTrendResp> bgUnitTrendResps;
        for(Integer type : UNIT_TREND_TYPES){
            bgUnitTrendResps = new ArrayList<>();
            BgUnitTrendResp bgUnitTrendResp;
            for(String date : dates){
                bgUnitTrendResp = new BgUnitTrendResp();
                bgUnitTrendResp.setDate(date);
                float val = 0f;
                bgUnitTrendResp.setNum(BigDecimalUtil.keepTwoDecimals(val,2));
                bgUnitTrendResps.add(bgUnitTrendResp);
            }
            bgUnitTrendListResp = new BgUnitTrendListResp();
            bgUnitTrendListResp.setBtypeDesc(ItemBtype.getValue(type));
            bgUnitTrendListResp.setBgUnitTrends(bgUnitTrendResps);
            bgUnitTrendListResps.add(bgUnitTrendListResp);
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("unitId",bgSearchReq.getUnitId());
        List<UnitDevice> unitDevices = unitDeviceService.selectByMap(queryMap);
        if(StringUtils.isEmpty(unitDevices)){
            return ResponseResult.buildOkResult(bgUnitTrendListResps);
        }

        //获取单位下所有数据项
        List<String> deviceIds = new ArrayList<>();
        for(UnitDevice unitDevice : unitDevices){
            deviceIds.add(unitDevice.getDeviceId());
        }
        List<DeviceItem> items = new ArrayList<>();
        Map<String,List<DeviceItem>> did2TranItems =  deviceItemRepository.getDeviceItemInfosByCodes(deviceIds);
        if(StringUtils.isNotEmpty(did2TranItems)){
            for(Map.Entry<String,List<DeviceItem>> entry : did2TranItems.entrySet()){
                items.addAll(entry.getValue());
            }
        }
        Map<String, List<DeviceItem>> did2CalcItems = deviceItemRepository.getCalcItemsByDeviceCodes(deviceIds);
        if(StringUtils.isNotEmpty(did2CalcItems)){
            for(Map.Entry<String,List<DeviceItem>> entry : did2CalcItems.entrySet()){
                items.addAll(entry.getValue());
            }
        }
        if(StringUtils.isEmpty(items)){
            return ResponseResult.buildOkResult(bgUnitTrendListResps);
        }

        //查找类型和数据项的关系
        Map<Integer,List<String>> btype2ItemIds = new HashMap<>();
        List<String> needFindItemIds = new ArrayList<>();
        List<String> itemIds;
        Integer btype;
        String itemId;
        for(DeviceItem deviceItem : items){
            btype = deviceItem.getBtype();
            if(!UNIT_TREND_TYPES.contains(btype)){
                continue;
            }
            itemId = deviceItem.getId();
            needFindItemIds.add(itemId);
            itemIds = btype2ItemIds.containsKey(btype)?btype2ItemIds.get(btype):new ArrayList<>();
            itemIds.add(itemId);
            btype2ItemIds.put(btype,itemIds);
        }

        List<DeviceItemHistoryData> historyDatas = deviceItemRepository.getItemDataByItemIdAndTime(
                Joiner.on(",").join(needFindItemIds),DateUtil.toString(startDate) ,DateUtil.toString(endDate),dateType);
        if(StringUtils.isEmpty(historyDatas)){
            return ResponseResult.buildOkResult(bgUnitTrendListResps);
        }
        Map<String,List<DeviceItemHistoryData>> itemId2HistoryDatas = new HashMap<>();
        List<DeviceItemHistoryData> tempHistoryDatas ;
        for(DeviceItemHistoryData historyData :historyDatas){
            if(1 != historyData.getCtg()){
                continue;
            }
            itemId = historyData.getId();
            tempHistoryDatas = itemId2HistoryDatas.containsKey(itemId)?itemId2HistoryDatas.get(itemId):new ArrayList<>();
            tempHistoryDatas.add(historyData);
            itemId2HistoryDatas.put(itemId,tempHistoryDatas);
        }

        //数据项类型（btype） 找 数据项id(itemIds)
        //数据项id 找 历史数据（historyDatas）
        // 统计相同时间的数据
        bgUnitTrendListResps = new ArrayList<>();
        for(Integer type : UNIT_TREND_TYPES){
            bgUnitTrendResps = new ArrayList<>();
            BgUnitTrendResp bgUnitTrendResp;
            for(String date : dates){
                bgUnitTrendResp = new BgUnitTrendResp();
                bgUnitTrendResp.setDate(date);
                float val = 0f;
                List<String> deviceItemIds = btype2ItemIds.get(type);
                if(StringUtils.isNotEmpty(deviceItemIds)){
                    for(String deviceItemId:deviceItemIds){
                        List<DeviceItemHistoryData> deviceItemHistoryDatas =  itemId2HistoryDatas.get(deviceItemId);
                        if(StringUtils.isNotEmpty(deviceItemHistoryDatas)){
                            for(DeviceItemHistoryData deviceItemHistoryData : deviceItemHistoryDatas){
                                if(DateUtil.toString(deviceItemHistoryData.getTm()).contains(date)){
                                    val += deviceItemHistoryData.getVal();
                                }
                            }
                        }
                    }
                }
                bgUnitTrendResp.setNum(BigDecimalUtil.keepTwoDecimals(val,2));
                bgUnitTrendResps.add(bgUnitTrendResp);
            }
            bgUnitTrendListResp = new BgUnitTrendListResp();
            bgUnitTrendListResp.setBtypeDesc(ItemBtype.getValue(type));
            bgUnitTrendListResp.setBgUnitTrends(bgUnitTrendResps);
            bgUnitTrendListResps.add(bgUnitTrendListResp);
        }
        return ResponseResult.buildOkResult(bgUnitTrendListResps);
    }


}
