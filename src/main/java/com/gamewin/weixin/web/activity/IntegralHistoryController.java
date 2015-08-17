/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.activity;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.IntegralHistory;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.IntegralHistoryService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.google.common.collect.Maps;

/**
 * IntegralHistory管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /integralHistory/ Create page : GET /integralHistory/create
 * Create action : POST /integralHistory/create Update page : GET
 * /integralHistory/update/{id} Update action : POST /integralHistory/update
 * Delete action : GET /integralHistory/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/integralHistory")
public class IntegralHistoryController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private IntegralHistoryService integralHistoryService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ValueSetService valueSetService;

	@RequestMapping(value = "myIntegeral/{id}", method = RequestMethod.GET)
	public String mylist(@PathVariable("id") Long id,@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		 
		User u=accountService.getUser(id);
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.setIntegral(u.getIntegral());
		
		Page<IntegralHistory> integralHistorys = integralHistoryService.getAllIntegralHistory(id, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("integralHistorys", integralHistorys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "integralHistory/integralHistoryList";
	}

 

}
