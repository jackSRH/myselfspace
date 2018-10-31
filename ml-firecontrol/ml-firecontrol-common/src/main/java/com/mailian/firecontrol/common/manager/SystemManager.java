package com.mailian.firecontrol.common.manager;

import com.mailian.core.constants.CoreCommonConstant;

import java.util.Collection;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/30
 * @Description:系统管理类
 */
public class SystemManager {

    /**
     * 判断是否超级管理员角色
     * @param roleKey
     * @return
     */
    public static boolean isAdminRole(String roleKey){ return CoreCommonConstant.MANAGER_ROLE_KEY.equals(roleKey);}

    /**
     * 判断是否超级管理员角色
     * @param roleKeys
     * @return
     */
    public static boolean isAdminRole(Collection<String> roleKeys){ return roleKeys.contains(CoreCommonConstant.MANAGER_ROLE_KEY);}
}
