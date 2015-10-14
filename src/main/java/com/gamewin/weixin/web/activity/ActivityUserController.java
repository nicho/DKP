/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.activity;

import java.util.List;
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

import com.gamewin.weixin.entity.ActivityUser;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.entity.ValueSet;
import com.gamewin.weixin.model.ActivityUserList;
import com.gamewin.weixin.model.QueryUserDto;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ActivityUserService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

/**
 * ActivityUser管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /activityUser/ Create page : GET /activityUser/create Create
 * action : POST /activityUser/create Update page : GET
 * /activityUser/update/{id} Update action : POST /activityUser/update Delete
 * action : GET /activityUser/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/activityUser")
public class ActivityUserController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private ActivityUserService activityUserService;

	@Autowired
	private ValueSetService valueSetService;

	@RequestMapping(value = "list/{id}", method = RequestMethod.GET)
	public String list(@PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<ActivityUser> activityUsers = activityUserService.getAllActivityUser(id, searchParams, pageNumber,
				pageSize, sortType);

		model.addAttribute("activityUsers", activityUsers);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "activityUser/activityUserList";
	}

	@RequestMapping(value = "myActivity", method = RequestMethod.GET)
	public String myActivity(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		Page<ActivityUser> activityUsers = activityUserService.getMyActivityUser(userId, searchParams, pageNumber,
				pageSize, sortType);

		model.addAttribute("activityUsers", activityUsers);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		List<ValueSet> ActivityTypeList=valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activityUser/myActivityUserList";
	}
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	
	/**
	 * 用户参与活动排行
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param model
	 * @param request
	 * @return
	 */ 
	@RequestMapping(value = "activityUserRankingList",method = RequestMethod.GET)
	public String activityUserRankingList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request,QueryUserDto dto) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		List<ActivityUserList> users = activityUserService.getUserAllActivityUserlist(searchParams, pageNumber, pageSize, sortType,dto);

		PageInfo<ActivityUserList> page = new PageInfo<ActivityUserList>(users);
		model.addAttribute("page", page);
		model.addAttribute("usersx", users);

		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes); 
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "activityUser/activityUserRankingList";
	}
}
