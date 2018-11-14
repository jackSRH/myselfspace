package com.mailian.firecontrol.service;


import com.mailian.firecontrol.dto.push.DeviceItem;

import java.util.List;
import java.util.Map;


public interface ExcelService {
	/**
	 * 获取导出数据项
	 * @param items
	 * @return
	 */
	Map<String, List<List<String>>> getExportItem(List<DeviceItem> items);
	/**
	 * 获取导入数据项
	 * @param excelData
	 * @return
	 */
	Map<String,List<DeviceItem>>  getImportItem(List<List<List<String>>> excelData);
}
