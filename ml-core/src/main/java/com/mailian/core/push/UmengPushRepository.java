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
    @Value("${umengpush.appkey}")
    private String APP_KEY;
    @Value("${umengpush.appMasterSecret}")
    private String APP_MASTERSECRET;
    private static final String ALIAS_TYPE = "uniqueKey";

    public void sendAndroidCustomizedcast(String uniqueKey,String title,Integer builderId,String text) throws Exception {
        AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(APP_KEY,APP_MASTERSECRET);
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
