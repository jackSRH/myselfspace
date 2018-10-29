package com.mailian.core.constants;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/22
 * @Description:
 */
public abstract class CommonConstant {
    /** 最大批处理数量*/
    public static final int BATCH_MAX_SIZE = 1000;
    /**上传图片格式定义*/
    public final static String IMAGE_TYPE = ".jpg,.gif,.png,.bmp,.jpeg";
    /** 序列号 模板名 */
    public final static String SERIAL_NUMBER_TEMPLATE = "serialNumberTemplate";

    /** 超级管理员角色键值 */
    public static final String MANAGER_ROLE_KEY = "admin";

    /**  token相关  */
    /*token 标识*/
    public static final String ML_TOKEN = "ml_token";
    /*刷新Token时间*/
    public static final long REFRESH_TOKEN_TIME = 1800;
    /*redis token 键*/
    public static final String REDIS_TOKEN_KEY = "tokenSession:";
    /*刷新Token标识*/
    public static final String REFRESH_TOKEN = "refreshToken";


    /** redis 键相关 */
    public static final String PREPARE_SERIAL_NUMBER_KEY = "preSerialNumbers:";

    public static final String SYS_CONFIG_KEY = "mlfirecontrol:sysconfigs";
    public static final String SYS_MENU_KEY = "mlfirecontrol:sysmenus";
    public static final String SYS_ROLE_KEY = "mlfirecontrol:sysroles";

}
