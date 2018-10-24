package com.mailian.core.db;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description: 数据权限控制
 */
public class DataScope {
    private String scopeName = "unitId";
    private List<Integer> dataIds;
    /* 别名 */
    private String alias;
    /* 针对查询结果控制 */
    private boolean scopeResult = false;

    public DataScope() {
    }

    public DataScope(List<Integer> dataIds) {
        this.dataIds = dataIds;
    }

    public DataScope(String scopeName, List<Integer> dataIds, String alias, boolean scopeResult) {
        this.scopeName = scopeName;
        this.dataIds = dataIds;
        this.alias = alias;
        this.scopeResult = scopeResult;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public List<Integer> getDataIds() {
        return dataIds;
    }

    public void setDataIds(List<Integer> dataIds) {
        this.dataIds = dataIds;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isScopeResult() {
        return scopeResult;
    }

    public void setScopeResult(boolean scopeResult) {
        this.scopeResult = scopeResult;
    }
}
