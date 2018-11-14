package com.mailian.firecontrol.service;


import com.mailian.firecontrol.dto.SelectDto;

import java.util.List;

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

}
