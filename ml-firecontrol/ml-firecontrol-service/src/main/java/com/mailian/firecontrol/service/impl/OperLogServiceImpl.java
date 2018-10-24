package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.firecontrol.dao.auto.mapper.OperLogMapper;
import com.mailian.firecontrol.dao.auto.model.OperLog;
import com.mailian.firecontrol.service.OperLogService;
import org.springframework.stereotype.Service;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/24
 * @Description:
 */
@Service
public class OperLogServiceImpl extends BaseServiceImpl<OperLog,OperLogMapper> implements OperLogService {
}
