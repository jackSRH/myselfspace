package com.mailian.core.db;

import com.mailian.core.util.PluginUtils;
import org.apache.ibatis.exceptions.PersistenceException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description: 空条件 sql 拦截
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class EmptyWhereInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(DataScopeInterceptor.class);

    private static final List<SqlCommandType> INTERCEPTOR_COMMAND = Arrays.asList(SqlCommandType.UPDATE,SqlCommandType.DELETE);

    public EmptyWhereInterceptor() {
    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement)metaStatementHandler.getValue("delegate.mappedStatement");

        if(INTERCEPTOR_COMMAND.contains(mappedStatement.getSqlCommandType())){
            BoundSql boundSql = (BoundSql)metaStatementHandler.getValue("delegate.boundSql");
            String originalSql = boundSql.getSql();
            if(!originalSql.contains(" where ")){
                log.error("");
                throw new PersistenceException("不允许删除或修改全表数据!");
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
