package com.mailian.firecontrol.common.constants;

import com.mailian.firecontrol.common.enums.ItemBtype;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
public class CommonConstant {
    /** 序列号 模板名 */
    public final static String SERIAL_NUMBER_TEMPLATE = "serialNumberTemplate";

    /** redis 键相关 */
    public static final String PREPARE_SERIAL_NUMBER_KEY = "preSerialNumbers:";
    public static final String SYS_CONFIG_KEY = "mlfirecontrol:sysconfigs";
    public static final String SYS_MENU_KEY = "mlfirecontrol:sysmenus";
    public static final String SYS_ROLE_KEY = "mlfirecontrol:sysroles";
    public static final String SYS_AREA_KEY = "mlfirecontrol:sysareas";
    public static final String SYS_DICT_DATA_KEY = "mlfirecontrol:dicttypedatas";
    public static final String SYS_DEVICE_UNIT_KEY = "mlfirecontrol:deviceunits";
    public static final String SYS_UNIT_STATUS_KEY = "mlfirecontrol:unitstatus";

    public static final String DEVICE_CODE_TO_DEVICE="mlfirecontrol:codeToDevice";
    public static final String DEVICE_CODE_COMMUMICATION_STATUS="mlfirecontrol:deviceStatus";
    public static final String DEVICE_CODE_TO_DEVICE_SUB="mlfirecontrol:gwDeviceSubs";
    public static final String SUB_ID_TO_DEVICE_SUB="mlfirecontrol:subidDeviceSubs";
    public static final String DEVICE_SUB_ITEM="mlfirecontrol:subidItems";
    public static final String ID_ITEM="mlfirecontrol:idItems";
    public static final String DEVICE_ITEM_REAL_TIME_DATA="mlfirecontrol:itemRealDatas";
    /* 运算库 */
    public static final String DEVICE_CODE_CALC_ITEM="mlfirecontrol:codeCalcItems";
    /** 通知模块键值 */
    public static final String ALARM_NOTICE = "mlfirecontrol:alarmNotice";


    /* 推送redis默认失效时间(永不失效) */
    public static final long PUSH_REDIS_DEFAULT_EXPIRE = -1;


    public static final Float ITEM_INITIAL_VALUE = -99.9f;


    /**图片存放路径 */
    public final static String UPLOAD_IMG_DIR = "images";

    /* 默认用户密码 */
    public final static String DEFAULT_PASSWORD = "123456";

    /* 电话号码匹配 */
    public static Pattern phonePattern = Pattern
            .compile("^(13[0-9]|15[012356789]|18[012356789]|14[012356789]|17[012356789])[0-9]{8}$");

    public final static String ALARM_INFO = "alarmInfoTemplate";

    /** 用户唯一键(友盟推送) */
    public static final String USRE_KEY = "%s&&%s";

    /** 分隔符 */
    public static final String SPLIT_STR = ";";

    /** 单位数据项类型*/
    public static final List<Integer> UNIT_TREND_TYPES = Arrays.asList(
            ItemBtype.VOLTAGE.id,ItemBtype.ELECTRICCURRENT.id,
            ItemBtype.LEAKAGE.id,ItemBtype.CABLE_TEMPERATURE.id);
}
