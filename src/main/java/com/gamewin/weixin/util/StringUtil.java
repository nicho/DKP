package com.gamewin.weixin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
    
	public static String dateToString(Date date, String dateFormat) {
		if (StringUtils.isEmpty(dateFormat)) {
			dateFormat = "yyyy-MM-dd";
		}
		String str = null;
		DateFormat format = new SimpleDateFormat(dateFormat);
		str = format.format(date);
		return str;
	}
}
