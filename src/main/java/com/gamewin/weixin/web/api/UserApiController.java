/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.api;

import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.web.util.UserData;

@Controller
@RequestMapping(value = "/adminAddUserApi")
public class UserApiController { 
	@Autowired
	private AccountService accountService;
	
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public void list(HttpServletRequest request, HttpServletResponse response) {
		//生成名字
		for (int i = 0; i < 4000; i++) {
			String name="离丶"; 
			
			Random random=new Random(); 
			int result=random.nextInt(4);//  
			for (int j = 0; j < result+2; j++) {
				 name+=UserData.create(); 
			}
			
			String userLoginName="FalseUser";
			System.out.println(name);
			User user=new User();
			user.setName(name);
			user.setLoginName(userLoginName+"_"+UUID.randomUUID());
			user.setPlainPassword("000000"); 
			user.setGameName(name); 
			user.setIsdelete(0);
			user.setIntegral(0.0);
			user.setStatus("enabled");
			user.setIsFalse("Y");
			accountService.registerUser(user);
		}
				
	}
}
