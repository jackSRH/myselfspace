package com.mailian.core.push;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/27
 * @Description:
 */
@Repository
public class UmengPushRepository {
    @Value("${manager.umengpush.appkey}")
    private String MANAGER_APP_KEY;
    @Value("${manager.umengpush.appMasterSecret}")
    private String MANAGER_APP_MASTERSECRET;

    @Value("${user.umengpush.appkey}")
    private String USER_APP_KEY;
    @Value("${user.umengpush.appMasterSecret}")
    private String USER_APP_MASTERSECRET;
    private static final String ALIAS_TYPE = "uniqueKey";

    public void sendManagerAndroidCustomizedcast(String uniqueKey,String title,Integer builderId,String text) throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(MANAGER_APP_KEY,MANAGER_APP_MASTERSECRET);
        customizedcast.setAlias(uniqueKey, ALIAS_TYPE);
        customizedcast.setTicker( "Android customizedcast ticker");
        customizedcast.setTitle(title);
        customizedcast.setBuilderId(builderId+10);
        customizedcast.setText(text);
        customizedcast.goAppAfterOpen();
        customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        customizedcast.setProductionMode();

        PushClient client = new PushClient();
        client.send(customizedcast);
    }

    public void sendUserAndroidCustomizedcast(String uniqueKey,String title,Integer builderId,String text) throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(USER_APP_KEY,USER_APP_MASTERSECRET);
        customizedcast.setAlias(uniqueKey, ALIAS_TYPE);
        customizedcast.setTicker( "Android customizedcast ticker");
        customizedcast.setTitle(title);
        customizedcast.setBuilderId(builderId+10);
        customizedcast.setText(text);
        customizedcast.goAppAfterOpen();
        customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        customizedcast.setProductionMode();

        PushClient client = new PushClient();
        client.send(customizedcast);
    }
}
