package com.mailian.core.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	
	
	public static Integer toInteger(Object obj,Integer defaultValue){
		try {
			return Integer.parseInt(getNumbers(StringUtil.valueOf(obj)));
		} catch (Exception e) {
               return defaultValue;
		}
	}
	
	public static String getNumbers(String content) {  
	       Pattern pattern = Pattern.compile("\\d+");  
	       Matcher matcher = pattern.matcher(content);  
	       while (matcher.find()) {  
	           return matcher.group(0);  
	       }  
	       return "";  
	}  
	
	public static Double toDouble(Object obj,Double defaultValue){
		try {
			return Double.parseDouble(StringUtil.valueOf(obj));
		} catch (Exception e) {
               return defaultValue;
		}
	}

	/**
	 * obj 鏄笉鏄暟瀛楃被鍨�鎴栬�鍙互杞垚number绫诲瀷
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNumber(Object obj) {
		if (null == obj)
			return false;
		String numStr = obj + "";
		if (numStr.trim().length() < 1)
			return false;
		numStr = numStr.replaceAll("\\d", "");
		if (numStr.equals(".") || numStr.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static BigDecimal bigDecimalAdd(BigDecimal... ages) {
		BigDecimal result = new BigDecimal(0);
		if (ages == null || ages.length < 1) {
			return result;
		}
		for (BigDecimal b : ages) {
			if (b != null) {
				result=result.add(b);
			}
		}
		return result;
	}
	
	public static BigDecimal createBigDecimal(String value,BigDecimal defualtBigDecimal){
		 if(StringUtil.isEmpty(value)){
			 return defualtBigDecimal;
		 }
		 return new BigDecimal(value);
	}
	
	/**
	 * 
	 */
	private static int numberAddValue=1;
	public synchronized static String createNumber(){
		long l=System.currentTimeMillis();
		l+=numberAddValue++;//闃叉寰幆鍒涘缓鍚屾牱鐨�
		if(numberAddValue>=Integer.MAX_VALUE)numberAddValue=0;
		final String number=String.valueOf(l);
		return number;
	}
	
	public static Double reserve(Double value,int digit){
		if(null==value){
			return (double) 0;
		}
		return Double.parseDouble(String.format("%."+digit+"f", value));
	}
	
	public static String reserveString(Double value,int digit){
		if(null==value){
			return "0";
		}
		return String.format("%."+digit+"f", value);
	}
	
	
}
