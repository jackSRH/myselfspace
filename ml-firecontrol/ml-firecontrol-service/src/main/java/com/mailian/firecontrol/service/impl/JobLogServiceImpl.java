package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.JobLogMapper;
import com.mailian.firecontrol.dao.auto.model.JobLog;
import com.mailian.firecontrol.service.JobLogService;
import org.springframework.stereotype.Service;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/29
 * @Description:
 */
@Service
public class JobLogServiceImpl extends BaseServiceImpl<JobLog,JobLogMapper> implements JobLogService {
}
