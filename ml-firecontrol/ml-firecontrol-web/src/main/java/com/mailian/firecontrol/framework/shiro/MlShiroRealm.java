package com.mailian.firecontrol.framework.shiro;

import com.mailian.core.enums.Status;
import com.mailian.core.shiro.JwtToken;
import com.mailian.core.util.JwtUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.AreaRank;
import com.mailian.firecontrol.dao.auto.model.Role;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.dto.ShiroUser;
import com.mailian.firecontrol.service.MenuService;
import com.mailian.firecontrol.service.RoleService;
import com.mailian.firecontrol.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * shiro 认证
 *
 * @author wangqiaoqing
 */
public class MlShiroRealm extends AuthorizingRealm {

    private final static Logger logger = LoggerFactory.getLogger(MlShiroRealm.class);

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private MenuService menuService;

    @Autowired
    @Lazy
    private RoleService roleService;

    @Autowired
    @Lazy
    private JwtUtils jwtUtils;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        // 此realm只支持jwtToken
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("MlShiroRealm.doGetAuthorizationInfo()");
        ShiroUser user = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(null != user) {
            info.setRoles(user.getRoles());
            info.addStringPermissions(menuService.selectPermsByUserId(user.getId()));
        }
        return info;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        logger.info("MlShiroRealm.doGetAuthenticationInfo()");

        //获取用户的输入的账号.
        Claims claims = jwtUtils.getClaimByToken((String)token.getPrincipal());
        String userName = jwtUtils.getUserName(claims);

        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        List<User> userList = userService.getUsersByName(userName);
        if(null == userList || userList.isEmpty()){
            throw new AuthenticationException("帐号密码错误");
        }

        User user = null;
        for (User userTemp : userList) {
            if(Status.NORMAL.id.intValue() == userTemp.getStatus().intValue()){
                user = userTemp;
            }
        }

        if(user == null){
            throw new AuthenticationException("用户已停用");
        }

        ShiroUser shiroUser = new ShiroUser();
        BeanUtils.copyProperties(user,shiroUser);

        List<Role> roleList = roleService.selectRolesByUserId(user.getId());
        Set<String> permsSet = new HashSet<>();
        Integer minRank = AreaRank.OTHER.id;
        for (Role role : roleList) {
            if (StringUtils.isNotEmpty(role.getRoleKey())) {
                permsSet.addAll(Arrays.asList(role.getRoleKey().trim().split(",")));
            }
            if(StringUtils.isNotNull(role.getShowRank()) && role.getShowRank() < minRank){
                minRank = role.getShowRank();
            }
        }
        shiroUser.setRoles(permsSet);
        shiroUser.setRank(minRank);
        shiroUser.setPrecinctIds(userService.getPrecinctIds(user.getId()));

        //设置盐值(保证用户密码一样的情况的下加密后的内容不一致)
        //ByteSource salt=ByteSource.Util.bytes(userName);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                shiroUser, //用户名
                user.getPassword(), //密码
                //salt,
                getName()  //realm name
        );
        return authenticationInfo;
    }


    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo()
    {
        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        this.clearCachedAuthorizationInfo(principalCollection);
        this.clearCachedAuthenticationInfo(principalCollection);
    }
}
