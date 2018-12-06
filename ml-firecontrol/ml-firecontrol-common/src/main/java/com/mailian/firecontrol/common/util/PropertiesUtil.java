package com.mailian.firecontrol.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static Properties props = new Properties();
	static {
		InputStream is = PropertiesUtil.class.getResourceAsStream("/conf.properties");
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static String getValue(String key, String defaultValue) {
		return props.getProperty(key) == null ? defaultValue : props
				.getProperty(key);
	}
}
