package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.AlarmLogMapper;
import com.mailian.firecontrol.dao.auto.model.AlarmLog;
import com.mailian.firecontrol.service.AlarmLogService;
import org.springframework.stereotype.Service;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/13
 * @Description:
 */
@Service
public class AlarmLogServiceImpl extends BaseServiceImpl<AlarmLog,AlarmLogMapper> implements AlarmLogService {
}
