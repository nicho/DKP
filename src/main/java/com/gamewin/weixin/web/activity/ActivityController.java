/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import com.gamewin.weixin.entity.ValueSet;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ActivityService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.gamewin.weixin.util.MobileContants;
import com.gamewin.weixin.web.util.QRCodeUtil;
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
	

	@Autowired
	private ValueSetService valueSetService;
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

		
		List<ValueSet> ActivityTypeList=valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activity/activityList";
	}
	//我发起的活动
		@RequestMapping(value = "myfqActivity", method = RequestMethod.GET)
		public String myfqActivity(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
				@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
				@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
				ServletRequest request) {
			Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
			Long userId = getCurrentUserId();

			Page<Activity> activitys = activityService.getAllmyfqActivity(userId, searchParams, pageNumber, pageSize, sortType);

			model.addAttribute("activitys", activitys);
			model.addAttribute("sortType", sortType);
			model.addAttribute("sortTypes", sortTypes);
			// 将搜索条件编码成字符串，用于排序，分页的URL
			model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

			
			List<ValueSet> ActivityTypeList=valueSetService.getActivityTypeAll("ActivityType");
			model.addAttribute("ActivityTypeList", ActivityTypeList);
			return "activity/activityList";
		}
	//审批用户
	@RequestMapping(value = "approvalList", method = RequestMethod.GET)
	public String approvalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Activity> activitys = activityService.getAllProcessActivty(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("activitys", activitys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		
		List<ValueSet> ActivityTypeList=valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activity/approvalActivityList";
	}
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("activity", new Activity());
		model.addAttribute("action", "create");
		List<ValueSet> ActivityTypeList=valueSetService.getActivityType("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activity/activityForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Activity newActivity, RedirectAttributes redirectAttributes, ServletRequest request) {
		User user = new User(getCurrentUserId());
		String startDateStr = request.getParameter("startDateStr");
		String endDateStr = request.getParameter("endDateStr");
		try { 
			newActivity.setStartDate(DateUtils.parseDate(startDateStr, "yyyy年MM月dd日 HH时mm分ss秒"));
			newActivity.setEndDate(DateUtils.parseDate(endDateStr, "yyyy年MM月dd日 HH时mm分ss秒"));
			newActivity.setCreateDate(new Date());
			newActivity.setCreateUser(user);
			newActivity.setIsdelete(0);
			newActivity.setStatus("process");
			activityService.saveActivity(newActivity);
			redirectAttributes.addFlashAttribute("message", "创建活动成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "创建活动失败");
		}
		
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
	
	
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") Long id,Model model,ServletRequest request) {
		
		Activity activity=activityService.getActivity(id);
		model.addAttribute("activity", activity); 
		List<ValueSet> ActivityTypeList=valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		
		  
		if("Y".equals(activity.getStatus()))
		{  
			
			String filePath = MobileContants.IMAGEURL+"\\" ;
			if(!StringUtils.isEmpty(activity.getQrCodeUrl()))
			{
				File file=new File(filePath+activity.getQrCodeUrl());    
				if(!file.exists())
				{
					//原文件不存在,重新生成
					createQrCode(activity, request, filePath);
				}
			}else
			{ 
				createQrCode(activity, request, filePath);
			}
			
			
		} 
		model.addAttribute("HttpImageUrl",MobileContants.HTTPIMAGEURL); 
		  
		return "activity/activityView";
	}
	
	
	private void createQrCode(Activity entity,ServletRequest request,String filePath)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowDate=sdf.format(new Date());
		
		String imageUrl = entity.getTitle()  + "-" + entity.getId() + ".jpg";
		String url =  MobileContants.YM+"/activity/join?activityId="+entity.getId(); // 
		
		File file =new File(filePath+nowDate);    
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		} 
		 
		try {  
				QRCodeUtil.createEncode(url, null, filePath+nowDate, imageUrl);
		 
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		entity.setQrCodeUrl(nowDate+"\\"+imageUrl);
		entity.setWebUrl(url);
		activityService.saveActivity(entity);
	}

	
	@RequestMapping(value = "approval/{id}")
	public String approval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, ServletRequest request) {
		Activity activity=activityService.getActivity(id);
		String status=request.getParameter("status");
		User user = new User(getCurrentUserId());
		if(("pass".equals(status) || "reject".equals(status)) && "process".equals(activity.getStatus()))
		{
			activity.setApproveUser(user);
			activity.setStatus(status);
			activityService.saveActivity(activity);
			redirectAttributes.addFlashAttribute("message", "审批成功");
		}
		else
		{
			redirectAttributes.addFlashAttribute("message", "非法操作");
		}

		return "redirect:/exchangeIntegralApply/approvalList";
	}
}
