package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.enums.Status;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.FilterUtil;
import com.mailian.core.util.StringUtils;
import com.mailian.core.util.TreeParser;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.MenuType;
import com.mailian.firecontrol.dao.auto.mapper.MenuMapper;
import com.mailian.firecontrol.dao.auto.model.Menu;
import com.mailian.firecontrol.dao.manual.mapper.SystemManualMapper;
import com.mailian.firecontrol.dto.web.request.MenuReq;
import com.mailian.firecontrol.dto.web.response.MenuResp;
import com.mailian.firecontrol.service.MenuService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 菜单service
 *
 * @author wangqiaoqing
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu,MenuMapper> implements MenuService {
    @Resource
    private SystemManualMapper systemManualMapper;

    @Cacheable(value = CommonConstant.SYS_MENU_KEY)
    @Override
    public List<Menu> getAllMenu() {
        return selectByMap(null);
    }

    private List<Menu> getAllMenuList(){
        MenuService menuService = (MenuService) AopContext.currentProxy();
        return menuService.getAllMenu();
    }

    @Override
    public Set<String> selectPermsByUserId(Integer userId) {
        Set<String> permsSet = new HashSet<>();
        List<Integer> menuIdList = systemManualMapper.selectMenuIdsByUserId(userId);
        if(StringUtils.isEmpty(menuIdList)) {
            return permsSet;
        }

        List<Menu> menus = getAllMenuList();
        if(StringUtils.isEmpty(menus)){
            return permsSet;
        }
        for (Integer menuId : menuIdList) {
            for (Menu menu : menus) {
                if(menu.getId().equals(menuId) && StringUtils.isNotEmpty(menu.getPerms())){
                    permsSet.addAll(Arrays.asList(menu.getPerms().trim().split(",")));
                }
            }
        }
        return permsSet;
    }

    @Override
    public List<MenuResp> selectLoginMenusByUserId(Integer userId) {
        List<MenuResp> menuList = new ArrayList<>();
        List<Integer> menuIdList = systemManualMapper.selectMenuIdsByUserId(userId);
        if(StringUtils.isNotEmpty(menuIdList)){
            List<Menu> menus = getAllMenuList();

            if(StringUtils.isEmpty(menus)){
                return menuList;
            }
            for (Integer menuId : menuIdList) {
                for (Menu menu : menus) {
                    if (menu.getId().equals(menuId) && MenuType.getLoginMenuTypeIds().contains(menu.getMenuType())) {
                        MenuResp menuResp = new MenuResp();
                        BeanUtils.copyProperties(menu, menuResp);
                        menuList.add(menuResp);
                    }
                }
            }
        }
        return menuList;
    }

    @Override
    public List<MenuResp> queryListByMenu(MenuReq menuReq) {
        List<MenuResp> menuList = new ArrayList<>();
        List<Menu> menus = getAllMenuList();
        if(StringUtils.isEmpty(menus)){
            return menuList;
        }

        Integer parentId = null;
        for (Menu menu : menus) {
            if(!FilterUtil.likeStr(menu.getMenuName(),menuReq.getMenuName())){
                continue;
            }
            if(StringUtils.isNotNull(menuReq.getVisible()) && !menuReq.getVisible().equals(menu.getVisible())){
                continue;
            }

            if(StringUtils.isNull(parentId)){
                parentId = menu.getParentId();
            }

            if(menu.getParentId().intValue() < parentId){
                parentId = menu.getParentId();
            }

            MenuResp menuResp = new MenuResp();
            BeanUtils.copyProperties(menu,menuResp);
            menuList.add(menuResp);
        }

        List<MenuResp> menuResps = new ArrayList<>();
        if(StringUtils.isNotNull(parentId)){
            menuResps = TreeParser.getTreeList(parentId.toString(),menuList,true);
        }
        return menuResps;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CommonConstant.SYS_MENU_KEY,allEntries = true)
    @Override
    public ResponseResult insertOrUpdate(MenuReq menuReq) {
        List<Menu> menus = getAllMenuList();
        if(StringUtils.isEmpty(menuReq.getId())){
            return insertByMenuInfo(menuReq,menus);
        }else{
            Menu menu = new Menu();
            BeanUtils.copyProperties(menuReq,menu);
            int updateResult = this.updateByPrimaryKeySelective(menu);

            return updateResult>0 ? ResponseResult.buildOkResult():ResponseResult.buildFailResult();
        }
    }

    @Override
    public Menu selectByPrimaryKey(Object id) {
        List<Menu> menus = getAllMenuList();
        if(StringUtils.isEmpty(menus)){
            throw new RequestException(ResponseCode.FAIL.code,"传入的ID不存在");
        }

        Menu menuDb = null;
        for (Menu menu : menus) {
            if(menu.getId().equals(id)){
                menuDb = menu;
            }
        }

        if(StringUtils.isNull(menuDb)){
            throw new RequestException(ResponseCode.FAIL.code,"传入的ID不存在");
        }
        return menuDb;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CommonConstant.SYS_MENU_KEY,allEntries = true)
    @Override
    public int deleteByPrimaryKey(Object id) {
        Menu menu = selectByPrimaryKey(id);

        MenuResp menuResp = new MenuResp();
        BeanUtils.copyProperties(menu,menuResp);

        List<Menu> menus = getAllMenuList();
        List<MenuResp> menuResps = new ArrayList<>();
        for (Menu menuDb : menus) {
            MenuResp menuRespDb = new MenuResp();
            BeanUtils.copyProperties(menuDb,menuRespDb);
            menuResps.add(menuRespDb);
        }

        List<String> chiledIdList = TreeParser.getChildIdList(menuResps,menuResp);

        return this.deleteBatchIds(chiledIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CommonConstant.SYS_MENU_KEY,allEntries = true)
    @Override
    public int deleteBatchIds(List ids) throws RequestException {
        //删除角色菜单关系
        Map<String,Object> delRoleMenuMap = new HashMap<>();
        delRoleMenuMap.put("menuIds",ids);
        systemManualMapper.deleteRoleMenuByMap(delRoleMenuMap);
        return baseMapper.deleteBatchIds(ids);
    }

    private ResponseResult insertByMenuInfo(MenuReq menuReq, List<Menu> menus) {
        if(StringUtils.isEmpty(menuReq.getMenuName())){
            throw new RequestException(ResponseCode.FAIL.code,"菜单名不能为空");
        }

        if(StringUtils.isEmpty(menuReq.getPerms())){
            throw new RequestException(ResponseCode.FAIL.code,"权限字符不能为空");
        }

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuReq,menu);

        menu.setId(null);
        if(StringUtils.isNull(menu.getParentId())){
            menu.setParentId(0);
        }
        if(StringUtils.isNull(menu.getVisible())){
            menu.setVisible(Status.NORMAL.id.byteValue());
        }
        if(StringUtils.isNull(menu.getOrderNum())){
            menu.setOrderNum(1);
        }
        int insertResult = this.insert(menu);
        return insertResult>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }
}
