package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.PageBean;
import com.mailian.core.bean.ResponseResult;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.manager.SystemManager;
import com.mailian.firecontrol.dao.auto.model.Role;
import com.mailian.firecontrol.dto.web.request.RoleReq;
import com.mailian.firecontrol.dto.web.response.RoleResp;
import com.mailian.firecontrol.framework.util.ShiroUtils;
import com.mailian.firecontrol.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/28
 * @Description:
 */
@RestController
@RequestMapping("/system/role")
@Api(description = "角色相关接口")
@WebAPI
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @Log(title = "系统",action = "获取角色列表")
    @ApiOperation(value = "获取角色列表", httpMethod = "GET",notes = "支持分页,含禁用数据")
    @ApiImplicitParam(name = "roleReq", value = "角色查询参数", required = false, dataType = "RoleReq")
    @RequestMapping(value="/getList",method = RequestMethod.GET)
    public ResponseResult<PageBean<RoleResp>> getList(RoleReq roleReq){
        return ResponseResult.buildOkResult(roleService.queryListByRole(roleReq));
    }

    @Log(title = "系统",action = "获取所有角色")
    @ApiOperation(value = "获取所有角色", httpMethod = "GET",notes = "用于选择角色,禁用的不显示")
    @RequestMapping(value="/getAllList",method = RequestMethod.GET)
    public ResponseResult<List<RoleResp>> getAllList(){
        List<RoleResp> roleRespList = new ArrayList<>();
        List<Role> roleList = roleService.getAllRole();
        if(StringUtils.isNotEmpty(roleList)){
            for (Role role : roleList) {
                if(Status.DISABLE.id.equals(role.getStatus())){
                    continue;
                }
                RoleResp roleResp = new RoleResp();
                BeanUtils.copyProperties(role,roleResp);
                roleRespList.add(roleResp);
            }

            Collections.sort(roleRespList, new Comparator<RoleResp>() {
                @Override
                public int compare(RoleResp o1, RoleResp o2) {
                    return o1.getRoleSort() - o2.getRoleSort();
                }
            });

        }
        return ResponseResult.buildOkResult(roleRespList);
    }

    @Log(title = "系统",action = "获取角色详情")
    @ApiOperation(value = "角色详情", httpMethod = "GET",notes = "根据角色id获取角色详细信息")
    @RequestMapping(value="/detail/{roleId}",method = RequestMethod.GET)
    public ResponseResult<RoleResp> detail(@ApiParam(name="roleId",value = "角色id",required = true) @PathVariable("roleId") Integer roleId) {
        Role role = roleService.selectByPrimaryKey(roleId);

        RoleResp roleResp = null;
        if(StringUtils.isNotNull(role)){
            roleResp = new RoleResp();
            BeanUtils.copyProperties(role,roleResp);
        }
        return ResponseResult.buildOkResult(roleResp);
    }

    @Log(title = "系统",action = "获取角色拥有的菜单")
    @ApiOperation(value = "获取角色拥有的菜单", httpMethod = "GET",notes = "根据角色id获取角色详细信息")
    @RequestMapping(value="/getMenuIds",method = RequestMethod.GET)
    public ResponseResult<List<Integer>> getMenuIds(@ApiParam(name="roleId",value = "角色id",required = true) @RequestParam(value="roleId") Integer roleId) {
        List<Integer> menuIds = roleService.getMenuIdsByRoleId(roleId);
        return ResponseResult.buildOkResult(menuIds);
    }


    @Log(title = "系统",action = "新增或修改角色")
    @ApiOperation(value = "保存", httpMethod = "POST",notes = "支持新增修改")
    @ApiImplicitParam(name = "roleReq", value = "角色参数", required = true, dataType = "RoleReq")
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public ResponseResult save(@RequestBody RoleReq roleReq){
        ResponseResult responseResult = roleService.insertOrUpdate(roleReq);
        ShiroUtils.clearCachedAuthorizationInfo();
        return responseResult;
    }


    @Log(title = "系统",action = "删除角色")
    @ApiOperation(value = "删除角色", httpMethod = "GET")
    @RequestMapping(value="/delete/{roleId}",method = RequestMethod.GET)
    public ResponseResult delete(@ApiParam(name="roleId",value = "角色ID",required = true) @PathVariable("roleId") Integer roleId){
        Role role = roleService.selectByPrimaryKey(roleId);
        int result = 1;
        if(StringUtils.isNotNull(role)) {
            if (SystemManager.isAdminRole(role.getRoleKey())) {
                return error("不允许删除超级管理员角色");
            }

            result = roleService.deleteByPrimaryKey(roleId);
            ShiroUtils.clearCachedAuthorizationInfo();
        }
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

}
