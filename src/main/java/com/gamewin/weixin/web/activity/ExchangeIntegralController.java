/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.activity;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.ExchangeIntegral;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ExchangeIntegralService;
import com.google.common.collect.Maps;

/**
 * ExchangeIntegral管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /exchangeIntegral/ Create page : GET /exchangeIntegral/create Create action :
 * POST /exchangeIntegral/create Update page : GET /exchangeIntegral/update/{id} Update action :
 * POST /exchangeIntegral/update Delete action : GET /exchangeIntegral/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/exchangeIntegral")
public class ExchangeIntegralController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private ExchangeIntegralService exchangeIntegralService;

	
 
	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<ExchangeIntegral> exchangeIntegrals = exchangeIntegralService.getAllExchangeIntegral(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("exchangeIntegrals", exchangeIntegrals);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		 
		return "exchangeIntegral/exchangeIntegralList";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("exchangeIntegral", new ExchangeIntegral());
		model.addAttribute("action", "create"); 
		return "exchangeIntegral/exchangeIntegralForm";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid ExchangeIntegral newExchangeIntegral, RedirectAttributes redirectAttributes, ServletRequest request) {
		 User user = new User(getCurrentUserId()); 
		try {  
			newExchangeIntegral.setCteateUser(user);
			newExchangeIntegral.setCteateDate(new Date()); 
			newExchangeIntegral.setIsdelete(0);
			newExchangeIntegral.setStatus("Y");
			exchangeIntegralService.saveExchangeIntegral(newExchangeIntegral);
			redirectAttributes.addFlashAttribute("message", "拍卖处理成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "拍卖处理失败");
		}
		
		return "redirect:/exchangeIntegral/";
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "disabled/{id}")
	public String disabled(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ExchangeIntegral exchangeIntegral = exchangeIntegralService.getExchangeIntegral(id);
		exchangeIntegral.setStatus("N");
		exchangeIntegralService.saveExchangeIntegral(exchangeIntegral);
		redirectAttributes.addFlashAttribute("message", "失效兑换物品'" + exchangeIntegral.getGoodsName() + "'成功");
		return "redirect:/exchangeIntegral/";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ExchangeIntegral exchangeIntegral = exchangeIntegralService.getExchangeIntegral(id);
		exchangeIntegral.setIsdelete(1);
		exchangeIntegralService.saveExchangeIntegral(exchangeIntegral);
		redirectAttributes.addFlashAttribute("message", "删除兑换物品'" + exchangeIntegral.getGoodsName() + "'成功");
		return "redirect:/exchangeIntegral/";
	}
	
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("exchangeIntegral", exchangeIntegralService.getExchangeIntegral(id)); 
		model.addAttribute("action", "update"); 
		return "exchangeIntegral/exchangeIntegralForm"; 
	}

	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "update", method = RequestMethod.POST) 
	public String update(@Valid @ModelAttribute("newExchangeIntegral") ExchangeIntegral newExchangeIntegral, RedirectAttributes redirectAttributes, ServletRequest request) { 
		 User user = new User(getCurrentUserId()); 
		newExchangeIntegral.setCteateUser(user);
		newExchangeIntegral.setCteateDate(new Date()); 
		newExchangeIntegral.setIsdelete(0);
		newExchangeIntegral.setStatus("Y");
		exchangeIntegralService.saveExchangeIntegral(newExchangeIntegral);
		redirectAttributes.addFlashAttribute("message", "更新兑换物品成功");
		return "redirect:/exchangeIntegral/";
	}


}
