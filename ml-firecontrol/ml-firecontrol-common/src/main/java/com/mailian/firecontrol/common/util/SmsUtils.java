package com.mailian.firecontrol.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunma.my.YunmaClient;
import com.yunma.sms.entity.SmsSendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SmsUtils {
	private static final Logger log = LoggerFactory.getLogger(SmsUtils.class);

	private static final String APPKEY = PropertiesUtil.getValue("yunma.appkey");
	private static final String SECRETKEY = PropertiesUtil.getValue("yunma.secretkey");
	private static final String SMSTEMPLATEID = PropertiesUtil.getValue("yunma.smstemplateid");


	public static int sendSms(String phone, String did, String content,
			String date, String name) {
		String mstr = String.valueOf(System.currentTimeMillis());
		YunmaClient client = new YunmaClient(APPKEY, SECRETKEY, mstr);
		SmsSendRequest req = new SmsSendRequest();
		
		req.setRecNo(phone);
		req.setSendtimer("");// 不填代表立即发送 ，例：201604251102
		// req.setSmsParam("{"name":"云码APP"}");
		Map<String, String> hmap = new HashMap<String, String>();
		hmap.put("did", did);
		String cont = "";
		String cont1 = "";
		content = (name==null?"":name) + content;
		if(content.length()>20)
		{
			cont = content.substring(0,20);
			cont1 = content.substring(20);
		}else{
			cont = content;
		}
		hmap.put("content", cont);
		hmap.put("content1", cont1);
		hmap.put("date", date);
		// hmap.put("name", "脉联云服务");
		String param = JSON.toJSONString(hmap);
		req.setSmsParam(param);
		// {"code":"0988","product":"九五云码"}
		req.setSmsSign("脉联云服务");
		req.setSmsTemplateId(SMSTEMPLATEID);
		String smsContent = "发送短信:" + phone + param;
		log.info(smsContent);
		String resp = client.execute(req);
		String smsResult = "发送短信结果:"+ resp;
		log.info(smsResult);
		
		String result = JSONObject.parseObject(resp).getString("result");
		if(!"0".equals(result))
		{
			log.warn(smsContent+ "\r\n" + smsResult);
		}
		return Integer.valueOf(result);

	}

}
