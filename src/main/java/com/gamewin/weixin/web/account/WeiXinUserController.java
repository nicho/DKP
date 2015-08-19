/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.util.MobileHttpClient;

@Controller
@RequestMapping(value = "/weixinUser")
public class WeiXinUserController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "bindUserOpenId", method = { RequestMethod.GET, RequestMethod.POST })
	public String bindUserOpenId(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws Exception {
		String code = request.getParameter("code");
		String grant_type = request.getParameter("grant_type");
		
		model.addAttribute("grant_type", grant_type);
		
		String openId = MobileHttpClient.getUserOpenIdByCode(code);
		model.addAttribute("openId", openId);
		System.out.println(" openId:"+openId);
		User user=accountService.findByWeixinOpenid(openId);
		if(user!=null)
		{
			Subject subject = SecurityUtils.getSubject(); 
			try {
				subject.login(new UsernamePasswordToken(user.getLoginName(), user.getWeixinOpenPwd(), true));
				if (subject.isAuthenticated()) { 
					redirectAttributes.addFlashAttribute("message", "登录成功");
					return "redirect:/index/my";
				}else
				{
					
					model.addAttribute("username",user.getLoginName());
					model.addAttribute("message", "密码错误!请重新输入密码绑定!");
				}
			} catch (Exception e) {
				model.addAttribute("username",user.getLoginName());
				model.addAttribute("message", "密码错误!请重新输入密码绑定!");
			} 
			
		} 
		return "weiXinUser/bindUserOpenIdFrom";
		
	}

	@RequestMapping(value = "updateBindUserOpenId", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateBindUserOpenId(HttpServletRequest request, HttpServletResponse response, Model model) {
		String openId = request.getParameter("openId");
		// String grant_type = request.getParameter("grant_type");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(username, password, false));
			if (subject.isAuthenticated()) {
				ShiroUser user = (ShiroUser) subject.getPrincipal();
				User usr = accountService.getUser(user.id);
				if (StringUtils.isEmpty(usr.getWeixinOpenid()) || !password.equals(usr.getWeixinOpenPwd())) {
					 
						if (!StringUtils.isEmpty(openId)) { 
							usr.setWeixinOpenid(openId);
							usr.setWeixinOpenPwd(password);
							accountService.updateUser(usr);
							model.addAttribute("message", "绑定成功!");
							return "redirect:/index/my";
						} else {
							model.addAttribute("message", "获取微信用户失效,请重新绑定");
						}

				 
				}else
				{
					model.addAttribute("message", "用户已绑定过.无需重复绑定!");
				}
			} else {
				model.addAttribute("message", "用户名/密码错误");
			}
		} catch (Exception e) {
			model.addAttribute("message", "绑定异常,请重新绑定");
		}
		return "redirect:/login";
	}
	
	
	
	@RequestMapping(value = "wxbindUserOpenId", method = { RequestMethod.GET, RequestMethod.POST })
	public String wxbindUserOpenId(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) throws Exception {
		String code = request.getParameter("code");
		String grant_type = request.getParameter("grant_type");
		
		model.addAttribute("grant_type", grant_type);
		
		String openId = MobileHttpClient.getUserOpenIdByCode(code);
		model.addAttribute("openId", openId);
		System.out.println(" openId:"+openId);
		User user=accountService.findByWxWeixinOpenid(openId);
		if(user!=null)
		{
			Subject subject = SecurityUtils.getSubject(); 
			try {
				subject.login(new UsernamePasswordToken(user.getLoginName(), user.getWeixinOpenPwd(), true));
				if (subject.isAuthenticated()) { 
					redirectAttributes.addFlashAttribute("message", "登录成功");
					return "redirect:/index/my";
				}else
				{
					
					model.addAttribute("username",user.getLoginName());
					model.addAttribute("message", "密码错误!请重新输入密码绑定!");
				}
			} catch (Exception e) {
				model.addAttribute("username",user.getLoginName());
				model.addAttribute("message", "密码错误!请重新输入密码绑定!");
			} 
			
		} 
		return "weiXinUser/wxbindUserOpenIdFrom";
		
	}

	@RequestMapping(value = "wxupdateBindUserOpenId", method = { RequestMethod.GET, RequestMethod.POST })
	public String wxupdateBindUserOpenId(HttpServletRequest request, HttpServletResponse response, Model model) {
		String openId = request.getParameter("openId");
		// String grant_type = request.getParameter("grant_type");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(username, password, false));
			if (subject.isAuthenticated()) {
				ShiroUser user = (ShiroUser) subject.getPrincipal();
				User usr = accountService.getUser(user.id);
				if (StringUtils.isEmpty(usr.getWxWeixinOpenid())) {
					 
						if (!StringUtils.isEmpty(openId)) { 
							usr.setWxWeixinOpenid(openId); ;
							accountService.updateUser(usr);
							model.addAttribute("message", "绑定DKP成功!");
							return "redirect:/index/my";
						} else {
							model.addAttribute("message", "获取微信用户失效,请重新绑定");
						}

				 
				}else
				{
					model.addAttribute("message", "用户已绑定过.无需重复绑定!");
				}
			} else {
				model.addAttribute("message", "用户名/密码错误");
			}
		} catch (Exception e) {
			model.addAttribute("message", "绑定异常,请重新绑定");
		}
		return "redirect:/login";
	}
}
