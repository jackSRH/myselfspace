package com.mailian.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/23
 * @Description:
 */
public class ReflectionKit {
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                return Object.class;
            }
        }
    }
}
