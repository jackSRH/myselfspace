package com.mailian.firecontrol.common.constants;

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

    public static final String DEVICE_CODE_TO_DEVICE="mlfirecontrol:codeToDevice";
    public static final String DEVICE_CODE_COMMUMICATION_STATUS="mlfirecontrol:deviceStatus";
    public static final String DEVICE_CODE_TO_DEVICE_SUB="mlfirecontrol:gwDeviceSubs";
    public static final String SUB_ID_TO_DEVICE_SUB="mlfirecontrol:subidDeviceSubs";
    public static final String DEVICE_SUB_ITEM="mlfirecontrol:subidItems";
    public static final String ID_ITEM="mlfirecontrol:idItems";
    public static final String DEVICE_ITEM_REAL_TIME_DATA="mlfirecontrol:itemRealDatas";
    /* 运算库 */
    public static final String DEVICE_CODE_CALC_ITEM="mlfirecontrol:codeCalcItems";

    /* 推送redis默认失效时间(永不失效) */
    public static final long PUSH_REDIS_DEFAULT_EXPIRE = -1;


    public static final Float ITEM_INITIAL_VALUE = -99.9f;


    /**图片存放路径 */
    public final static String UPLOAD_IMG_DIR = "images";
}
