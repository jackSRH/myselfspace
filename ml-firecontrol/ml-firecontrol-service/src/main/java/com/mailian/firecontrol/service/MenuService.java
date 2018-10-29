package com.mailian.firecontrol.service;


import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.Menu;
import com.mailian.firecontrol.dto.web.request.MenuReq;
import com.mailian.firecontrol.dto.web.response.MenuResp;

import java.util.List;
import java.util.Set;

public interface MenuService extends BaseService<Menu> {
    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> getAllMenu();

    /**
     * 根据 userId 获取权限
     * @param userId
     * @return
     */
    Set<String> selectPermsByUserId(Integer userId);

    /**
     * 根据 userId 获取展示菜单 (仅返回目录和菜单级)
     * @param userId
     * @return
     */
    List<MenuResp> selectLoginMenusByUserId(Integer userId);

    /**
     * 查询菜单列表
     * @param menuReq
     * @return
     */
    List<MenuResp> queryListByMenu(MenuReq menuReq);

    /**
     * 新增修改菜单
     * @param menuReq
     * @return
     */
    ResponseResult insertOrUpdate(MenuReq menuReq);
}
