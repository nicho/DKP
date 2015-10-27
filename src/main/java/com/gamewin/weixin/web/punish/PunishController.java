/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.punish;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamewin.weixin.entity.Punish;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.entity.ValueSet;
import com.gamewin.weixin.model.UserDto;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.punish.PunishService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/punish")
public class PunishController {
	@Autowired
	ValueSetService valueSetService;
	
	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private PunishService punishService;

	@Autowired
	private AccountService accountService;

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Punish> punishs = punishService.getAllPunish(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("punishs", punishs);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "punish/punishList";
	}

	@RequestMapping(value = "mylist", method = RequestMethod.GET)
	public String mylist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Punish> punishs = punishService.getAllPunishMy(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("punishs", punishs);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "punish/myPunishList";
	}

	@RequestMapping(value = "alllist", method = RequestMethod.GET)
	public String alllist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Punish> punishs = punishService.getAllPunish(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("punishs", punishs);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		
		List<ValueSet> PenaltyItemList = valueSetService.getActivityType("PenaltyItem");
		model.addAttribute("PenaltyItemList", PenaltyItemList);
		
		return "punish/myPunishList";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("punish", new Punish());
		model.addAttribute("action", "create");

	
		
		List<ValueSet> ActivityTypeList = valueSetService.getActivityType("PenaltyItem");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "punish/punishForm";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Punish newPunish, RedirectAttributes redirectAttributes, ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {

			String userId = request.getParameter("userId");
			if (StringUtils.isEmpty(userId)) {
				String userName = request.getParameter("userName");
				if(!StringUtils.isEmpty(userName))
				{
					User user=accountService.findByGameName(userName);
					if(user!=null)
						userId=user.getId()+"";
				}

			}
			if (!StringUtils.isEmpty(userId)) {
				User user = accountService.getUser(Long.parseLong(request.getParameter("userId")));
				// 判断用户积分
				if (user.getIntegral() >= newPunish.getIntegral()) {
					newPunish.setCreateUser(createuser);
					newPunish.setCreateDate(new Date());
					newPunish.setUser(user);
					newPunish.setIsdelete(0);
					newPunish.setStatus("Y");
					ValueSet punishItem=new ValueSet();
					punishItem.setId(Long.parseLong(request.getParameter("punishItemId")));
					newPunish.setPunishItem(punishItem);
				 
					punishService.savePunishToo(newPunish, user);

					redirectAttributes.addFlashAttribute("message", "惩罚处理成功");
				} else {
					redirectAttributes.addFlashAttribute("message", "会员积分不够");
				}

			}else
			{
				redirectAttributes.addFlashAttribute("message", "惩罚处理失败");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "惩罚处理失败");
		}

		return "redirect:/punish/";
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "disabled/{id}")
	public String disabled(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Punish punish = punishService.getPunish(id);
		punish.setStatus("N");
		punishService.savePunish(punish);
		redirectAttributes.addFlashAttribute("message", "失效惩罚'" + punish.getPunishName() + "'成功");
		return "redirect:/punish/";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Punish punish = punishService.getPunish(id);
		punish.setIsdelete(1);
		punishService.savePunish(punish);
		redirectAttributes.addFlashAttribute("message", "删除惩罚'" + punish.getPunishName() + "'成功");
		return "redirect:/punish/";
	}
	@RequestMapping(value = "findPunishUser")
	@ResponseBody
	public String findPunishUser(@RequestParam("query") String query) { 
		List<UserDto> list=accountService.getAllUserDtoByName(query);
		ObjectMapper  objectMapper = new ObjectMapper();
		String jsonString="";
		try {
			jsonString = objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) { 
			e.printStackTrace();
		}
		 return jsonString;
	}
	
	@RequestMapping(value = "getPunishUser")
	@ResponseBody
	public String getPunishUser(@RequestParam("query") String query) { 
		User user=accountService.findByGameName(query);
		ObjectMapper  objectMapper = new ObjectMapper();
		String jsonString="";
		try {
			jsonString = objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) { 
			e.printStackTrace();
		}
		 return jsonString;
	}
	
	@RequestMapping(value = "checkPunishUserName")
	@ResponseBody
	public String checkPunishUserName(@RequestParam("userName") String userName) {
		User user=accountService.findByGameName(userName);
		if (user != null) {
			return "true";
		} else {
			return "false";
		}
	}
}
