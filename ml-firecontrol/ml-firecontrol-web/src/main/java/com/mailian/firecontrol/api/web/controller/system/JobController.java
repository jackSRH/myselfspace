package com.mailian.firecontrol.api.web.controller.system;

import com.mailian.core.annotation.WebAPI;
import com.mailian.core.base.controller.BaseController;
import com.mailian.core.bean.ResponseResult;
import com.mailian.firecontrol.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/30
 * @Description:
 */
@RestController
@RequestMapping("/system/job")
@Api(description = "系统任务相关接口 (用于测试)")
@WebAPI
public class JobController extends BaseController {
    @Autowired
    private JobService jobService;

    @ApiOperation(value = "立即执行任务", httpMethod = "GET",notes = "根据任务id立即执行任务")
    @RequestMapping(value="/run/{jobId}",method = RequestMethod.GET)
    public ResponseResult run(@ApiParam(name="jobId",value = "任务id",required = true) @PathVariable("jobId") Integer jobId) {
        int result = jobService.run(jobId);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @ApiOperation(value = "暂停任务", httpMethod = "GET",notes = "根据任务id 暂停任务")
    @RequestMapping(value="/pauseJob/{jobId}",method = RequestMethod.GET)
    public ResponseResult pauseJob(@ApiParam(name="jobId",value = "任务id",required = true) @PathVariable("jobId") Integer jobId) {
        int result = jobService.pauseJob(jobId);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

    @ApiOperation(value = "恢复任务", httpMethod = "GET",notes = "根据任务id恢复任务")
    @RequestMapping(value="/resumeJob/{jobId}",method = RequestMethod.GET)
    public ResponseResult resumeJob(@ApiParam(name="jobId",value = "任务id",required = true) @PathVariable("jobId") Integer jobId) {
        int result = jobService.resumeJob(jobId);
        return result>0?ResponseResult.buildOkResult():ResponseResult.buildFailResult();
    }

}
