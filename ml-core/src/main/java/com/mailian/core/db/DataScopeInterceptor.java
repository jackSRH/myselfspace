package com.mailian.core.db;

import cn.hutool.core.collection.CollectionUtil;
import com.mailian.core.handler.AsyncExceptionHandler;
import com.mailian.core.util.PluginUtils;
import com.mailian.core.util.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description: 数据权限拦截器
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class DataScopeInterceptor implements Interceptor {
    public DataScopeInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement)metaStatementHandler.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        } else {
            BoundSql boundSql = (BoundSql)metaStatementHandler.getValue("delegate.boundSql");
            String originalSql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            DataScope dataScope = this.findDataScopeObject(parameterObject);
            if (dataScope == null) {
                return invocation.proceed();
            } else {
                String scopeName = dataScope.getScopeName();
                List<Integer> dataIds = dataScope.getDataIds();
                String join = CollectionUtil.join(dataIds, ",");
                String alias = dataScope.getAlias();
                if(dataScope.isScopeResult()){
                    scopeName = StringUtils.nvl(alias,"temp_data_scope")+"."+dataScope.getScopeName();
                    originalSql = "select * from (" + originalSql + ") temp_data_scope where " + scopeName + " in (" + join + ")";
                }else{
                    scopeName = StringUtils.isEmpty(alias) ? scopeName : alias+"."+scopeName;
                    if(originalSql.contains(" where ")) {
                        originalSql = originalSql + " and " + scopeName + " in (" + join + ")";
                    }else{
                        originalSql = originalSql + " where " + scopeName + " in (" + join + ")";
                    }
                }

                metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
                return invocation.proceed();
            }
        }
    }

    public DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope)parameterObj;
        } else {
            if (parameterObj instanceof Map) {
                Iterator var2 = ((Map)parameterObj).values().iterator();

                while(var2.hasNext()) {
                    Object val = var2.next();
                    if (val instanceof DataScope) {
                        return (DataScope)val;
                    }
                }
            }

            return null;
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}
