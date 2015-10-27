/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.account;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.OrgService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.util.ReadProperties;
 
@Controller
@RequestMapping(value = "/index")
public class IndexController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrgService orgService;
	@RequestMapping(value = "/my",method = RequestMethod.GET)
	public String myindex(Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		User u=accountService.getUser(user.getId()); 
		user.setIntegral(u.getIntegral());
		String orgId = ReadProperties.getDomainMap().get("orgId");
		String orgNotice=orgService.getMyOrgNotice(new Long(orgId));
		model.addAttribute("orgNotice", orgNotice);
		return "account/myindex";
	} 

}
