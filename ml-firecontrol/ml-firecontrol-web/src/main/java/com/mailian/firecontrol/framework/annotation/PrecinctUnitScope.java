package com.mailian.firecontrol.framework.annotation;

import java.lang.annotation.*;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/12/6
 * @Description:
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrecinctUnitScope {
    /** 管辖区别名 */
    String palias() default "precinct_id";
    /** 单位别名 */
    String ualias() default "unit_id";
    /** 参数是否有涉及数据过滤 */
    boolean hasPrecinctOrUnit() default false;
    /** req参数名 */
    String reqParamName() default "";
    /** 管辖区参数名 */
    String precinctParamName() default "precinctId";
    /** 单位参数名*/
    String unitParamName() default "unitId";
    /** 参数是否json格式 */
    boolean isJson() default false;

}
