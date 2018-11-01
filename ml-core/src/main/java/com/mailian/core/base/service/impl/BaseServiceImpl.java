package com.mailian.core.base.service.impl;

import com.mailian.core.base.mapper.BaseMapper;
import com.mailian.core.base.model.BaseDomain;
import com.mailian.core.base.service.BaseService;
import com.mailian.core.bean.BaseUserInfo;
import com.mailian.core.bean.SpringContext;
import com.mailian.core.constants.CoreCommonConstant;
import com.mailian.core.enums.ResponseCode;
import com.mailian.core.exception.RequestException;
import com.mailian.core.util.PageUtil;
import com.mailian.core.util.ReflectionKit;
import com.mailian.core.util.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service基类实现
 * @author wangqiaoqing
 *
 */
public abstract class BaseServiceImpl<T extends BaseDomain,M extends BaseMapper<T>> implements BaseService<T> {
    private static final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    protected M baseMapper;

    public BaseServiceImpl() {
    }

    protected Class<M> currentModelClass() {
        return ReflectionKit.getSuperClassGenricType(this.getClass(), 1);
    }

    @Override
    public List<T> selectByMap(Map<String,Object> map) {
        return baseMapper.selectByMap(map);
    }

    @Override
    public int deleteByPrimaryKey(Object id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record) {
        setUserNameAndTime(record,true);
        return baseMapper.insert(record);
    }

    @Override
    public int insertSelective(T record) {
        setUserNameAndTime(record,true);
        return baseMapper.insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(Object id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        setUserNameAndTime(record,false);
        return baseMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        setUserNameAndTime(record,false);
        return baseMapper.updateByPrimaryKeySelective(record);
    }

    public void setUserNameAndTime(BaseDomain record,boolean needCreate){
        Subject subject = SecurityUtils.getSubject();
        Date now = new Date();
        String userName = "system";
        if(StringUtils.isNotNull(subject)) {
            BaseUserInfo baseUserInfo = (BaseUserInfo) subject.getPrincipal();
            if(StringUtils.isNotNull(baseUserInfo)) {
                userName = baseUserInfo.getUserName();
            }
        }
        if (needCreate) {
            record.setCreateBy(userName);
            record.setCreateTime(now);
        }
        record.setUpdateBy(userName);
        record.setUpdateTime(now);
    }

    @Override
    public int deleteBatchIds(List ids) throws RequestException {
        if(StringUtils.isEmpty(ids)){
            throw new RequestException(ResponseCode.FAIL.code,"删除ids不能为空!");
        }

        Integer totalPage = PageUtil.getTotalPage(CoreCommonConstant.BATCH_MAX_SIZE,ids.size());
        int result = 0;
        for (int i=1; i<=totalPage;i++) {
            List<T> addList = PageUtil.pagedList(i,CoreCommonConstant.BATCH_MAX_SIZE,ids);
            result = result + baseMapper.deleteBatchIds(addList);
        }
        return result;
    }

    @Override
    public List<T> selectBatchIds(Collection ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public int insertBatch(List<T> objs) {
        if(StringUtils.isEmpty(objs)){
            throw new RequestException(ResponseCode.FAIL.code,"新增的数据不能为空!");
        }
        Integer totalPage = PageUtil.getTotalPage(CoreCommonConstant.BATCH_MAX_SIZE,objs.size());
        int result = 0;
        SqlSession batchSqlSession = null;
        try {
            SqlSessionTemplate sqlSessionTemplate = SpringContext.getBean(SqlSessionTemplate.class);
            batchSqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
            M baseMapper = batchSqlSession.getMapper(this.currentModelClass());
            for (int i=1; i<=totalPage;i++) {
                List<T> addList = PageUtil.pagedList(i,CoreCommonConstant.BATCH_MAX_SIZE,objs);
                baseMapper.insertBatch(addList);
                //提交事务
                batchSqlSession.commit();
                //清理缓存，防止溢出
                batchSqlSession.clearCache();
            }
            result = 1;
        }catch(Exception e) {
            //回滚没有提交的数据
            batchSqlSession.rollback();
            log.error("批量新增异常",e);
            throw new RequestException(ResponseCode.FAIL.code,"批量新增失败!");
        }finally {
            batchSqlSession.close();
        }
        return result;
    }

}
