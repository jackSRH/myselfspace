package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.SysConfigType;
import com.mailian.firecontrol.dao.auto.mapper.SysConfigMapper;
import com.mailian.firecontrol.dao.auto.model.SysConfig;
import com.mailian.firecontrol.service.SysConfigService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/23
 * @Description:
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig,SysConfigMapper> implements SysConfigService {
    @Resource
    private SysConfigMapper sysConfigMapper;

    @CacheEvict(key = "#record.configKey",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public int insert(SysConfig record) {
        return super.insert(record);
    }

    @CacheEvict(key = "#record.configKey",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public int insertSelective(SysConfig record) {
        return super.insertSelective(record);
    }

    @CacheEvict(key = "#record.configKey",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public int updateByPrimaryKeySelective(SysConfig record) {
        return super.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.configKey",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public int updateByPrimaryKey(SysConfig record) {
        return super.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByPrimaryKey(Object id) {
        SysConfig sysConfig = this.selectByPrimaryKey(id);
        SysConfigService sysConfigService = (SysConfigService) AopContext.currentProxy();
        return sysConfigService.deleteBySysConfig(sysConfig);
    }

    @CachePut(value = CommonConstant.SYS_CONFIG_KEY,key = "#result.configKey",condition = "#result != null")
    @Override
    public SysConfig selectByPrimaryKey(Object id) {
        return super.selectByPrimaryKey(id);
    }

    @CacheEvict(key = "#sysConfigType.key",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public int updateBySysConfigType(SysConfigType sysConfigType, String value) {
        SysConfigService sysConfigService = (SysConfigService) AopContext.currentProxy();
        SysConfig sysConfigDb = sysConfigService.getConfigByType(sysConfigType);
        if(StringUtils.isNull(sysConfigDb)){
            return 0;
        }

        SysConfig sysConfig = new SysConfig();
        sysConfig.setId(sysConfigDb.getId());
        sysConfig.setConfigValue(value);
        return this.updateByPrimaryKeySelective(sysConfig);
    }

    @Cacheable(key = "#sysConfigType.key",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public SysConfig getConfigByType(SysConfigType sysConfigType) {
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("configKey",sysConfigType.key);

        List<SysConfig> sysConfigs = this.selectByMap(queryMap);
        if(StringUtils.isNotEmpty(sysConfigs)){
            return sysConfigs.get(0);
        }
        return null;
    }

    @CacheEvict(key = "#sysConfig.configKey",value = CommonConstant.SYS_CONFIG_KEY)
    @Override
    public int deleteBySysConfig(SysConfig sysConfig) {
        return sysConfigMapper.deleteByPrimaryKey(sysConfig.getId());
    }
}
