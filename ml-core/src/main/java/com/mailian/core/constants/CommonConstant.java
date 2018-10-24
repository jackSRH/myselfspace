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


    /**  token相关  */
    /*token 标识*/
    public static final String ML_TOKEN = "ml_token";
    /*刷新Token时间*/
    public static final long REFRESH_TOKEN_TIME = 1800;
    /*redis token 键*/
    public static final String REDIS_TOKEN_KEY = "tokenSession:";
    /*刷新Token标识*/
    public static final String REFRESH_TOKEN = "refreshToken";
}
