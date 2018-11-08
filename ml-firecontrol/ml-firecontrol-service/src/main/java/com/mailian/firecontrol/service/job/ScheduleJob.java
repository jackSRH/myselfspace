package com.mailian.firecontrol.service.job;

import com.mailian.core.bean.SpringContext;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.ScheduleConstants;
import com.mailian.firecontrol.dao.auto.model.Job;
import com.mailian.firecontrol.dao.auto.model.JobLog;
import com.mailian.firecontrol.service.JobLogService;
import com.mailian.firecontrol.service.impl.JobLogServiceImpl;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/30
 * @Description:
 */
public class ScheduleJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Job job = new Job();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.JOB_PARAM_KEY), job);

        JobLogService jobLogService = SpringContext.getBean(JobLogServiceImpl.class);

        String jobName = job.getJobName();
        String methodName = job.getMethodName();
        String params = job.getParams();
        if(StringUtils.isEmpty(jobName) || StringUtils.isEmpty(methodName)){
            return;
        }

        JobLog jobLog = new JobLog();
        jobLog.setJobName(jobName);
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setMethodName(methodName);
        jobLog.setParams(params);
        jobLog.setCreateTime(new Date());

        long startTime = System.currentTimeMillis();

        try {
            // 执行任务
            log.info("任务开始执行 - 名称：{} 方法：{}", jobName, methodName);

            Object target = SpringContext.getBean(jobName);
            Method method;
            if (StringUtils.isNotEmpty(params)) {
                method = target.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }

            long times = System.currentTimeMillis() - startTime;
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(Status.NORMAL.id);
            jobLog.setJobMessage(job.getJobName() + " 总共耗时：" + times + "毫秒");

            log.info("任务执行结束 - 名称：{} 耗时：{} 毫秒", job.getJobName(), times);
        } catch (Exception e) {
            log.info("任务执行失败 - 名称：{} 方法：{}", job.getJobName(), job.getMethodName());
            log.error("任务执行异常  - ：", e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setJobMessage(job.getJobName() + " 总共耗时：" + times + "毫秒");
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(Status.DISABLE.id);
            jobLog.setExceptionInfo(e.getMessage());
        } finally {
            //jobLogService.insert(jobLog);
        }
    }
}
