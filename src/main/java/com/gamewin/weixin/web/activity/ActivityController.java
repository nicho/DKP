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

import com.gamewin.weixin.entity.Activity;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ActivityService;
import com.google.common.collect.Maps;

/**
 * Activity管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /activity/ Create page : GET /activity/create Create action :
 * POST /activity/create Update page : GET /activity/update/{id} Update action :
 * POST /activity/update Delete action : GET /activity/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private ActivityService activityService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Activity> activitys = activityService.getAllActivity(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("activitys", activitys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "activity/activityList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("activity", new Activity());
		model.addAttribute("action", "create");
		return "activity/activityForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Activity newActivity, RedirectAttributes redirectAttributes, ServletRequest request) {
		User user = new User(getCurrentUserId());

		newActivity.setCreateDate(new Date());
		newActivity.setCreateUser(user);
		newActivity.setIsdelete(0);
		newActivity.setStatus("N");
		activityService.saveActivity(newActivity);

		redirectAttributes.addFlashAttribute("message", "创建活动成功");
		return "redirect:/activity/";
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
		Activity activity = activityService.getActivity(id);
		activity.setStatus("disabled");
		activityService.saveActivity(activity);
		redirectAttributes.addFlashAttribute("message", "失效任务'" + activity.getTitle() + "'成功");
		return "redirect:/activity/";
	}

}
