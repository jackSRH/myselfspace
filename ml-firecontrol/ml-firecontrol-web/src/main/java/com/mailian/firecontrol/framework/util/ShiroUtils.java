package com.mailian.firecontrol.framework.util;

import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.framework.shiro.MlShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/9
 * @Description:
 */
public class ShiroUtils {
    /**
     * 清理shiro缓存
     */
    public static void clearCachedAuthorizationInfo()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MlShiroRealm realm = (MlShiroRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static String getIp()
    {
        return SecurityUtils.getSubject().getSession().getHost();
    }

    public static String getSessionId()
    {
        return String.valueOf(SecurityUtils.getSubject().getSession().getId());
    }

    public static Subject getSubjct()
    {
        return SecurityUtils.getSubject();
    }

    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    public static ShiroUser getCurUser(){
        Subject subject = getSubjct();
        if(StringUtils.isNotNull(subject)){
            return (ShiroUser) subject.getPrincipal();
        }
        return null;
    }

    public static ShiroUser getUser() {
        return (ShiroUser) getSubjct().getPrincipal();
    }
}
