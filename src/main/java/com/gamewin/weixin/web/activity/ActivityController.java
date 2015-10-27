/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.activity;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.Activity;
import com.gamewin.weixin.entity.ActivityUser;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.entity.ValueSet;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ActivityService;
import com.gamewin.weixin.service.activity.ActivityUserService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.gamewin.weixin.util.MobileContants;
import com.gamewin.weixin.util.MobileHttpClient;
import com.gamewin.weixin.util.ReadProperties;
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
	private ActivityUserService activityUserService;
	@Autowired
	private ValueSetService valueSetService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Activity> activitys = activityService
				.getPassActivity(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("activitys", activitys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activity/activityList";
	}

	// 我发起的活动
	@RequestMapping(value = "myfqActivity", method = RequestMethod.GET)
	public String myfqActivity(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Activity> activitys = activityService.getAllmyfqActivity(userId, searchParams, pageNumber, pageSize,
				sortType);

		model.addAttribute("activitys", activitys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activity/myactivityList";
	}

	// 活动申请审核页面加载
	@RequestMapping(value = "approvalList", method = RequestMethod.GET)
	public String approvalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request, RedirectAttributes redirectAttributes) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		try {
			Page<Activity> activitys = activityService.getAllProcessActivty(userId, searchParams, pageNumber, pageSize,
					sortType);

			model.addAttribute("activitys", activitys);
			model.addAttribute("sortType", sortType);
			model.addAttribute("sortTypes", sortTypes);
			// 将搜索条件编码成字符串，用于排序，分页的URL
			model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

			List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
			model.addAttribute("ActivityTypeList", ActivityTypeList);
			return "activity/approvalActivityList";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/activity/activityList";
		}
	}

	// 审批确认活动
	@RequestMapping(value = "approvalConfirmList", method = RequestMethod.GET)
	public String approvalConfirmList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request, RedirectAttributes redirectAttributes) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		try {
			Page<Activity> activitys = activityService.getAllConfirmProcessActivty(userId, searchParams, pageNumber,
					pageSize, sortType);

			model.addAttribute("activitys", activitys);
			model.addAttribute("sortType", sortType);
			model.addAttribute("sortTypes", sortTypes);
			// 将搜索条件编码成字符串，用于排序，分页的URL
			model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

			List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
			model.addAttribute("ActivityTypeList", ActivityTypeList);
			return "activity/approvalConfirmActivityList";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/activity/activityList";
		}
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("activity", new Activity());
		model.addAttribute("action", "create");
		List<ValueSet> ActivityTypeList = valueSetService.getActivityType("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		return "activity/activityForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Activity newActivity, RedirectAttributes redirectAttributes, ServletRequest request) {
		User user = new User(getCurrentUserId());
		String startDateStr = request.getParameter("startDateStr");
		String endDateStr = request.getParameter("endDateStr");
		try {
			newActivity.setStartDate(DateUtils.parseDate(startDateStr, "yyyy-MM-dd HH:mm:ss"));
			newActivity.setEndDate(DateUtils.parseDate(endDateStr, "yyyy-MM-dd HH:mm:ss"));
			newActivity.setCreateDate(new Date());
			newActivity.setCreateUser(user);
			newActivity.setIsdelete(0);
			if ("AssociationActivity".equals(newActivity.getfType())) {
				newActivity.setStatus("process");
				activityService.saveActivityGHCreate(newActivity);
				redirectAttributes.addFlashAttribute("message", "活动确认提交审核成功");
			} else if ("PersonalActivities".equals(newActivity.getfType())) {
				newActivity.setStatus("pass");
				activityService.saveActivityGRCreate(newActivity);
				redirectAttributes.addFlashAttribute("message", "创建个人活动成功,活动积分已扣除.");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "创建活动失败" + e.getMessage());
		}

		return "redirect:/activity/myfqActivity";
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
		Activity activity = activityService.getActivity(id);
		activity.setStatus("disabled");
		activityService.saveActivity(activity);
		redirectAttributes.addFlashAttribute("message", "失效任务'" + activity.getTitle() + "'成功");
		return "redirect:/activity/approvalConfirmList/";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Activity activity = activityService.getActivity(id);
		activity.setStatus("disabled");
		activity.setIsdelete(1);
		activityService.saveActivity(activity);
		redirectAttributes.addFlashAttribute("message", "删除任务'" + activity.getTitle() + "'成功");
		return "redirect:/activity/approvalConfirmList/";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "deleteindex/{id}")
	public String deleteindex(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Activity activity = activityService.getActivity(id);
		activity.setStatus("disabled");
		activity.setIsdelete(1);
		activityService.saveActivity(activity);
		redirectAttributes.addFlashAttribute("message", "删除任务'" + activity.getTitle() + "'成功");
		return "redirect:/activity/";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") Long id, Model model, ServletRequest request) {

		Activity activity = activityService.getActivity(id);
		model.addAttribute("activity", activity);
		List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);

		if ("pass".equals(activity.getStatus())) {
			String IMAGEURL = ReadProperties.getDomainMap().get("IMAGEURL");
			String filePath = IMAGEURL + "\\";
			if (!StringUtils.isEmpty(activity.getQrCodeUrl())) {
				File file = new File(filePath + activity.getQrCodeUrl());
				if (!file.exists()) {
					// 原文件不存在,重新生成
					createQrCode(activity, request, filePath);
				}
			} else {
				createQrCode(activity, request, filePath);
			}

		}
		String HTTPIMAGEURL = ReadProperties.getDomainMap().get("HTTPIMAGEURL");
		model.addAttribute("HttpImageUrl", HTTPIMAGEURL);

		return "activity/activityView";
	}

	private void createQrCode(Activity entity, ServletRequest request, String filePath) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowDate = sdf.format(new Date());
		String YM = ReadProperties.getDomainMap().get("YM");
		String orgId = ReadProperties.getDomainMap().get("orgId");
		String imageUrl = entity.getActivityType() + "-" + entity.getId() + ".jpg";
		String url = YM + "/activity/registerActivity/"+ entity.getId(); //

		File file = new File(filePath + nowDate);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		String IMAGEURL = ReadProperties.getDomainMap().get("IMAGEURL");
		String wxurl = IMAGEURL + nowDate + "\\" + imageUrl; //

		// String AccessToken = activityService.getAccessToken();

		try {
			String AccessToken = MobileHttpClient.getAccessToken();
			// QRCodeUtil.createEncode(url, null, filePath + nowDate, imageUrl);
			String ticket = MobileHttpClient.getJsapi_ticket_WeixinLs(AccessToken, new Long(orgId+entity.getId()));
			MobileHttpClient.getticketImage(URLEncoder.encode(ticket, "UTF-8"), wxurl);
		} catch (Exception e) {
			e.printStackTrace();
		}

		entity.setQrCodeUrl(nowDate + "\\" + imageUrl);
		entity.setWebUrl(url);
		activityService.saveActivity(entity);
	}

	@RequestMapping(value = "approval/{id}")
	public String approval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, ServletRequest request) {
		Activity activity = activityService.getActivity(id);
		String status = request.getParameter("status");
		User user = new User(getCurrentUserId());
		if (("pass".equals(status) || "reject".equals(status)) && "process".equals(activity.getStatus())) {
			activity.setApproveUser(user);
			activity.setStatus(status);
			activityService.saveActivity(activity);
			redirectAttributes.addFlashAttribute("message", "审批成功");
		} else {
			redirectAttributes.addFlashAttribute("message", "非法操作");
		}

		return "redirect:/activity/approvalList";
	}

	@RequestMapping(value = "approvalConfirm/{id}")
	public String approvalConfirm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Activity activity = activityService.getActivity(id);
		model.addAttribute("activity", activity);
		List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);

		List<ActivityUser> activityUsers = activityUserService.getAllActivityUser(id);
		model.addAttribute("activityUsers", activityUsers);

		if (!"ConfirmProcess".equals(activity.getStatus())) {
			redirectAttributes.addFlashAttribute("message", "操作失败,该活动已被确认审批,或非法操作!");
			return "redirect:/activity/approvalConfirmList/";
		}
		return "activity/approvalConfirm";
	}

	@RequestMapping(value = "registerActivity/{id}", method = RequestMethod.GET)
	public String registerActivity(@PathVariable("id") Long id, Model model, ServletRequest request) {

		Activity activity = activityService.getActivity(id);
		model.addAttribute("activity", activity);
		List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);
		if (new Date().getTime() > activity.getEndDate().getTime()) {
			model.addAttribute("isclose", "Y");
			if ("pass".equals(activity.getStatus())) {
				activityService.updateActivityClose(id);
			}

		} 
		return "activity/activityRegister";
	}

	@RequestMapping(value = "closeActivity/{id}", method = RequestMethod.GET)
	public String closeActivity(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Activity ay = activityService.getActivity(id);
		if ("pass".equals(ay.getStatus())) {
			activityService.updateActivityClose(id);
			redirectAttributes.addFlashAttribute("message", "活动结束登记成功");
			return "redirect:/activity/myfqActivity";
		} else {
			redirectAttributes.addFlashAttribute("message", "状态异常!非法操作!");
			return "redirect:/activity/myfqActivity";
		}

	}

	@RequestMapping(value = "registerActivity", method = RequestMethod.POST)
	public String registerActivity(@Valid Long activityId, RedirectAttributes redirectAttributes, ServletRequest request) {
		User user = new User(getCurrentUserId());
		Activity activity = activityService.getActivity(activityId);
		if (activity != null && activityUserService.findByActivityUser(getCurrentUserId(), activityId) == false) {
			ActivityUser au = new ActivityUser();
			au.setActivity(activity);
			au.setUser(user);
			au.setCreateDate(new Date());
			activityUserService.saveActivityUser(au);
			redirectAttributes.addFlashAttribute("message", "活动登记成功");
			return "redirect:/activity/view/" + activityId;

		} else {
			redirectAttributes.addFlashAttribute("message", "活动不存在,或您已经参与了活动.");
		}
		return "redirect:/activity/";
	}

	@RequestMapping(value = "confirmActivity/{id}", method = RequestMethod.GET)
	public String confirmActivity(@PathVariable("id") Long id, Model model, ServletRequest request,
			RedirectAttributes redirectAttributes) {

		Activity activity = activityService.getActivity(id);
		if (activity == null) {
			redirectAttributes.addFlashAttribute("message", "非法操作!活动不存在!");
			return "redirect:/activity/myfqActivity/";
		}
		model.addAttribute("activity", activity);
		List<ValueSet> ActivityTypeList = valueSetService.getActivityTypeAll("ActivityType");
		model.addAttribute("ActivityTypeList", ActivityTypeList);

		List<ActivityUser> activityUsers = activityUserService.getAllActivityUser(id);
		model.addAttribute("activityUsers", activityUsers);
		if (!"pass".equals(activity.getStatus()) && !"close".equals(activity.getStatus())) {
			redirectAttributes.addFlashAttribute("message", "操作失败,或活动已提交发放申请,或非法操作!");
			return "redirect:/activity/myfqActivity/";
		}

		return "activity/activityConfirm";
	}

	@RequestMapping(value = "activityConfirm", method = RequestMethod.POST)
	public String activityConfirm(@Valid Long activityId, @Valid Long[] chk_list,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		try {
			User user = new User(getCurrentUserId());
			Activity activity = activityService.getActivity(activityId);
			if (activity != null) {
				if (!"pass".equals(activity.getStatus()) && !"close".equals(activity.getStatus())) {
					redirectAttributes.addFlashAttribute("message", "操作失败,该活动已提交发放申请,或非法操作!");
					return "redirect:/activity/myfqActivity/";
				}
				if (chk_list != null && chk_list.length > 0) {
					if ("AssociationActivity".equals(activity.getfType())) {
						// 公会活动
						activity.setStatus("ConfirmProcess");
						activity.setUpdateDate(new Date());
						activityService.saveActivityConfirmProcess(activity, chk_list);

						redirectAttributes.addFlashAttribute("message", "活动确认提交审核成功");
					} else if ("PersonalActivities".equals(activity.getfType())) {
						activity.setStatus("ConfirmPass");
						activity.setUpdateDate(new Date());
						activity.setConfirmUser(user);
						activityService.saveActivityConfirmPass(activity, chk_list);

						redirectAttributes.addFlashAttribute("message", "活动确认成功");
					}
				} else {
					redirectAttributes.addFlashAttribute("message", "活动确认失败.没有选任何人");
				}

			} else {
				redirectAttributes.addFlashAttribute("message", "活动不存在");
			}
			return "redirect:/activity/myfqActivity";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "活动确认失败." + e.getMessage());
			return "redirect:/activity/myfqActivity";
		}
	}

	@RequestMapping(value = "approvalConfirm", method = RequestMethod.POST)
	public String approvalConfirm(@Valid Long activityId, RedirectAttributes redirectAttributes, ServletRequest request) {
		try {
			User user = new User(getCurrentUserId());
			Activity activity = activityService.getActivity(activityId);
			if (activity != null) {

				if (!"ConfirmProcess".equals(activity.getStatus())) {
					redirectAttributes.addFlashAttribute("message", "操作失败,该活动已被确认审批,或非法操作!");
					return "redirect:/activity/approvalConfirmList/";
				}
				if ("AssociationActivity".equals(activity.getfType())) {
					String approvalStatus = request.getParameter("approvalStatus");
					if (!StringUtils.isEmpty(approvalStatus)) {
						if ("Y".equals(approvalStatus)) {
							activity.setStatus("ConfirmPass");
							activity.setUpdateDate(new Date());
							activity.setConfirmUser(user);
							activityService.saveActivityApprovalConfirm(activity);
							redirectAttributes.addFlashAttribute("message", "活动确认审批通过,活动积分已进入用户账户.活动已结束");

						} else {
							activity.setStatus("ConfirmReject");
							activity.setUpdateDate(new Date());
							activity.setConfirmUser(user);
							activityService.saveActivity(activity);
							redirectAttributes.addFlashAttribute("message", "活动确认审批拒绝");
						}

					}
				} else {
					redirectAttributes.addFlashAttribute("message", "不是公会活动");
				}

			} else {
				redirectAttributes.addFlashAttribute("message", "活动不存在");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "活动确认失败" + e.getMessage());
		}
		return "redirect:/activity/approvalConfirmList";
	}

}
