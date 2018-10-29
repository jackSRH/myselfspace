package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.enums.Status;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.dao.auto.mapper.JobMapper;
import com.mailian.firecontrol.dao.auto.model.Job;
import com.mailian.firecontrol.service.JobService;
import com.mailian.firecontrol.service.util.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/30
 * @Description:
 */
@Service
public class JobServiceImpl extends BaseServiceImpl<Job,JobMapper> implements JobService {
    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<Job> jobList = baseMapper.selectByMap(null);
        for (Job job : jobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, job.getId());
            // 如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, job);
            }
        }
    }

    @Override
    public int deleteByPrimaryKey(Object id) {
        int result = super.deleteByPrimaryKey(id);
        if(result>0){
            ScheduleUtils.deleteScheduleJob(scheduler, Integer.parseInt(id.toString()));
        }
        return result;
    }

    @Override
    public int updateByPrimaryKey(Job record) {
        Job jobDb = baseMapper.selectByPrimaryKey(record.getId());
        updateScheduleJob(record, jobDb);
        return super.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(Job record) {
        if(StringUtils.isNotEmpty(record.getCronExpression())){
            Job jobDb = baseMapper.selectByPrimaryKey(record.getId());
            updateScheduleJob(record, jobDb);
        }else{
            if(StringUtils.isNotNull(record.getStatus())){
                Job jobDb = baseMapper.selectByPrimaryKey(record.getId());
                updateScheduleJob(record, jobDb);
            }
        }
        return super.updateByPrimaryKeySelective(record);
    }

    private void updateScheduleJob(Job record, Job jobDb) {
        if(!jobDb.getCronExpression().equals(record.getCronExpression())){
            jobDb.setCronExpression(record.getCronExpression());
            ScheduleUtils.updateScheduleJob(scheduler,jobDb);
        }else {
            if (!jobDb.getStatus().equals(record.getStatus())) {
                if (record.getStatus().intValue() == Status.NORMAL.id.intValue()) {
                    ScheduleUtils.resumeJob(scheduler, record.getId());
                }
                if (record.getStatus().intValue() == Status.DISABLE.id.intValue()) {
                    ScheduleUtils.pauseJob(scheduler, record.getId());
                }
            }
        }
    }

    @Override
    public int insert(Job record) {
        int result = super.insert(record);
        if(result>0) {
            ScheduleUtils.createScheduleJob(scheduler, record);
        }
        return result;
    }

    @Override
    public int insertSelective(Job record) {
        int result = super.insertSelective(record);
        if(result>0) {
            ScheduleUtils.createScheduleJob(scheduler, record);
        }
        return result;
    }

    @Override
    public int run(Integer jobId) {
        return ScheduleUtils.run(scheduler, this.selectByPrimaryKey(jobId));
    }

    @Override
    public int pauseJob(Integer jobId) {
        Job newJob = new Job();
        newJob.setStatus(Status.DISABLE.id);
        int result = baseMapper.updateByPrimaryKeySelective(newJob);
        if (result > 0) {
            ScheduleUtils.pauseJob(scheduler, jobId);
        }
        return result;
    }

    @Override
    public int resumeJob(Integer jobId) {
        Job newJob = new Job();
        newJob.setStatus(Status.NORMAL.id);
        int result = baseMapper.updateByPrimaryKeySelective(newJob);
        if (result > 0) {
            ScheduleUtils.resumeJob(scheduler, jobId);
        }
        return result;
    }
}
