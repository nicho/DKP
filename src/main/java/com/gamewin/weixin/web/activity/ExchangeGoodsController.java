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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.ExchangeGoods;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ExchangeGoodsService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.google.common.collect.Maps;

/**
 * ExchangeGoods管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /exchangeGoods/ Create page : GET /exchangeGoods/create Create action :
 * POST /exchangeGoods/create Update page : GET /exchangeGoods/update/{id} Update action :
 * POST /exchangeGoods/update Delete action : GET /exchangeGoods/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/exchangeGoods")
public class ExchangeGoodsController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private ExchangeGoodsService exchangeGoodsService;
	

	@Autowired
	private ValueSetService valueSetService;
	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<ExchangeGoods> exchangeGoodss = exchangeGoodsService.getAllExchangeGoods(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("exchangeGoodss", exchangeGoodss);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		 
		return "exchangeGoods/exchangeGoodsList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("exchangeGoods", new ExchangeGoods());
		model.addAttribute("action", "create"); 
		return "exchangeGoods/exchangeGoodsForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid ExchangeGoods newExchangeGoods, RedirectAttributes redirectAttributes, ServletRequest request) {
		 User createuser = new User(getCurrentUserId()); 
		 User user = new User(Long.parseLong(request.getParameter("userId"))); 
		try {  
			newExchangeGoods.setCteateUser(createuser);
			newExchangeGoods.setCteateDate(new Date()); 
			newExchangeGoods.setUser(user);
			newExchangeGoods.setIsdelete(0);
			newExchangeGoods.setStatus("Y");
			exchangeGoodsService.saveExchangeGoods(newExchangeGoods);
			redirectAttributes.addFlashAttribute("message", "拍卖处理成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "拍卖处理失败");
		}
		
		return "redirect:/exchangeGoods/";
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	@RequestMapping(value = "disabled/{id}")
	public String disabled(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ExchangeGoods exchangeGoods = exchangeGoodsService.getExchangeGoods(id);
		exchangeGoods.setStatus("N");
		exchangeGoodsService.saveExchangeGoods(exchangeGoods);
		redirectAttributes.addFlashAttribute("message", "失效拍卖'" + exchangeGoods.getGoodsName() + "'成功");
		return "redirect:/exchangeGoods/";
	}
	
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ExchangeGoods exchangeGoods = exchangeGoodsService.getExchangeGoods(id);
		exchangeGoods.setIsdelete(1);
		exchangeGoodsService.saveExchangeGoods(exchangeGoods);
		redirectAttributes.addFlashAttribute("message", "删除拍卖物品'" + exchangeGoods.getGoodsName() + "'成功");
		return "redirect:/exchangeIntegral/";
	}
	 

}
