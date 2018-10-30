package com.mailian.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description:
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ValueOperations<String,String> valueOperations;
    @Autowired
    private HashOperations hashOperations;
    @Autowired
    private ListOperations listOperations;
    @Autowired
    private SetOperations setOperations;
    @Autowired
    private ZSetOperations zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 30;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public <T> T get(String key, TypeReference<T> type, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, type);
    }

    public <T> T get(String key, TypeReference<T> type) {
        return get(key, type, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, TypeReference<T> type){
        return JSON.parseObject(json, type);
    }

    public <T> void rightPushAll(String key, List<T> objs, long expire){
        listOperations.rightPushAll(key,objs.toArray());
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public <T> void rightPushAll(String key, List<T> objs){
        rightPushAll(key, objs, DEFAULT_EXPIRE);
    }

    public <T> T leftPop(String key) {
        return (T)listOperations.leftPop(key);
    }

    public long size(String key){
        return listOperations.size(key);
    }


    /**
     * 设置过期时间
     * @param key
     * @param expire
     */
    private void setExpire (String key,long expire){
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    //---------------------------------------------------------------------
    // HashOperations -> Redis Redis Hash 操作
    //---------------------------------------------------------------------

    /**
     * 向redis 中添加内容
     * @param key       保存key
     * @param hashKey   hashKey
     * @param data      保存对象 data
     * @param expire    过期时间    -1：表示不过期
     */
    public void addHashValue(String key,String hashKey, Object data, long expire) {
        hashOperations.put(key, hashKey, data);

        setExpire(key,expire);
    }

    /**
     * Hash 添加数据
     * @param key   key
     * @param map   data
     */
    public <T> void addAllHashValue(String key, Map<String, T> map, long expire) {
        hashOperations.putAll(key, map);

        setExpire(key,expire);
    }

    /**
     * 删除hash key
     * @param key       key
     * @param hashKey   hashKey
     */
    public long deleteHashValue(String key, String hashKey) {
        return hashOperations.delete(key, hashKey);
    }

    /**
     * 获取数据
     */
    public <T> T getHashValue(String key, String hashKey) {
        return (T)hashOperations.get(key, hashKey);
    }

    /**
     * 判断是否存在该键
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hasHashKey(String key, String hashKey){
        return hashOperations.hasKey(key,hashKey);
    }

    /**
     * 批量获取数据
     */
    public <T> List<T> getHashAllValue(String key) {
        return hashOperations.values(key);
    }

    /**
     * 批量获取指定hashKey的数据
     */
    public <T> List<T> getHashMultiValue(String key, List<String> hashKeys) {
        return  hashOperations.multiGet(key, hashKeys);
    }

    /**
     * 获取Map
     * @param key
     * @param <HK>
     * @param <HV>
     * @return
     */
    public <HK,HV> Map<HK,HV> entries(String key){
        return hashOperations.entries(key);
    }

    /**
     * 获取hash数量
     */
    public Long getHashCount(String key) {
        return hashOperations.size(key);
    }


    //---------------------------------------------------------------------
    // ZSetOperations -> Redis Sort Set 操作
    //---------------------------------------------------------------------

    /**
     * 设置zset值
     */
    public boolean addZSetValue(String key, Object member, long score){
        return zSetOperations.add(key, member, score);
    }

    /**
     * 设置zset值
     */
    public boolean addZSetValue(String key, Object member, double score){
        return zSetOperations.add(key, member, score);
    }

    /**
     * 批量设置zset值
     */
    public long addBatchZSetValue(String key, Set<ZSetOperations.TypedTuple<Object>> tuples){
        return zSetOperations.add(key, tuples);
    }

    /**
     * 自增zset值
     */
    public void incZSetValue(String key, String member, long delta){
        zSetOperations.incrementScore(key, member, delta);
    }

    /**
     * 获取zset数量
     */
    public long getZSetScore(String key, String member){
        Double score = zSetOperations.score(key, member);
        if(score==null){
            return 0;
        }else{
            return score.longValue();
        }
    }

    /**
     * 获取有序集 key 中成员 member 的排名 。其中有序集成员按 score 值递减 (从小到大) 排序。
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetRank(String key, long start, long end){
        return zSetOperations.rangeWithScores(key, start, end);
    }


    //---------------------------------------------------------------------
    // listOperations -> Redis List() 操作
    //---------------------------------------------------------------------

    /**
     * 添加list列表
     */
    public void addListValue(String key,Object list){
        listOperations.leftPush(key,list);
    }

    /**
     * 获取指定Key对应的list
     */
    public Object getListValue(String key){
        return listOperations.leftPop(key);
    }

    //---------------------------------------------------------------------
    // setOperations -> Redis Set() 操作
    //---------------------------------------------------------------------

    /**
     * 添加Set集合集合
     */
    public void addSetValue(String key,Object list){
        setOperations.add(key,list);
    }

    /**
     * 获取指定Key对应的set
     */
    public Object getSetValue(String key){
        return setOperations.members(key);
    }

    /**
     * 获取并移除指定key的值
     */
    public Object popSetValue(String key){
        return setOperations.pop(key);
    }

}
