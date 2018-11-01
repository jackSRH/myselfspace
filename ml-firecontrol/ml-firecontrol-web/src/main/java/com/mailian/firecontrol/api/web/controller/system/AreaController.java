package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.Log;
import com.mailian.core.annotation.WebAPI;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.dao.auto.model.Area;
import com.mailian.firecontrol.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/1
 * @Description:
 */
@RestController
@RequestMapping("/system/area")
@Api(description = "区域相关接口")
@WebAPI
public class AreaController {
    @Autowired
    private AreaService areaService;

    @Log(title = "系统",action = "获取省份城市区域列表")
    @ApiOperation(value = "获取省份城市区域列表", httpMethod = "GET")
    @GetMapping(value = "getList")
    public ResponseResult<List<Area>> getList(){
        return ResponseResult.buildOkResult(areaService.selectAll());
    }

}
