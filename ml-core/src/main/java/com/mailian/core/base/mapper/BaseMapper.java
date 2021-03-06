package com.mailian.core.base.mapper;

import com.mailian.core.base.model.BaseDomain;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Mapper基类
 * @author wangqiaoqing
 *
 */
public interface BaseMapper<T extends BaseDomain> {
    /**
     * 根据map条件查询
     * @return
     */
    List<T> selectByMap(Map<String, Object> map);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Object id);

    /**
     * 添加
     * @param record
     * @return
     */
    int insert(T record);

    /**
     * 添加（空值不操作）
     * @param record
     * @return
     */
    int insertSelective(T record);

    /**
     * 根据主键获取对象
     * @param id
     * @return
     */
    T selectByPrimaryKey(Object id);

    /**
     * 根据主键修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键修改（空值不操作）
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据主键批量删除
     * @param ids
     * @return
     */
    int deleteBatchIds(@Param(value = "ids") List ids);

    /**
     * 根据主键批量获取
     * @param ids
     * @return
     */
    List<T> selectBatchIds(@Param(value = "ids") Collection ids);


    /**
     * 批量新增
     * @param objs
     * @return
     */
    int insertBatch(@Param(value = "objs") List<T> objs);
}
