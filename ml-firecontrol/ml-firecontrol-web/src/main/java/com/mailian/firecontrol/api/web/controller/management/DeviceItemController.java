package com.mailian.firecontrol.api.web.controller.management;

import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.ExcelConstant;
import com.mailian.firecontrol.common.enums.ItemBtype;
import com.mailian.firecontrol.common.enums.ItemStype;
import com.mailian.firecontrol.common.util.ComputeUtil;
import com.mailian.firecontrol.common.util.ExcelUtil;
import com.mailian.firecontrol.common.util.ParseXlsUtils;
import com.mailian.firecontrol.common.util.StringUtil;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.push.DeviceItemRealTimeData;
import com.mailian.firecontrol.dto.push.DeviceSub;
import com.mailian.firecontrol.dto.push.YcTran;
import com.mailian.firecontrol.dto.push.YkTran;
import com.mailian.firecontrol.dto.push.YxTran;
import com.mailian.firecontrol.dto.web.request.DeviceItemInfo;
import com.mailian.firecontrol.dto.web.request.YcCalcInfo;
import com.mailian.firecontrol.dto.web.request.YcStoreInfo;
import com.mailian.firecontrol.dto.web.request.YcTranInfo;
import com.mailian.firecontrol.dto.web.request.YkTranInfo;
import com.mailian.firecontrol.dto.web.request.YxCalcInfo;
import com.mailian.firecontrol.dto.web.request.YxTranInfo;
import com.mailian.firecontrol.dto.web.response.ItemBtypeResp;
import com.mailian.firecontrol.service.DeviceItemOpertionService;
import com.mailian.firecontrol.service.ExcelService;
import com.mailian.firecontrol.service.cache.DeviceSubCache;
import com.mailian.firecontrol.service.repository.DeviceItemRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/management/deviceItem")
@Api(description = "数据项信息相关接口")
@WebAPI
public class DeviceItemController extends BaseController {
    private static final Integer COLUMNWIDTH = 30;
    @Resource
    private DeviceItemOpertionService deviceItemOpertionService;
    @Resource
    private DeviceItemRepository deviceItemRepository;
    @Resource
    private ExcelService excelService;
    @Resource
    private DeviceSubCache deviceSubCache;

    @ApiOperation(value = "通过单位id获取可配置的数据项", httpMethod = "GET")
    @GetMapping(value = "/getConfigItemsBySid")
    public ResponseResult<Map<String, Object>> getConfigItemsBySid(@ApiParam(value = "单位id") @RequestParam(value = "sid")  Integer sid)
            throws ExecutionException, InterruptedException {
        if(StringUtils.isEmpty(sid)){
            return error("单位id不能为空");
        }
        return  ResponseResult.buildOkResult(deviceItemOpertionService.getConfigItemsByUnitId(sid));
    }

    @ApiOperation(value = "通过网关id获取该网关下所有数据项", httpMethod = "GET")
    @GetMapping(value = "/getItemsByDeviceCode")
    public ResponseResult<Map<String, List<DeviceItem>>> getItemsByDeviceCode(@ApiParam(value = "网关id") @RequestParam(value = "code") String code){
        if(StringUtils.isEmpty(code)){
            return error("网关id为空");
        }
        List<DeviceItem> items = deviceItemOpertionService.getAllItemsByDeviceCodes(Arrays.asList(code));
        if(StringUtils.isEmpty(items)){
            return ResponseResult.buildOkResult(new HashMap<>());
        }
        return  ResponseResult.buildOkResult(deviceItemOpertionService.getType2ItemsData(items));
    }


    @ApiOperation(value = "通过数据项id列表获取所有数据项信息", httpMethod = "POST")
    @RequestMapping(value = "/getItemInfosByItemIds",method = RequestMethod.POST)
    public ResponseResult<Map<String, DeviceItem>> getItemInfosByItemIds(@ApiParam(name="itemIds",value = "数据项id列表",required = true) @RequestBody List<String> itemIds) {
        if(StringUtils.isEmpty(itemIds)){
            return error("数据项id列表为空");
        }
        return  ResponseResult.buildOkResult(deviceItemRepository.getDeviceItemInfosByItemIds(itemIds));
    }

    @ApiOperation(value = "通过RTU获取传输库数据项", httpMethod = "GET")
    @GetMapping(value = "/getItemsByDeviceSub")
    public ResponseResult<Map<String, List<DeviceItem>>> getItemsByDeviceSub(@ApiParam(value = "RTU组合id") @RequestParam(value = "sub") String sub){
        if(StringUtils.isEmpty(sub)){
            return error("rtuId为空");
        }
        Map<String, List<DeviceItem>> sub2DeviceItems = deviceItemRepository.getDeviceItemInfosBySubIds(Arrays.asList(sub));
        if(StringUtils.isEmpty(sub2DeviceItems)){
            return ResponseResult.buildOkResult(new HashMap<>());
        }
        List<DeviceItem> allDeviceItems = new ArrayList<>();
        for(Map.Entry<String, List<DeviceItem>> entry : sub2DeviceItems.entrySet()) {
            allDeviceItems.addAll(entry.getValue());
        }
        return  ResponseResult.buildOkResult(deviceItemOpertionService.getType2ItemsData(allDeviceItems));
    }

    @ApiOperation(value = "修改遥测传输参数", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rtuIds", value = "RTU组合id列表", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updateYcTranInfos",method = RequestMethod.POST)
    public ResponseResult updateYcTranInfos(@RequestBody List<YcTranInfo> ycTranInfos, String rtuIds) {
        if(StringUtils.isEmpty(ycTranInfos)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(rtuIds)){
            return error("rtuId列表为空");
        }

        boolean updateRes = deviceItemOpertionService.updateYcTranInfos(ycTranInfos,rtuIds);
        if(!updateRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "修改遥控传输参数", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rtuIds", value = "RTU组合id列表", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updateYkTranInfos",method = RequestMethod.POST)
    public ResponseResult updateYkTranInfos(@RequestBody List<YkTranInfo> ykTranInfos, String rtuIds) {
        if(StringUtils.isEmpty(ykTranInfos)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(rtuIds)){
            return error("rtuId列表为空");
        }

        boolean updateRes = deviceItemOpertionService.updateYkTranInfos(ykTranInfos,rtuIds);
        if(!updateRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "修改遥信传输参数", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rtuIds", value = "RTU组合id列表", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updateYxTranInfos",method = RequestMethod.POST)
    public ResponseResult updateYxTranInfos(@RequestBody List<YxTranInfo> yxTranInfos, String rtuIds) {
        if(StringUtils.isEmpty(yxTranInfos)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(rtuIds)){
            return error("rtuId列表为空");
        }

        boolean updateRes = deviceItemOpertionService.updateYxTranInfos(yxTranInfos,rtuIds);
        if(!updateRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "修改遥测存储参数", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCodes", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updateYcStoreInfos",method = RequestMethod.POST)
    public ResponseResult updateYcStoreInfos(@RequestBody List<YcStoreInfo> ycStoreInfos, String deviceCodes) {
        if(StringUtils.isEmpty(ycStoreInfos)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(deviceCodes)){
            return error("rtuIds为空");
        }

        Boolean updateRes =deviceItemOpertionService.updateYcStoreInfos(ycStoreInfos,deviceCodes);
        if(!updateRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "导出", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "code", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/exportItem",method = RequestMethod.GET)
    public ResponseResult exportItem(String code, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;  filename="
                + URLEncoder.encode(""+ Calendar.getInstance().getTimeInMillis(),
                "utf-8") + ".xls");

        ServletOutputStream out = response.getOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<String, List<DeviceItem>> code2Items = deviceItemRepository.getDeviceItemInfosByCodes(Arrays.asList(code));
        if(StringUtils.isNull(code2Items)) {
            return ResponseResult.buildFailResult();
        }

        //通过code获取List<DeviceItem>
        List<DeviceItem> items = code2Items.get(code);
        Map<String, List<List<String>>> type2items =  excelService.getExportItem(items);

        ExcelUtil.CellEntity id = new ExcelUtil.CellEntity();
        id.setName(ExcelConstant.EXPORT_ENTITY_ID);
        ExcelUtil.CellEntity name = new ExcelUtil.CellEntity();
        name.setName(ExcelConstant.EXPORT_ENTITY_NAME);

        ExcelUtil.CellEntity abbreviation = new ExcelUtil.CellEntity();
        abbreviation.setName(ExcelConstant.EXPORT_ENTITY_ABBREVIATION);
        ExcelUtil.CellEntity dataType = new ExcelUtil.CellEntity();
        dataType.setName(ExcelConstant.EXPORT_ENTITY_DATATYPE);

        ExcelUtil.CellEntity modulus = new ExcelUtil.CellEntity();
        modulus.setName(ExcelConstant.EXPORT_ENTITY_MODULUS);
        ExcelUtil.CellEntity basic = new ExcelUtil.CellEntity();
        basic.setName(ExcelConstant.EXPORT_ENTITY_BASIC);

        ExcelUtil.CellEntity unit = new ExcelUtil.CellEntity();
        unit.setName(ExcelConstant.EXPORT_ENTITY_UNIT);
        ExcelUtil.CellEntity alarm = new ExcelUtil.CellEntity();
        alarm.setName(ExcelConstant.EXPORT_ENTITY_ALARM);

        ExcelUtil.CellEntity store = new ExcelUtil.CellEntity();
        store.setName(ExcelConstant.EXPORT_ENTITY_STORE);
        ExcelUtil.CellEntity itemType = new ExcelUtil.CellEntity();
        itemType.setName(ExcelConstant.EXPORT_ENTITY_ITEMTYPE);

        ExcelUtil.CellEntity desc0 = new ExcelUtil.CellEntity();
        desc0.setName(ExcelConstant.EXPORT_ENTITY_DESC0);
        ExcelUtil.CellEntity desc1 = new ExcelUtil.CellEntity();
        desc1.setName(ExcelConstant.EXPORT_ENTITY_DESC1);

        ExcelUtil.CellEntity maxData = new ExcelUtil.CellEntity();
        maxData.setName(ExcelConstant.EXPORT_ENTITY_MAXDATA);
        ExcelUtil.CellEntity minData = new ExcelUtil.CellEntity();
        minData.setName(ExcelConstant.EXPORT_ENTITY_MINDATA);

        ExcelUtil.CellEntity rule = new ExcelUtil.CellEntity();
        rule.setName(ExcelConstant.EXPORT_ENTITY_RULE);
        ExcelUtil.CellEntity displayName = new ExcelUtil.CellEntity();
        displayName.setName(ExcelConstant.EXPORT_ENTITY_DISPLAYNAME);

        List<ExcelUtil.CellEntity> yaoceEntity = new ArrayList<ExcelUtil.CellEntity>();
        yaoceEntity.add(id);
        yaoceEntity.add(name);
        yaoceEntity.add(abbreviation);
        yaoceEntity.add(displayName);
        yaoceEntity.add(basic);
        yaoceEntity.add(modulus);
        yaoceEntity.add(alarm);
        yaoceEntity.add(store);
        yaoceEntity.add(itemType);

        List<List<ExcelUtil.CellEntity>> yaoceList = new ArrayList<List<ExcelUtil.CellEntity>>();
        yaoceList.add(yaoceEntity);
        //创建遥测excel
        ExcelUtil.printXls(workbook,out, type2items.get("ycs"), yaoceList, code + "遥测", 0,COLUMNWIDTH);

        List<ExcelUtil.CellEntity> yaoxinEntity = new ArrayList<ExcelUtil.CellEntity>();
        yaoxinEntity.add(id);
        yaoxinEntity.add(name);
        yaoxinEntity.add(abbreviation);
        yaoxinEntity.add(displayName);
        yaoxinEntity.add(desc0);
        yaoxinEntity.add(desc1);
        yaoxinEntity.add(alarm);
        yaoxinEntity.add(itemType);

        List<List<ExcelUtil.CellEntity>> yaoxinList = new ArrayList<List<ExcelUtil.CellEntity>>();
        yaoxinList.add(yaoxinEntity);
        //创建遥信excel
        ExcelUtil.printXls(workbook,out, type2items.get("yxs"), yaoxinList, code + "遥信", 1,COLUMNWIDTH);

        List<ExcelUtil.CellEntity> yaotiaoEntity = new ArrayList<ExcelUtil.CellEntity>();
        yaotiaoEntity.add(id);
        yaotiaoEntity.add(name);
        yaotiaoEntity.add(abbreviation);
        yaotiaoEntity.add(dataType);

        yaotiaoEntity.add(modulus);
        yaotiaoEntity.add(basic);
        yaotiaoEntity.add(unit);
        yaotiaoEntity.add(maxData);
        yaotiaoEntity.add(minData);

        List<List<ExcelUtil.CellEntity>> yaotiaoList = new ArrayList<List<ExcelUtil.CellEntity>>();
        yaotiaoList.add(yaotiaoEntity);
        //创建遥调excel
        ExcelUtil.printXls(workbook, out, type2items.get("yts"), yaotiaoList, code + "遥调", 2,COLUMNWIDTH);

        List<ExcelUtil.CellEntity> yaokongEntity = new ArrayList<ExcelUtil.CellEntity>();
        yaokongEntity.add(id);
        yaokongEntity.add(name);
        yaokongEntity.add(abbreviation);
        yaokongEntity.add(displayName);
        yaokongEntity.add(rule);

        List<List<ExcelUtil.CellEntity>> yaokongList = new ArrayList<List<ExcelUtil.CellEntity>>();
        yaokongList.add(yaokongEntity);
        //创建遥控excel
        ExcelUtil.printXls(workbook,out,type2items.get("yks"), yaokongList, code + "遥控", 3,COLUMNWIDTH);

        if (out != null) {
            workbook.write(out);
        }
        out.flush();
        out.close();

        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "导入", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "code", value = "网关id", required = true,paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/importItem", method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    public ResponseResult importItem(@RequestParam(value = "excelFile") MultipartFile excelFile,
                                     String code) throws IOException {
        Map<String, List<DeviceSub>>  code2Subs = deviceSubCache.getSubsByCodes(Arrays.asList(code));
        if(StringUtils.isEmpty(code2Subs) || StringUtils.isEmpty(code2Subs.get(code))) {
            return ResponseResult.buildFailResult();
        }

        List<String> deviceSubIds = new ArrayList<>();
        for(DeviceSub sub : code2Subs.get(code)) {
            deviceSubIds.add(sub.getRtuidcb());
        }

        List<List<List<String>>> excelData = ParseXlsUtils.readExcle(excelFile.getInputStream());
        Map<String, List<DeviceItem>> type2Items = excelService.getImportItem(excelData);
        List<DeviceItem> ycItems = type2Items.get("YC");

        //更新遥测数据
        if(StringUtils.isNotEmpty(ycItems)) {
            List<YcTran> ycTrans = new ArrayList<>();
            YcTran ycTran;
            for(DeviceItem item : ycItems) {
                ycTran = new YcTran();
                ycTran.setBasic(item.getBasic());
                ycTran.setBtype(item.getBtype());
                ycTran.setCtratio(item.getCtratio());
                ycTran.setId(item.getId());
                ycTran.setPalarm(item.getPalarm());
                ycTran.setShortname(item.getShortname());
                ycTran.setStore(item.getStore());
                ycTran.setDisplayname(item.getDisplayname());
                ycTrans.add(ycTran);
            }
            deviceItemRepository.setYcTranInfos(ycTrans,StringUtil.listTurnString(deviceSubIds));
        }

        //更新遥信数据
        List<DeviceItem> yxItems = type2Items.get("YX");
        if(StringUtils.isNotEmpty(yxItems)) {
            List<YxTran> yxTrans = new ArrayList<>();
            YxTran yxTran;
            for(DeviceItem item : yxItems) {
                yxTran = new YxTran();
                yxTran.setBtype(item.getBtype());
                yxTran.setId(item.getId());
                yxTran.setPalarm(item.getPalarm());
                yxTran.setShortname(item.getShortname());
                yxTran.setDisplayname(item.getDisplayname());
                yxTran.setDesc0(item.getDesc0());
                yxTran.setDesc1(item.getDesc1());
                yxTrans.add(yxTran);
            }
            deviceItemRepository.setYxTranInfos(yxTrans, StringUtil.listTurnString(deviceSubIds));
        }

        //更新遥控数据
        List<DeviceItem> ykItems = type2Items.get("YK");
        if(StringUtils.isNotEmpty(ykItems)) {
            List<YkTran> ykTrans = new ArrayList<>();
            YkTran ykTran;
            for(DeviceItem item : ykItems) {
                ykTran = new YkTran();
                ykTran.setId(item.getId());
                ykTran.setShortname(item.getShortname());
                ykTran.setDesc(item.getDesc());
                ykTran.setDisplayname(item.getDisplayname());
                ykTrans.add(ykTran);
            }
            deviceItemRepository.setYkTranInfos(ykTrans,StringUtil.listTurnString(deviceSubIds));
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "按名称分类导出", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "code", value = "网关id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceName", value = "网关名称", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/exportItemByName", method = RequestMethod.GET)
    public ResponseResult exportItemByName(String code,String deviceName, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;  filename="
                + URLEncoder.encode(""+ Calendar.getInstance().getTimeInMillis(),
                "utf-8") + ".xls");

        ServletOutputStream out = response.getOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        //通过code获取List<DeviceItem>
        Map<String, List<DeviceItem>> code2Items = deviceItemRepository.getDeviceItemInfosByCodes(Arrays.asList(code));
        if(StringUtils.isEmpty(code2Items)) {
            return ResponseResult.buildFailResult();
        }

        List<DeviceItem> items = code2Items.get(code);
        Map<String, Map<String, String>> name2Type = new HashMap<>();
        for(DeviceItem item : items) {
            String name = item.getShortname();
            Integer itemType = item.getBtype();
            String subName = item.getId();

            Map<String, String> type2Subname = new HashMap<>();
            if(!name2Type.containsKey(name)) {
                type2Subname.put(ExcelConstant.itemType2Value.get(itemType), subName);

            }else{
                type2Subname = name2Type.get(name);
                String beforeSubName = type2Subname.get(ExcelConstant.itemType2Value.get(itemType));
                String afterSubName = addSubName(beforeSubName,subName);
                type2Subname.put(ExcelConstant.itemType2Value.get(itemType), afterSubName);
            }
            name2Type.put(name, type2Subname);
        }

        List<ExcelUtil.CellEntity> entitys = new ArrayList<>();
        ExcelUtil.CellEntity name = new ExcelUtil.CellEntity();
        name.setName(ExcelConstant.EXPORT_ENTITY_NAME);
        entitys.add(name);

        ExcelUtil.CellEntity watermeter = new ExcelUtil.CellEntity();
        watermeter.setName(ExcelConstant.EXPORT_ENTITY_WATERMETER);
        entitys.add(watermeter);

        ExcelUtil.CellEntity electricmeter = new ExcelUtil.CellEntity();
        electricmeter.setName(ExcelConstant.EXPORT_ENTITY_ELECTRICMETER);
        entitys.add(electricmeter);

        ExcelUtil.CellEntity voltage = new ExcelUtil.CellEntity();
        voltage.setName(ExcelConstant.EXPORT_ENTITY_VOLTAGE);
        entitys.add(voltage);

        ExcelUtil.CellEntity electriccurrent = new ExcelUtil.CellEntity();
        electriccurrent.setName(ExcelConstant.EXPORT_ENTITY_ELECTRICCURRENT);
        entitys.add(electriccurrent);

        ExcelUtil.CellEntity smokealarm = new ExcelUtil.CellEntity();
        smokealarm.setName(ExcelConstant.EXPORT_ENTITY_SMOKEALARM);
        entitys.add(smokealarm);

        ExcelUtil.CellEntity accessalarm = new ExcelUtil.CellEntity();
        accessalarm.setName(ExcelConstant.EXPORT_ENTITY_ACCESSALARM);
        entitys.add(accessalarm);

        ExcelUtil.CellEntity gasalarm = new ExcelUtil.CellEntity();
        gasalarm.setName(ExcelConstant.EXPORT_ENTITY_GASALARM);
        entitys.add(gasalarm);

        ExcelUtil.printSortXls(workbook, out, entitys, name2Type, "站点信息",
                0, code, deviceName);

        if (out != null) {
            workbook.write(out);
        }
        out.flush();
        out.close();
        return ResponseResult.buildOkResult();
    }

    private String addSubName(String subName,String addName) {
        return subName + "," + addName;
    }

    @ApiOperation(value = "添加运算库数据项", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCodes", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/addCalcItem",method = RequestMethod.POST)
    public ResponseResult addCalcItem(@RequestBody DeviceItemInfo deviceItemInfo, String deviceCodes) {
        if(StringUtils.isNull(deviceItemInfo)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(deviceCodes)){
            return error("网关id为空");
        }

        DeviceItem deviceItem = new DeviceItem();
        BeanUtils.copyProperties(deviceItemInfo,deviceItem);
        Boolean addRes = deviceItemRepository.addCalcItem(deviceItem,deviceCodes);
        if(!addRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "修改运算库遥测数据项", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCodes", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updateYcCalcItem",method = RequestMethod.POST)
    public ResponseResult updateYcCalcItem(@RequestBody List<YcCalcInfo> ycCalcInfos, String deviceCodes) {
        if(StringUtils.isEmpty(ycCalcInfos)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(deviceCodes)){
            return error("网关id为空");
        }

        boolean updateRes = deviceItemOpertionService.updateYcCalcItem(ycCalcInfos,deviceCodes);
        if(!updateRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "修改运算库遥信数据项", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCodes", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/updateYxCalcItem",method = RequestMethod.POST)
    public ResponseResult updateYxCalcItem(@RequestBody List<YxCalcInfo> yxCalcInfos, String deviceCodes) {
        if(StringUtils.isEmpty(yxCalcInfos)){
            return  ResponseResult.buildOkResult();
        }
        if(StringUtils.isEmpty(deviceCodes)){
            return error("网关id为空");
        }

        boolean updateRes = deviceItemOpertionService.updateYxCalcItem(yxCalcInfos,deviceCodes);
        if(!updateRes){
            return  ResponseResult.buildFailResult();
        }
        return  ResponseResult.buildOkResult();
    }

    @ApiOperation(value = "通过网关id获取运算库数据项", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCodes", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/getCalcItemsByCodes",method = RequestMethod.GET)
    public ResponseResult<List<DeviceItemInfo>> getCalcItemsByCodes(String deviceCodes) throws ParseException {
        if(StringUtils.isEmpty(deviceCodes)){
            return error("网关id为空");
        }
        return  ResponseResult.buildOkResult(deviceItemOpertionService.getCalcItemsByCodes(deviceCodes));
    }

    @ApiOperation(value = "获取存储库数据项", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCode", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/getStoreItemsByCode", method = RequestMethod.GET)
    public ResponseResult<Map<String,Object>> getStoreItemsByCode(String deviceCode){
        if(StringUtils.isEmpty(deviceCode)){
            return error("网关id为空");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("trans",deviceItemOpertionService.getStoreItemsByCodeAndStype(deviceCode, ItemStype.TRANSPORTYC.id));
        data.put("calc", deviceItemOpertionService.getStoreItemsByCodeAndStype(deviceCode,ItemStype.OPERATIONYC.id));
        return  ResponseResult.buildOkResult(data);
    }

    @ApiOperation(value = "获取告警库数据项", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "deviceCode", value = "网关id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getAlarmItemsByCodeAndType", method = RequestMethod.GET)
    public ResponseResult<List<DeviceItemInfo>> getAlarmItemsByCodeAndType(String deviceCode,Integer type) {
        if(StringUtils.isEmpty(deviceCode)){
            return error("网关id为空");
        }
        if(StringUtils.isEmpty(type)){
            return error("类型为空");
        }
        return ResponseResult.buildOkResult(deviceItemOpertionService.getAlarmItemsByCodeAndType(deviceCode,type));
    }

    @ApiOperation(value = "脚本验证", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "content", value = "js脚本", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceCode", value = "网关id", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/jsCheck", method = RequestMethod.POST)
    public ResponseResult<Float> jsCheck(String content,String deviceCode) throws Exception {
        if(StringUtils.isEmpty(content)){
            return error("脚本为空");
        }
        if(StringUtils.isEmpty(deviceCode)){
            return error("网关id为空");
        }

        Map<String, List<DeviceItemRealTimeData>> code2Dates = deviceItemRepository.getDeviceItemRealTimeDataByCodes(Arrays.asList(deviceCode));
        if(StringUtils.isEmpty(code2Dates)){
            return ResponseResult.buildFailResult();
        }

        Map<String, Float> id2Data = new HashMap<>();
        for(Map.Entry<String, List<DeviceItemRealTimeData>> entry : code2Dates.entrySet()) {
            for(DeviceItemRealTimeData realTimeData : entry.getValue()) {
                id2Data.put(realTimeData.getId(), realTimeData.getVal());
            }
        }
        return ResponseResult.buildOkResult(ComputeUtil.compute(id2Data, content));
    }

    @ApiOperation(value = "获取数据项属性列表", httpMethod = "GET")
    @GetMapping(value = "/getItemBtypeList")
    public ResponseResult getItemBtypeList(){
        List<ItemBtypeResp> ItemBtypeList = new ArrayList<>();
        ItemBtypeResp itemBtypeResp;
        ItemBtype[] itemBtypes = ItemBtype.values();
        for (ItemBtype itemBtype : itemBtypes) {
            itemBtypeResp = new ItemBtypeResp();
            itemBtypeResp.setName(itemBtype.value);
            itemBtypeResp.setValue(itemBtype.id);
            ItemBtypeList.add(itemBtypeResp);
        }
        return ResponseResult.buildOkResult(ItemBtypeList);
    }



}
