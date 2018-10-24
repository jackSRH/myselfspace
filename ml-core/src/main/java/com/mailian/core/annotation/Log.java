package com.mailian.core.annotation;

import com.mailian.core.enums.ChannelType;

import java.lang.annotation.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /** 模块 */
    String title() default "";

    /** 功能 */
    String action() default "";

    /** 渠道 */
    ChannelType channel() default ChannelType.WEB;

    /** 是否保存请求的参数 */
    boolean isSaveRequestData() default true;

}
