package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.enums.Status;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.FilterUtil;
import com.mailian.core.util.PageUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.mapper.RoleMapper;
import com.mailian.firecontrol.dao.auto.model.Role;
import com.mailian.firecontrol.dao.auto.model.RoleMenu;
import com.mailian.firecontrol.dao.manual.mapper.SystemManualMapper;
import com.mailian.firecontrol.dto.web.request.RoleReq;
import com.mailian.firecontrol.dto.web.response.RoleResp;
import com.mailian.firecontrol.service.RoleMenuService;
import com.mailian.firecontrol.service.RoleService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 角色service
 *
 * @author wangqiaoqing
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role,RoleMapper> implements RoleService {
    @Resource
    private SystemManualMapper systemManualMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Cacheable(value = CommonConstant.SYS_ROLE_KEY)
    @Override
    public List<Role> getAllRole() {
        return baseMapper.selectByMap(null);
    }

    private List<Role> getAllRoleList(){
        RoleService roleService = (RoleService) AopContext.currentProxy();
        return roleService.getAllRole();
    }

    @Override
    public Role selectByPrimaryKey(Object id) {
        List<Role> roleList = getAllRoleList();
        for (Role role : roleList) {
            if(role.getId().equals(id)){
                return role;
            }
        }

        return null;
    }

    @Override
    public Set<String> selectRoleNamesByUserId(Integer userId) {
        List<Role> perms = selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        if(StringUtils.isEmpty(perms)){
            return permsSet;
        }
        for (Role perm : perms)
        {
            if (null != perm && !StringUtils.isEmpty(perm.getRoleKey()))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public PageBean<RoleResp> queryListByRole(RoleReq roleReq) {
        Integer currentPage = roleReq.getCurrentPage();
        Integer pageSize = roleReq.getPageSize();

        PageBean<RoleResp> pageBean;
        List<Role> roleList = getAllRoleList();
        if(StringUtils.isNotEmpty(roleList)){
            List<RoleResp> respList = new ArrayList<>();
            for (Role role : roleList) {
                if(FilterUtil.likeStr(role.getRoleName(),roleReq.getRoleName())) {
                    RoleResp roleResp = new RoleResp();
                    BeanUtils.copyProperties(role, roleResp);
                    respList.add(roleResp);
                }
            }

            Collections.sort(respList, new Comparator<RoleResp>() {
                @Override
                public int compare(RoleResp o1, RoleResp o2) {
                    return o1.getRoleSort() - o2.getRoleSort();
                }
            });

            List<RoleResp> resultList = PageUtil.pagedList(currentPage,pageSize,respList);
            pageBean = new PageBean(currentPage,pageSize,respList.size(),resultList);
        }else{
            pageBean = new PageBean(currentPage,pageSize,0,null);
        }
        return pageBean;
    }

    @Override
    public List<Role> selectRolesByUserId(Integer uid) {
        return systemManualMapper.selectRolesByUserId(uid);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CommonConstant.SYS_ROLE_KEY,allEntries = true)
    @Override
    public ResponseResult insertOrUpdate(RoleReq roleReq) {
        List<Role> roleList = getAllRoleList();

        Integer roleId = roleReq.getId();
        if(StringUtils.isEmpty(roleId)){
            return insertByVo(roleReq, roleList);
        }else{
            Role roleDb = selectByPrimaryKey(roleReq.getId());
            if(StringUtils.isNull(roleDb)){
                throw new RequestException(ResponseCode.FAIL.code,"角色不存在");
            }

            if(SystemManager.isAdminRole(roleDb.getRoleKey())){
                return ResponseResult.buildOkResult();
            }

            if(StringUtils.isNull(roleDb)){
                throw new RequestException(ResponseCode.FAIL.code,"请传入正确的角色ID");
            }
            return updateByRoleVo(roleReq, roleDb,roleList);

        }
    }

    @Override
    public List<Integer> getMenuIdsByRoleId(Integer roleId) {
        List<Integer> menuIds = new ArrayList<>();
        if(StringUtils.isEmpty(roleId)){
            return menuIds;
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("roleId",roleId);
        List<RoleMenu> roleMenuList = roleMenuService.selectByMap(queryMap);
        for (RoleMenu roleMenu : roleMenuList) {
            menuIds.add(roleMenu.getMenuId());
        }
        return menuIds;
    }

    private ResponseResult updateByRoleVo(RoleReq roleReq,Role oldRole, Collection<Role> roleList) {
        Integer roleId = roleReq.getId();
        if((StringUtils.isNotEmpty(roleReq.getRoleKey()) && !roleReq.getRoleKey().equals(oldRole.getRoleKey())) ||
                (StringUtils.isNotEmpty(roleReq.getRoleName()) && !roleReq.getRoleName().equals(oldRole.getRoleName()))){
            //判断
            if(checkUnique(roleReq.getRoleName(),roleReq.getRoleKey(),roleList)){
                throw new RequestException(ResponseCode.FAIL.code,"角色名或权限字符重复");
            }
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleReq,role);
        int updateResult = this.updateByPrimaryKeySelective(role);

        //更新角色菜单关联关系
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("roleId",roleId);
        List<RoleMenu> roleMenuList = roleMenuService.selectByMap(queryMap);

        List<Integer> menuIdList = roleReq.getMenuIds();
        if(StringUtils.isEmpty(menuIdList)){
            if(StringUtils.isNotEmpty(roleMenuList)){
                List<Long> roleMenuIdList = new ArrayList<>();
                for (RoleMenu roleMenu : roleMenuList) {
                    roleMenuIdList.add(roleMenu.getId());
                }
                roleMenuService.deleteBatchIds(roleMenuIdList);
            }
        }else {
            Integer oldMenuId;
            List<Long> delRoleMenuIds = new ArrayList<>();
            for (RoleMenu roleMenu : roleMenuList) {
                oldMenuId = roleMenu.getMenuId();
                if (menuIdList.contains(oldMenuId)){
                    menuIdList.remove(oldMenuId);
                }else{
                    delRoleMenuIds.add(roleMenu.getId());
                }
            }
            if(StringUtils.isNotEmpty(delRoleMenuIds)) {
                roleMenuService.deleteBatchIds(delRoleMenuIds);
            }


            if(StringUtils.isNotEmpty(menuIdList)) {
                RoleMenu roleMenu;
                roleMenuList = new ArrayList<>();
                for (Integer menuId : menuIdList) {
                    roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    roleMenuList.add(roleMenu);
                }
                roleMenuService.insertBatch(roleMenuList);
            }
        }

        return updateResult>0 ? ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    private ResponseResult insertByVo(RoleReq roleReq, List<Role> roleList) {
        //判断
        if(StringUtils.isEmpty(roleReq.getRoleName())){
            throw new RequestException(ResponseCode.FAIL.code,"角色名不能为空");
        }

        if(StringUtils.isEmpty(roleReq.getRoleKey())){
            throw new RequestException(ResponseCode.FAIL.code,"权限字符不能为空");
        }

        if(checkUnique(roleReq.getRoleName(),roleReq.getRoleKey(),roleList)){
            throw new RequestException(ResponseCode.FAIL.code,"角色名或权限字符重复");
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleReq,role);
        if(StringUtils.isNull(role.getRoleSort())){
            role.setRoleSort(1);
        }
        if(StringUtils.isNull(role.getStatus())){
            role.setStatus(Status.NORMAL.id);
        }
        int insertResult = this.insert(role);

        //添加角色菜单关联关系
        List<Integer> menuIdList = roleReq.getMenuIds();
        if(StringUtils.isNotEmpty(menuIdList)){
            RoleMenu roleMenu;
            List<RoleMenu> roleMenuList = new ArrayList<>();
            for (Integer menuId : menuIdList) {
                roleMenu = new RoleMenu();
                roleMenu.setMenuId(menuId);
                roleMenu.setRoleId(role.getId());
                roleMenuList.add(roleMenu);
            }
            roleMenuService.insertBatch(roleMenuList);
        }

        return insertResult>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    /**
     * 校验角色名，权限字符是否唯一
     * @param roleName
     * @param roleKey
     * @param roleList
     * @return
     */
    private boolean checkUnique(String roleName,String roleKey,Collection<Role> roleList){
        if(StringUtils.isEmpty(roleList)){
            return false;
        }
        for (Role role : roleList) {
            if(StringUtils.isNotEmpty(roleName) && roleName.equals(role.getRoleName())){
                return true;
            }
            if(StringUtils.isNotEmpty(roleKey) && roleKey.equals(role.getRoleKey())){
                return true;
            }
        }

        return false;
    }


    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CommonConstant.SYS_ROLE_KEY,allEntries = true)
    @Override
    public int deleteByPrimaryKey(Object id) {
        //删除用户角色关联关系
        Map<String,Object> delUserRoleMap = new HashMap<>();
        delUserRoleMap.put("roleId",id);
        systemManualMapper.deleteUserRoleByMap(delUserRoleMap);

        //删除角色菜单关联关系
        systemManualMapper.deleteRoleMenuByMap(delUserRoleMap);

        return super.deleteByPrimaryKey(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CommonConstant.SYS_ROLE_KEY,allEntries = true)
    @Override
    public int deleteBatchIds(List ids) throws RequestException {
        //删除用户角色关联关系
        Map<String,Object> delUserRoleMap = new HashMap<>();
        delUserRoleMap.put("roleIds",ids);
        systemManualMapper.deleteUserRoleByMap(delUserRoleMap);

        //删除角色菜单关联关系
        systemManualMapper.deleteRoleMenuByMap(delUserRoleMap);
        return super.deleteBatchIds(ids);
    }
}
