/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.gamewin.weixin.entity.User;

public class UserData {
	public static String create()   {
		String str = null;
		int hightPos, lowPos;// 定义高低位
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39)));// 获取高位值
		lowPos = (161 + Math.abs(random.nextInt(93)));// 获取低位值
		byte[] b = new byte[2];
		b[0] = (new Integer(hightPos).byteValue());
		b[1] = (new Integer(lowPos).byteValue());
		try {
			str = new String(b, "GBk");
		} catch (UnsupportedEncodingException e) {  
		}// 转成中文
		return str;
	}
	
	public static void main(String[] args) {
		
		//生成名字
		String name="离丶"+UserData.create()+UserData.create();

		System.out.println(name);
	}
}
