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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.ExchangeIntegral;
import com.gamewin.weixin.entity.ExchangeIntegralApply;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.ExchangeIntegralApplyService;
import com.gamewin.weixin.service.activity.ExchangeIntegralService;
import com.gamewin.weixin.util.StringUtil;
import com.google.common.collect.Maps;

/**
 * ExchangeIntegralApply管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /exchangeIntegralApply/ Create page : GET
 * /exchangeIntegralApply/create Create action : POST
 * /exchangeIntegralApply/create Update page : GET
 * /exchangeIntegralApply/update/{id} Update action : POST
 * /exchangeIntegralApply/update Delete action : GET
 * /exchangeIntegralApply/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/exchangeIntegralApply")
public class ExchangeIntegralApplyController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private ExchangeIntegralApplyService exchangeIntegralApplyService;
	@Autowired
	private ExchangeIntegralService exchangeIntegralService;
 

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		// 我的申请
		Page<ExchangeIntegralApply> exchangeIntegralApplys = exchangeIntegralApplyService.getAllExchangeIntegralApply(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("exchangeIntegralApplys", exchangeIntegralApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "exchangeIntegralApply/myExchangeIntegralApplyList";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "approvalList",method = RequestMethod.GET)
	public String approvalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		// 我的申请
		Page<ExchangeIntegralApply> exchangeIntegralApplys = exchangeIntegralApplyService.getAllExchangeIntegralApprovalList(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("exchangeIntegralApplys", exchangeIntegralApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "exchangeIntegralApply/exchangeIntegralApplyList";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "approvalAllList",method = RequestMethod.GET)
	public String approvalAllList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
	 
		Page<ExchangeIntegralApply> exchangeIntegralApplys = exchangeIntegralApplyService.getAllExchangeIntegralAllList(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("exchangeIntegralApplys", exchangeIntegralApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "exchangeIntegralApply/exchangeIntegralList";
	}
	@RequestMapping(value = "create/{id}", method = RequestMethod.GET)
	public String createForm(@PathVariable("id") Long id,Model model, RedirectAttributes redirectAttributes) {
		User createuser = new User(getCurrentUserId());
		ExchangeIntegral exchangeIntegral=exchangeIntegralService.getExchangeIntegral(id);
		model.addAttribute("exchangeIntegralApply", new ExchangeIntegralApply());
		model.addAttribute("action", "create");
		model.addAttribute("exchangeIntegral", exchangeIntegral);
		if(exchangeIntegral.getLimitedNumber()!=null && exchangeIntegral.getLimitedNumber()>0)
		{
			//判断是否超限
			Integer sumCount=exchangeIntegralApplyService.getExchangeIntegralApplyBySysdate(id,createuser.getId(), StringUtil.dateToString(new Date(), null));
			if(sumCount!=null && sumCount>=exchangeIntegral.getLimitedNumber())
			{
				redirectAttributes.addFlashAttribute("message", "提交物品"+exchangeIntegral.getGoodsName()+"超限,每日限制数量:"+exchangeIntegral.getLimitedNumber()+",您今日已提交数量:"+sumCount);
				return "redirect:/exchangeIntegral/";
			}
		}
		return "exchangeIntegralApply/exchangeIntegralApplyForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid ExchangeIntegralApply newExchangeIntegralApply, RedirectAttributes redirectAttributes, ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {
			Long exchangeIntegralId =Long.parseLong(request.getParameter("exchangeIntegralId")); 
			ExchangeIntegral exchangeIntegral=exchangeIntegralService.getExchangeIntegral(exchangeIntegralId);
			
			if(exchangeIntegral.getLimitedNumber()!=null && exchangeIntegral.getLimitedNumber()>0)
			{
				//判断是否超限
				Integer sumCount=exchangeIntegralApplyService.getExchangeIntegralApplyBySysdate(exchangeIntegralId,createuser.getId(), StringUtil.dateToString(new Date(), null));
				if(sumCount!=null)
				{
					if((sumCount+newExchangeIntegralApply.getNumber())>exchangeIntegral.getLimitedNumber())
					{
						redirectAttributes.addFlashAttribute("message", "提交物品"+exchangeIntegral.getGoodsName()+"超限,每日限制数量:"+exchangeIntegral.getLimitedNumber()+",您今日已提交数量:"+sumCount);
						return "redirect:/exchangeIntegral/";
					}
				}else
				{
					if(newExchangeIntegralApply.getNumber()>exchangeIntegral.getLimitedNumber())
					{
						redirectAttributes.addFlashAttribute("message", "提交物品"+exchangeIntegral.getGoodsName()+"超限,每日限制数量:"+exchangeIntegral.getLimitedNumber()+",当次提交数量不能超过:"+exchangeIntegral.getLimitedNumber());
						return "redirect:/exchangeIntegral/";
					}
				}
			}
		 
			newExchangeIntegralApply.setIntegral(newExchangeIntegralApply.getIntegral()*newExchangeIntegralApply.getNumber());
			newExchangeIntegralApply.setExchangeIntegral(exchangeIntegral);
			newExchangeIntegralApply.setCteateUser(createuser);
			newExchangeIntegralApply.setCteateDate(new Date()); 
			newExchangeIntegralApply.setIsdelete(0);
			newExchangeIntegralApply.setStatus("Approval");
			exchangeIntegralApplyService.saveExchangeIntegralApply(newExchangeIntegralApply);

			redirectAttributes.addFlashAttribute("message", "提交申请成功");

			 

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "提交申请失败");
		}

		return "redirect:/exchangeIntegralApply/";
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "approval/{id}")
	public String approval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, ServletRequest request) {
		ExchangeIntegralApply exchangeIntegralApply = exchangeIntegralApplyService.getExchangeIntegralApply(id);
		String status=request.getParameter("status");
		User user = new User(getCurrentUserId());
		if(("pass".equals(status) || "reject".equals(status)) && "Approval".equals(exchangeIntegralApply.getStatus()))
		{
			exchangeIntegralApply.setApprovalUser(user);
			exchangeIntegralApply.setStatus(status);
			exchangeIntegralApplyService.saveExchangeIntegralApplyApproval(exchangeIntegralApply); 
			redirectAttributes.addFlashAttribute("message", "审批成功");
		}
		else
		{
			redirectAttributes.addFlashAttribute("message", "非法操作");
		}

		return "redirect:/exchangeIntegralApply/approvalList";
	}
	
}
