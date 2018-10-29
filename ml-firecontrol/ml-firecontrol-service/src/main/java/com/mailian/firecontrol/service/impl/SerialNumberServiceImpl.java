package com.mailian.firecontrol.service.impl;

import com.mailian.core.base.service.impl.BaseServiceImpl;
import com.mailian.core.config.SystemConfig;
import com.mailian.core.constants.CommonConstant;
import com.mailian.core.enums.BooleanEnum;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.FreeMarkerUtil;
import com.mailian.core.util.RedisKeys;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.enums.SerialNumModule;
import com.mailian.firecontrol.dao.auto.mapper.SerialNumberMapper;
import com.mailian.firecontrol.dao.auto.model.SerialNumber;
import com.mailian.firecontrol.service.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/2
 * @Description:
 */
@Service
public class SerialNumberServiceImpl extends BaseServiceImpl<SerialNumber,SerialNumberMapper> implements SerialNumberService {

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SystemConfig systemConfig;

    /** 生成器锁 */
    private final ReentrantLock lock = new ReentrantLock();

    /** 预生成锁 */
    private final ReentrantLock prepareLock = new ReentrantLock();


    private String generatePrepareSerialNumbers(String moduleCode,SerialNumber serialNumber){
        int prepare = serialNumber.getPreMaxNum();//预生成流水号数量

        //临时List变量
        Collection<String> resultList = new ArrayList<String>(prepare);
        lock.lock();
        String redisKey = RedisKeys.getSysConfigKey(systemConfig.serverIdCard,CommonConstant.PREPARE_SERIAL_NUMBER_KEY+moduleCode);
        try{
            String pattern = serialNumber.getConfigTemplet().trim();//配置模板
            int curSerial = serialNumber.getCurSerial(); //存储当前最大值
            int isAutoIncrement = serialNumber.getIsAutoIncrement();

            for(int i=0;i<prepare;i++){
                if(BooleanEnum.YES.id.equals(isAutoIncrement)) {
                    curSerial = curSerial + 1;
                }
                //动态数字生成
                String formatSerialNum = FreeMarkerUtil.genStrBySerialNumberStrTemplate(CommonConstant.SERIAL_NUMBER_TEMPLATE,pattern,curSerial);
                resultList.add(formatSerialNum);
            }

            //更新数据
            SerialNumber systemSerialNumber = new SerialNumber();
            systemSerialNumber.setId(serialNumber.getId());
            systemSerialNumber.setCurSerial(curSerial);
            systemSerialNumber.setUpdateBy("system");
            systemSerialNumber.setUpdateTime(new Date());
            baseMapper.updateByPrimaryKeySelective(systemSerialNumber);

            listOperations.rightPushAll(redisKey,resultList.toArray());
        }finally{
            lock.unlock();
        }
        return (String) listOperations.leftPop(redisKey);
    }

    @Override
    public String generateSerialNumberByModelCode(SerialNumModule serialNumModule) {
        String moduleCode = serialNumModule.code;
        //预序列号加锁
        prepareLock.lock();
        String redisKey = RedisKeys.getSysConfigKey(systemConfig.serverIdCard,CommonConstant.PREPARE_SERIAL_NUMBER_KEY+moduleCode);
        try{
            long size = listOperations.size(redisKey);
            //判断内存中是否还有序列号
            if(size > 0){
                //若有，返回第一个，并删除
                String result = (String) listOperations.leftPop(redisKey);
                return result;
            }
        }finally {
            //预序列号解锁
            prepareLock.unlock();
        }

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("moduleCode",moduleCode);
        List<SerialNumber> serialNumberList = this.selectByMap(queryMap);
        if(StringUtils.isEmpty(serialNumberList)){
            throw new RequestException(ResponseCode.FAIL.code,"模块"+moduleCode+"不存在");
        }
        SerialNumber serialNumber = serialNumberList.get(0);

        //生成预序列号，存到缓存中
        String result = this.generatePrepareSerialNumbers(moduleCode,serialNumber);
        return result;
    }
}
