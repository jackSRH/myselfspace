package com.mailian.firecontrol.service;


import com.mailian.core.base.service.BaseService;
import com.mailian.firecontrol.dao.auto.model.Job;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/30
 * @Description:
 */
public interface JobService extends BaseService<Job> {
    /**
     * 立即运行任务
     * @param jobId
     * @return
     */
    int run(Integer jobId);


    /**
     * 暂停任务
     * @param jobId
     * @return
     */
    public int pauseJob(Integer jobId);

    /**
     * 恢复任务
     * @param jobId
     * @return
     */
    public int resumeJob(Integer jobId);
}
