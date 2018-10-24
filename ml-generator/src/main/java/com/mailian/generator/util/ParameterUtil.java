package com.mailian.generator.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/22
 * @Description:
 */
public class ParameterUtil {
    public static String getParameterClause(String code,IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("#{");
        sb.append(code);
        sb.append(",jdbcType=");
        sb.append(introspectedColumn.getJdbcTypeName());
        if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            sb.append(",typeHandler=");
            sb.append(introspectedColumn.getTypeHandler());
        }

        sb.append('}');
        return sb.toString();
    }
}
