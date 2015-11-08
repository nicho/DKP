/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.account;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
import com.gamewin.weixin.entity.Org;
import com.gamewin.weixin.service.account.OrgService;
import com.gamewin.weixin.util.ReadProperties;
 
@Controller
@RequestMapping(value = "/guild")
public class GuildController {
	
	@Autowired
	private OrgService orgService;
	
	@RequestMapping(value = "/guildInformation",method = RequestMethod.GET)
	public String guildInformation(Model model) {
		String orgId = ReadProperties.getDomainMap().get("orgId");
		Org org=orgService.getOrg(new Long(orgId));
		model.addAttribute("org", org);
		return "guild/guildInformation";
	} 
	
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "updateGuildInformation", method = RequestMethod.POST) 
	public String update(@Valid @ModelAttribute("newOrg") Org org, RedirectAttributes redirectAttributes, ServletRequest request) {  
		orgService.updateOrgNotice(org);
		redirectAttributes.addFlashAttribute("message", "更新公会信息成功");
		return "redirect:/guild/guildInformation";
	}
}
