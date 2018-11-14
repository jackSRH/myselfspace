package com.mailian.firecontrol.common.constants;

import java.util.HashMap;
import java.util.Map;

public class ExcelConstant {
    /** ID */
	public static final String EXPORT_ENTITY_ID = "数据项ID（不可更改）"; 
    /** 变量名 */
	public static final String EXPORT_ENTITY_NAME = "名称（不可更改）"; 
    /** 展示名称 */
	public static final String EXPORT_ENTITY_DISPLAYNAME = "英文简称（Ia，Ua）"; 
    /** 简称 */
	public static final String EXPORT_ENTITY_ABBREVIATION = "简称";
    /** 数据类型 */
	public static final String EXPORT_ENTITY_DATATYPE = "数据类型（1：有符号整形\n2：无符号整形\n-1：浮点型）";
    /** CT变比 */
	public static final String EXPORT_ENTITY_MODULUS = "CT变比";
    /** 基值 */
	public static final String EXPORT_ENTITY_BASIC = "基值";
    /** 单位 */
	public static final String EXPORT_ENTITY_UNIT = "单位";
    /** 告警 */
	public static final String EXPORT_ENTITY_ALARM = "告警（0：无告警\n1：有告警）";
    /** 存储 */
	public static final String EXPORT_ENTITY_STORE = "存储（-1：不存储 \n3600：存储）";
    /** 属性 */
	public static final String EXPORT_ENTITY_ITEMTYPE = "属性（0：无  1：电量 \n2：水量  3：气量  4：电压\n5：电流  6：门禁  7：燃气\n8：烟感  9：开关  10：通信 \n11：跳闸）";
    /** 0描述 */
	public static final String EXPORT_ENTITY_DESC0 = "0描述";
    /** 1描述 */
	public static final String EXPORT_ENTITY_DESC1 = "1描述";
    /** 最大值 */
	public static final String EXPORT_ENTITY_MAXDATA = "最大值";
    /** 最小值 */
	public static final String EXPORT_ENTITY_MINDATA = "最小值";
    /** 规则描述 */
	public static final String EXPORT_ENTITY_RULE = "规则描述";
    /** 水表变量名 */
	public static final String EXPORT_ENTITY_WATERMETER = "水表变量名"; 
    /** 电表变量名 */
	public static final String EXPORT_ENTITY_ELECTRICMETER = "电表变量名"; 
    /** 电压变量名 */
	public static final String EXPORT_ENTITY_VOLTAGE = "电压变量名"; 
    /** 电流变量名 */
	public static final String EXPORT_ENTITY_ELECTRICCURRENT = "电流变量名"; 
    /** 烟感告警变量名 */
	public static final String EXPORT_ENTITY_SMOKEALARM = "烟感告警变量名"; 
    /** 门禁告警变量名 */
	public static final String EXPORT_ENTITY_ACCESSALARM = "门禁告警变量名"; 
    /** 燃气告警变量名 */
	public static final String EXPORT_ENTITY_GASALARM = "燃气告警变量名";
	
    /** type2value */
	public static final Map<Integer, String> itemType2Value = new HashMap<Integer, String>(){
		private static final long serialVersionUID = 1L;
		{
			put(0,"无");
			put(1,"电表");
			put(2,"水表");
			put(3,"气量");
			put(4,"电压");
			put(5,"电流");
			put(6,"门禁");
			put(7,"燃气");
			put(8,"烟感");
			put(9,"开关");
			put(10,"通信");
			put(11,"跳闸");
		}
	};
	
}
