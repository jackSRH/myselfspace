package com.mailian.firecontrol.api.web.controller.system;

import com.fasterxml.jackson.annotation.JsonView;
import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.manager.ViewManager;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.model.Menu;
import com.mailian.firecontrol.dto.web.request.MenuReq;
import com.mailian.firecontrol.dto.web.response.MenuResp;
import com.mailian.firecontrol.framework.util.ShiroUtils;
import com.mailian.firecontrol.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/31
 * @Description:
 */
@RestController
@RequestMapping("/system/menu")
@Api(description = "菜单相关接口")
@WebAPI
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Log(title = "系统",action = "获取菜单列表")
    @ApiOperation(value = "获取菜单列表", httpMethod = "GET",notes = "所有数据，展示层级")
    @ApiImplicitParam(name = "menuReq", value = "菜单查询参数", required = false, dataType = "MenuReq")
    @RequestMapping(value="/getList",method = RequestMethod.GET)
    @JsonView(value = ViewManager.WebSimpleView.class)
    public ResponseResult<List<MenuResp>> getList(MenuReq menuReq){
        return ResponseResult.buildOkResult(menuService.queryListByMenu(menuReq));
    }

    @Log(title = "系统",action = "获取菜单详情")
    @ApiOperation(value = "菜单详情", httpMethod = "GET",notes = "根据菜单id获取菜单详细信息")
    @RequestMapping(value="/detail/{menuId}",method = RequestMethod.GET)
    @JsonView(value = ViewManager.WebDetailView.class)
    public ResponseResult<MenuResp> detail(@ApiParam(name="menuId",value = "菜单id",required = true) @PathVariable("menuId") Integer menuId) {
        Menu menu = menuService.selectByPrimaryKey(menuId);

        MenuResp menuInfo = new MenuResp();
        BeanUtils.copyProperties(menu,menuInfo);

        //获取父级菜单
        if(StringUtils.isNotEmpty(menu.getParentId())){
            menu = menuService.selectByPrimaryKey(menu.getParentId());
            MenuResp parentMenuInfo = new MenuResp();
            BeanUtils.copyProperties(menu,parentMenuInfo);
            menuInfo.setParentMenu(parentMenuInfo);
        }

        return ResponseResult.buildOkResult(menuInfo);
    }

    @Log(title = "系统",action = "修改或新增菜单")
    @ApiOperation(value = "保存", httpMethod = "POST",notes = "支持新增修改")
    @ApiImplicitParam(name = "menuReq", value = "菜单参数", required = true, dataType = "MenuReq")
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public ResponseResult save(@RequestBody MenuReq menuReq){
        ResponseResult responseResult = menuService.insertOrUpdate(menuReq);
        ShiroUtils.clearCachedAuthorizationInfo();
        return responseResult;
    }


    @Log(title = "系统",action = "删除菜单")
    @ApiOperation(value = "删除菜单", httpMethod = "GET")
    @RequestMapping(value="/delete/{menuId}",method = RequestMethod.GET)
    public ResponseResult delete(@ApiParam(name="menuId",value = "菜单ID",required = true) @PathVariable("menuId") Integer menuId){
        int result = menuService.deleteByPrimaryKey(menuId);
        ShiroUtils.clearCachedAuthorizationInfo();
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }
}
