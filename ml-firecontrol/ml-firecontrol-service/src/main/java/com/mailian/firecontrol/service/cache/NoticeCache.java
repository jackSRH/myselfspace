package com.mailian.firecontrol.service.cache;

import com.alibaba.fastjson.JSON;
import com.mailian.core.bean.BasePage;
import com.mailian.core.bean.PageBean;
import com.mailian.core.config.SystemConfig;
import com.mailian.core.enums.BooleanEnum;
import com.mailian.core.push.UmengPushRepository;
import com.mailian.core.util.RedisUtils;
import com.mailian.core.util.StringUtils;
import com.mailian.firecontrol.common.constants.CommonConstant;
import com.mailian.firecontrol.common.enums.NoticeType;
import com.mailian.firecontrol.dao.auto.mapper.UserMapper;
import com.mailian.firecontrol.dao.auto.model.User;
import com.mailian.firecontrol.dao.manual.mapper.SystemManualMapper;
import com.mailian.firecontrol.dto.app.NoticeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/21
 * @Description:
 */
@Component
public class NoticeCache {
    private static final Logger log = LoggerFactory.getLogger(NoticeCache.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UmengPushRepository umengPushRepository;
    @Resource
    private SystemManualMapper systemManualMapper;
    @Autowired
    private SystemConfig systemConfig;
    @Resource
    private UserMapper userMapper;

    /**
     * 添加消息
     * @param noticeInfo
     * @return
     */
    @Async
    public void addNotice(NoticeInfo noticeInfo){
        String noticeKey = getNoticeKey(noticeInfo.getType());
        redisUtils.leftPush(noticeKey,noticeInfo);

        /* 调用第三方推送通知APP */
        if(StringUtils.isNull(noticeInfo.getUid())){
            /* 获取该单位的用户*/
            List<Integer> unitUids = new ArrayList<>();
            if(StringUtils.isNotEmpty(noticeInfo.getUnitId())){
                unitUids = systemManualMapper.selectUidsByUnitId(noticeInfo.getUnitId());
                for (Integer unitUid : unitUids) {
                    String toUser = String.format(CommonConstant.USRE_KEY,systemConfig.serverIdCard,unitUid);
                    try {
                        umengPushRepository.sendUserAndroidCustomizedcast(toUser,NoticeType.getValue(noticeInfo.getType()).value,noticeInfo.getType().intValue(),noticeInfo.getContent());
                        log.error("用户端消息推送成功,消息内容:{},目标键:{}",JSON.toJSONString(noticeInfo),toUser);
                    } catch (Exception e) {
                        log.error("用户端消息推送失败,消息内容:{},目标键:{}",JSON.toJSONString(noticeInfo),toUser,e);
                    }
                }
            }

            /* 获取所有该管辖区用户 */
            List<Integer> uidList = systemManualMapper.selectUidsByPrecinctId(noticeInfo.getPrecinctId());
            List<Integer> adminUidList = systemManualMapper.selectAdminUserIds();
            for (Integer adminUid : adminUidList) {
                if(!uidList.contains(adminUid)){
                    uidList.add(adminUid);
                }
            }
            uidList.removeAll(unitUids);
            for (Integer uid : uidList) {
                String toUser = String.format(CommonConstant.USRE_KEY,systemConfig.serverIdCard,uid);
                try {
                    umengPushRepository.sendManagerAndroidCustomizedcast(toUser,NoticeType.getValue(noticeInfo.getType()).value,noticeInfo.getType().intValue(),noticeInfo.getContent());
                    log.error("管理端消息推送成功,消息内容:{},目标键:{}",JSON.toJSONString(noticeInfo),toUser);
                } catch (Exception e) {
                    log.error("管理端消息推送失败,消息内容:{},目标键:{}",JSON.toJSONString(noticeInfo),toUser,e);
                }
            }
        }else{
            User user = userMapper.selectByPrimaryKey(noticeInfo.getUid());

            String toUser = String.format(CommonConstant.USRE_KEY, systemConfig.serverIdCard, noticeInfo.getUid());
            if(StringUtils.isNotEmpty(user.getUnitId())) {
                try {
                    umengPushRepository.sendUserAndroidCustomizedcast(toUser, NoticeType.getValue(noticeInfo.getType().intValue()).value, noticeInfo.getType().intValue(), noticeInfo.getContent());
                    log.error("用户端消息推送成功,消息内容:{},目标键:{}",JSON.toJSONString(noticeInfo),toUser);
                } catch (Exception e) {
                    log.error("用户端消息推送失败,消息内容:{},目标邮箱:{}", JSON.toJSONString(noticeInfo), toUser, e);
                }
            }else{
                try {
                    umengPushRepository.sendManagerAndroidCustomizedcast(toUser, NoticeType.getValue(noticeInfo.getType().intValue()).value, noticeInfo.getType().intValue(), noticeInfo.getContent());
                    log.error("管理端消息推送成功,消息内容:{},目标键:{}",JSON.toJSONString(noticeInfo),toUser);
                } catch (Exception e) {
                    log.error("管理端消息推送失败,消息内容:{},目标邮箱:{}", JSON.toJSONString(noticeInfo), toUser, e);
                }
            }
        }

    }

    /**
     * 移除消息
     * @param noticeInfo
     * @return
     */
    @Async
    public void removeNotice(NoticeInfo noticeInfo){
        String noticeKey = getNoticeKey(noticeInfo.getType());
        Map<String,Object> resultMap = getNoticeInfo(noticeKey,noticeInfo);
        if(StringUtils.isNotNull(noticeKey) && resultMap.containsKey("noticeInfo")) {
            NoticeInfo noticeInfoDb = (NoticeInfo) resultMap.get("noticeInfo");
            redisUtils.deleteListValue(noticeKey, 1, noticeInfoDb);
        }
    }


    /**
     * 获取具体通知信息
     * @param noticeInfo
     * @return
     */
    public Map<String,Object> getNoticeInfo(String noticeKey,NoticeInfo noticeInfo){
        Map<String,Object> resultMap = new HashMap<>();
        if(StringUtils.isNull(noticeInfo.getId()) || StringUtils.isNull(noticeInfo.getRealType())){
            return resultMap;
        }

        List<Object> objects = redisUtils.listRange(noticeKey,0,-1);
        if(StringUtils.isNotEmpty(objects)) {
            for (int i = 0; i < objects.size(); i++) {
                NoticeInfo noticeInfoDb = (NoticeInfo) objects.get(i);
                if (noticeInfoDb.getId().equals(noticeInfo.getId()) &&
                        noticeInfoDb.getRealType().intValue() == noticeInfo.getRealType().intValue() &&
                        noticeInfo.getUid() == noticeInfoDb.getUid()) {
                    resultMap.put("index",i);
                    resultMap.put("noticeInfo",noticeInfoDb);
                    break;
                }
            }
        }

        return resultMap;
    }

    @Async
    public void readNotice(NoticeInfo noticeInfo){
        String noticeKey = getNoticeKey(noticeInfo.getType());
        Map<String,Object> resultMap = getNoticeInfo(noticeKey,noticeInfo);
        if(StringUtils.isNotNull(noticeKey) && resultMap.containsKey("noticeInfo")) {
            Integer index = (Integer) resultMap.get("index");
            NoticeInfo noticeInfoDb = (NoticeInfo) resultMap.get("noticeInfo");
            noticeInfoDb.setRead(BooleanEnum.YES.id);
            redisUtils.listSet(noticeKey, index, noticeInfoDb);
        }
    }

    /**
     * 根据 消息类型获取消息列表
     * @param noticeType 消息类型
     * @param basePage 分页信息
     * @return
     */
    public PageBean<NoticeInfo> getNoticesByTypePage(NoticeType noticeType, BasePage basePage){
        String noticeKey = getNoticeKey(noticeType.id);

        int total = (int) redisUtils.size(noticeKey);

        PageBean<NoticeInfo> pageBean = new PageBean<>(basePage.getCurrentPage(),basePage.getPageSize(),total,null);

        List<Object> objects = redisUtils.listRange(noticeKey,pageBean.getStartIndex().longValue(),(pageBean.getStartIndex()+pageBean.getPageSize()));
        List<NoticeInfo> noticeInfos = new ArrayList<>();
        for (Object object : objects) {
            NoticeInfo noticeInfo = (NoticeInfo) object;
            noticeInfos.add(noticeInfo);
        }

        pageBean.setItems(noticeInfos);
        return pageBean;
    }


    /**
     * 根据 消息类型获取消息列表
     * @param noticeType 消息类型
     * @return
     */
    public List<NoticeInfo> getNoticesByType(NoticeType noticeType){
        String noticeKey = getNoticeKey(noticeType.id);

        List<Object> objects = redisUtils.listRange(noticeKey,0,-1);
        List<NoticeInfo> noticeInfos = new ArrayList<>();
        for (Object object : objects) {
            NoticeInfo noticeInfo = (NoticeInfo) object;
            noticeInfos.add(noticeInfo);
        }
        return noticeInfos;
    }

    /**
     * 获取通知数量
     * @param noticeType
     * @return
     */
    public Integer getNoticeCountByType(NoticeType noticeType){
        String noticeKey = getNoticeKey(noticeType.id);
        return (int)redisUtils.size(noticeKey);
    }

    /**
     * 获取最新通知
     * @param noticeType
     * @return
     */
    public NoticeInfo getFristNoticeByType(NoticeType noticeType){
        String noticeKey = getNoticeKey(noticeType.id);

        List<Object> objects = redisUtils.listRange(noticeKey,0,1);
        List<NoticeInfo> noticeInfos = new ArrayList<>();
        for (Object object : objects) {
            NoticeInfo noticeInfo = (NoticeInfo) object;
            noticeInfos.add(noticeInfo);
        }
        return StringUtils.isNotEmpty(noticeInfos)?noticeInfos.get(0):null;
    }

    private String getNoticeKey(Integer noticeType) {
        String noticeKey = "";
        if(NoticeType.ALARM.id.intValue() == noticeType.intValue()){
            noticeKey = CommonConstant.ALARM_NOTICE;
        }
        return noticeKey;
    }
}
