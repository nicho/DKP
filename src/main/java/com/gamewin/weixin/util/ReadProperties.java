package com.gamewin.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

 
public final class ReadProperties { 
	private static Logger logger = Logger.getLogger(ReadProperties.class);
    private static Map<String,String> domainMap=new HashMap<String,String>();  

    static { 
        Properties prop = new Properties(); 
        InputStream in = ReadProperties.class.getResourceAsStream("/domain.properties"); 
        try { 
            prop.load(in); 
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next(); 
                domainMap.put(key, prop.getProperty(key));
            }
            in.close();
        } catch (IOException e) { 
            logger.error(e); 
        } 
        
    } 

    /** 
     * 私有构造方法，不需要创建对象 
     */ 
    private ReadProperties() { 
    }

	public static Map<String, String> getDomainMap() {
		return domainMap;
	} 

 
  
}