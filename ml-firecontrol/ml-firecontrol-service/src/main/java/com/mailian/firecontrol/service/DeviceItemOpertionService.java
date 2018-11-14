package com.mailian.firecontrol.service;


import com.mailian.firecontrol.dto.SelectDto;
import com.mailian.firecontrol.dto.push.DeviceItem;
import com.mailian.firecontrol.dto.web.request.DeviceItemInfo;
import com.mailian.firecontrol.dto.web.request.YcCalcInfo;
import com.mailian.firecontrol.dto.web.request.YcStoreInfo;
import com.mailian.firecontrol.dto.web.request.YcTranInfo;
import com.mailian.firecontrol.dto.web.request.YkTranInfo;
import com.mailian.firecontrol.dto.web.request.YxCalcInfo;
import com.mailian.firecontrol.dto.web.request.YxTranInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface DeviceItemOpertionService {
	/**
	 * 通过遥控数据项id和开关状态数据项id获取开关当前状态
	 * @param yaokongItemId,statusItemId
	 * @return
	 */
	String getYaokongStatus(String yaokongItemId, String statusItemId);

	/**
	 * 通过遥控数据项id获取遥控控制状态以及对应枚举值
	 * @param yaokongItemId,statusItemId
	 * @return
	 */
	List<SelectDto> getYaokongEnumList(String yaokongItemId);

	/**
	 * 通过单位di获取配置数据项
	 * @param unitId
	 * @return
	 */
	Map<String, Object> getConfigItemsByUnitId(Integer unitId) throws ExecutionException, InterruptedException;

	/**
	 * 通过网关id列表获取所有数据项
	 * @param deviceCodes
	 * @return
	 */
	List<DeviceItem> getAllItemsByDeviceCodes(List<String> deviceCodes);

	/**
	 * 数据项按四遥类型分类
	 * @param items
	 * @return
	 */
	Map<String,  List<DeviceItem>> getType2ItemsData(List<DeviceItem> items);

	/**
	 * 修改遥测传输参数
	 * @param ycTranInfos,rtuIds
	 * @return
	 */
	boolean updateYcTranInfos(List<YcTranInfo> ycTranInfos, String rtuIds);

	/**
	 * 修改遥控传输参数
	 * @param ykTranInfos,rtuIds
	 * @return
	 */
	boolean updateYkTranInfos(List<YkTranInfo> ykTranInfos, String rtuIds);

	/**
	 * 修改遥信传输参数
	 * @param yxTranInfos,rtuIds
	 * @return
	 */
	boolean updateYxTranInfos(List<YxTranInfo> yxTranInfos, String rtuIds);

	/**
	 * 修改遥测存储参数
	 * @param ycStoreInfos,rtuIds
	 * @return
	 */
	boolean updateYcStoreInfos(List<YcStoreInfo> ycStoreInfos, String deviceCodes);

	/**
	 * 修改计算遥测参数
	 * @param ycCalcInfos,rtuIds
	 * @return
	 */
	boolean updateYcCalcItem(List<YcCalcInfo> ycCalcInfos, String deviceCodes);

	/**
	 * 修改计算遥信参数
	 * @param yxCalcInfos,rtuIds
	 * @return
	 */
	boolean updateYxCalcItem(List<YxCalcInfo> yxCalcInfos, String deviceCodes);

	/**
	 * 获取运算库数据项
	 * @param deviceCodes
	 * @return
	 */
	List<DeviceItemInfo> getCalcItemsByCodes(String deviceCodes);

	/**
	 * 通过网关id和数据项类型获取存储数据项
	 * @param deviceCode，stype
	 * @return
	 */
	List<DeviceItemInfo> getStoreItemsByCodeAndStype(String deviceCode, Integer stype);

	/**
	 * 通过网关id和数据项类型获取告警数据项
	 * @param deviceCode，stype
	 * @return
	 */
	List<DeviceItemInfo> getAlarmItemsByCodeAndType(String deviceCode, Integer stype);

}
