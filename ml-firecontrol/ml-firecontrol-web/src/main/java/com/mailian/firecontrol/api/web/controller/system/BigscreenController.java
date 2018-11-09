package com.mailian.firecontrol.api.web.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.mailian.core.annotation.CurUser;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.db.DataScope;
import com.mailian.core.util.BigDecimalUtil;
import com.mailian.core.util.DateUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AlarmMisreport;
import com.mailian.firecontrol.common.enums.ItemBtype;
import com.mailian.firecontrol.common.enums.ReqDateType;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dto.DayTime;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemHistoryData;
import com.mailian.firecontrol.dto.web.request.BgSearchReq;
import com.mailian.firecontrol.dto.web.response.*;
import com.mailian.firecontrol.framework.util.ConvertDateUtil;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/5
 * @Description:
 */
@RestController
@RequestMapping("/system/bigscreen")
@Api(description = "大屏相关接口")
@WebAPI
public class BigscreenController extends BaseController {
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

    @ApiOperation(value = "获取管辖区数结构，用于大屏管辖区展示", httpMethod = "GET")
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


    @ApiOperation(value = "获取单位数结构，用于大屏单位展示", httpMethod = "GET")
    @GetMapping(value = "getAreaAndUnitList")
    public ResponseResult<List<AreaResp>> getAreaAndUnitList(@CurUser ShiroUser shiroUser,
                                                                 @ApiParam(value="区域名") @RequestParam(value = "areaName",required = false) String areaName){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())){
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult();
            }
            dataScope = new DataScope("precinct_id",precinctIds);
        }
        return ResponseResult.buildOkResult(areaService.selectAreaAndUnitList(areaName,dataScope));
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
    public ResponseResult<List<BgUnitTrendListResp>> getUnitTrendByUnitId(@CurUser ShiroUser shiroUser, BgSearchReq bgSearchReq){
        bgSearchReq.setUnitId(StringUtils.nvl(bgSearchReq.getUnitId(),shiroUser.getUnitId()));
        if(StringUtils.isEmpty(bgSearchReq.getUnitId())){
            return error("单位不存在");
        }
        List<BgUnitTrendListResp> bgUnitTrendListResps = new ArrayList<>();
        Integer dateType = bgSearchReq.getDateType();
        Integer itemCycle = null;
        Date now = new Date();
        Date startDate = DateUtil.getStartDate(StringUtils.nvl(bgSearchReq.getStartDate(),now));
        Date endDate = DateUtil.getEndDate(StringUtils.nvl(bgSearchReq.getEndDate(),now));

        //获取时间点
        List<DayTime> dates;
        if(ReqDateType.DAY.id.equals(dateType)){
            dates = ConvertDateUtil.getHoursBetween(startDate,endDate);
            startDate = DateUtil.getDateAfterHour(startDate,new BigDecimal(1));
            endDate = DateUtil.getDateAfterHour(endDate,new BigDecimal(1));
            itemCycle = ReqDateType.DAY.itemCycle;
        }else if(ReqDateType.WEEK.id.equals(dateType) || ReqDateType.MONTH.id.equals(dateType)){
            dates = ConvertDateUtil.getDaysBetween(startDate,endDate);
            itemCycle = ReqDateType.WEEK.itemCycle;
        }else if(ReqDateType.YEAR.id.equals(dateType)){
            dates = ConvertDateUtil.getMonthsBetween(startDate,endDate);
            itemCycle = ReqDateType.YEAR.itemCycle;
        }else{
            return error("时间类型有误");
        }

        String mapkey = "%s&%s";
        //初始化数据
        BgUnitTrendListResp bgUnitTrendListResp;
        List<BgUnitTrendResp> bgUnitTrendResps;
        Map<String,BgUnitTrendResp> typeDayMap = new HashMap<>();
        for(Integer type : UNIT_TREND_TYPES){
            bgUnitTrendResps = new ArrayList<>();
            BgUnitTrendResp bgUnitTrendResp;
            for(DayTime dayTime : dates){
                bgUnitTrendResp = new BgUnitTrendResp();
                bgUnitTrendResp.setDate(dayTime.getShowTime());
                bgUnitTrendResp.setNum(0D);

                String keyStr = String.format(mapkey,type,dayTime.getRealTime());
                typeDayMap.put(keyStr,bgUnitTrendResp);
                bgUnitTrendResps.add(bgUnitTrendResp);
            }
            bgUnitTrendListResp = new BgUnitTrendListResp();
            bgUnitTrendListResp.setBtype(type);
            bgUnitTrendListResp.setBgUnitTrends(bgUnitTrendResps);
            bgUnitTrendListResps.add(bgUnitTrendListResp);
        }

        List<String> deviceIds = unitService.getDevicesByUnitId(bgSearchReq.getUnitId());
        if(StringUtils.isEmpty(deviceIds)){
            return ResponseResult.buildOkResult(bgUnitTrendListResps);
        }

        //获取单位下所有数据项
        List<DeviceItem> items = new ArrayList<>();
        Map<String,List<DeviceItem>> deviceItemMap =  deviceItemRepository.getDeviceItemInfosByCodes(deviceIds);
        if(StringUtils.isNotEmpty(deviceItemMap)){
            for (List<DeviceItem> deviceItems : deviceItemMap.values()) {
                items.addAll(deviceItems);
            }
        }
        Map<String, List<DeviceItem>> deviceCalcItemMap = deviceItemRepository.getCalcItemsByDeviceCodes(deviceIds);
        if(StringUtils.isNotEmpty(deviceCalcItemMap)){
            for (List<DeviceItem> deviceItems : deviceCalcItemMap.values()) {
                items.addAll(deviceItems);
            }
        }
        if(StringUtils.isEmpty(items)){
            return ResponseResult.buildOkResult(bgUnitTrendListResps);
        }

        //查找类型和数据项的关系
        Map<String,Integer> itemTypeMap = new HashMap<>();
        List<String> needFindItemIds = new ArrayList<>();
        Integer btype;
        String itemId;
        for(DeviceItem deviceItem : items){
            btype = deviceItem.getBtype();
            if(!UNIT_TREND_TYPES.contains(btype)){
                continue;
            }
            itemId = deviceItem.getId();
            needFindItemIds.add(itemId);
            itemTypeMap.put(itemId,btype);
        }

        List<DeviceItemHistoryData> historyDatas = deviceItemRepository.getItemDataByItemIdAndTime(
                CollectionUtil.join(needFindItemIds,","),DateUtil.toString(startDate) ,DateUtil.toString(endDate),itemCycle);
        if(StringUtils.isEmpty(historyDatas)){
            return ResponseResult.buildOkResult(bgUnitTrendListResps);
        }
        for(DeviceItemHistoryData historyData :historyDatas){
            if(1 != historyData.getCtg()){
                continue;
            }
            itemId = historyData.getId();
            Integer type = itemTypeMap.get(itemId);
            String dateStr = "";
            if(ReqDateType.DAY.id.equals(dateType)){
                dateStr = DateUtil.format(historyData.getTm(),DateUtil.DATE_FORMAT_FOR_YMDH);
            }else if(ReqDateType.WEEK.id.equals(dateType) || ReqDateType.MONTH.id.equals(dateType)){
                dateStr = DateUtil.format(historyData.getTm(),DateUtil.DATE_FORMAT_FOR_YMD);
            }else if(ReqDateType.YEAR.id.equals(dateType)){
                dateStr = DateUtil.format(historyData.getTm(),DateUtil.DATE_FORMAT_FOR_YM);
            }
            String keyStr = String.format(mapkey,type,dateStr);
            if(typeDayMap.containsKey(keyStr)){
                BgUnitTrendResp bgUnitTrendResp = typeDayMap.get(keyStr);
                Double result = BigDecimalUtil.add(bgUnitTrendResp.getNum(),StringUtils.nvl(historyData.getVal(),0f));
                bgUnitTrendResp.setNum(BigDecimalUtil.round(result,2));
            }
        }
        return ResponseResult.buildOkResult(bgUnitTrendListResps);
    }


    @ApiOperation(value = "获取警情分析(运营商)", httpMethod = "GET")
    @RequestMapping(value="/getAlarmAnalysis",method = RequestMethod.GET)
    public ResponseResult<AlarmAnalysisResp> getAlarmAnalysis(@ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                              @ApiParam(value = "日期类型  1日,2周,3月") @RequestParam(value = "dateType",required = false,defaultValue = "1") Integer dateType){
        return ResponseResult.buildOkResult(facilitiesAlarmService.getAlarmAnalysisByAreaAndScope(areaId,dateType,null,null));
    }

    @ApiOperation(value = "获取警情分析(管辖区)", httpMethod = "GET")
    @RequestMapping(value="/getAlarmAnalysisByPrecinct",method = RequestMethod.GET)
    public ResponseResult<AlarmAnalysisResp> getAlarmAnalysisByPrecinct(@CurUser ShiroUser shiroUser,
                                                                    @ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId,
                                                                    @ApiParam(value = "管辖区id") @RequestParam(value = "precinctId",required = false) Integer precinctId,
                                                                        @ApiParam(value = "日期类型  1日,2周,3月") @RequestParam(value = "dateType",required = false,defaultValue = "1") Integer dateType){
        DataScope dataScope = null;
        if(!SystemManager.isAdminRole(shiroUser.getRoles())) {
            List<Integer> precinctIds = shiroUser.getPrecinctIds();
            if(StringUtils.isNotEmpty(precinctId)) {
                /*无数据权限*/
                if (!precinctIds.contains(precinctId)) {
                    return ResponseResult.buildOkResult(new AlarmAnalysisResp());
                }
            }
            if(StringUtils.isEmpty(precinctIds)){
                return ResponseResult.buildOkResult(new AlarmAnalysisResp());
            }
            dataScope = new DataScope("precinct_id", precinctIds);
        }
        if(StringUtils.isNotEmpty(precinctId)){
            dataScope = new DataScope("precinct_id", Arrays.asList(precinctId));
        }
        return ResponseResult.buildOkResult(facilitiesAlarmService.getAlarmAnalysisByAreaAndScope(areaId,dateType, AlarmMisreport.EFFECTIVE.id,dataScope));
    }


    @ApiOperation(value = "获取火警信息统计", httpMethod = "GET")
    @RequestMapping(value="/getFireAlarmCount",method = RequestMethod.GET)
    public ResponseResult<FireAlarmCountResp> getFireAlarmCount(@CurUser ShiroUser shiroUser,
                                                               @ApiParam(value = "区域id") @RequestParam(value = "areaId",required = false) Integer areaId){
        return ResponseResult.buildOkResult(facilitiesAlarmService.getFireAlarmCountByArea(areaId));
    }

}
