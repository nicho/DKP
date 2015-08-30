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

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.entity.AuctionApply;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.AuctionApplyService;
import com.gamewin.weixin.service.activity.AuctionService;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.google.common.collect.Maps;

/**
 * AuctionApply管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /auctionApply/ Create page : GET
 * /auctionApply/create Create action : POST
 * /auctionApply/create Update page : GET
 * /auctionApply/update/{id} Update action : POST
 * /auctionApply/update Delete action : GET
 * /auctionApply/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/auctionApply")
public class AuctionApplyController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private AuctionApplyService auctionApplyService;
	@Autowired
	private AuctionService auctionService;
	@Autowired
	private AccountService accountService;

	@Autowired
	private ValueSetService valueSetService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		// 我的申请
		Page<AuctionApply> auctionApplys = auctionApplyService.getAllAuctionApply(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("auctionApplys", auctionApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionApplyItems/myAuctionApplyList";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "approvalList",method = RequestMethod.GET)
	public String approvalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		// 我的申请
		Page<AuctionApply> auctionApplys = auctionApplyService.getAllAuctionApprovalList(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("auctionApplys", auctionApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionApplyItems/auctionApplyList";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "approvalAllList",method = RequestMethod.GET)
	public String approvalAllList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
	 
		Page<AuctionApply> auctionApplys = auctionApplyService.getAllAuctionAllList(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("auctionApplys", auctionApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionApply/auctionList";
	}
	@RequestMapping(value = "create/{id}", method = RequestMethod.GET)
	public String createForm(@PathVariable("id") Long id,Model model, RedirectAttributes redirectAttributes) {
		Auction auction=auctionService.getAuction(id);
		User user = accountService.getUser(getCurrentUserId());
		
		// 我的申请
		Integer count = auctionApplyService.getAuctionApplyCountByUser(id, user.getId());
		if(count>0)
		{
			redirectAttributes.addFlashAttribute("message", "您的申请正在审核中,无需重复申请!");
			return "redirect:/auction/";
		}
		if(user.getIntegral()>=auction.getIntegral())
		{
			model.addAttribute("auctionApply", new AuctionApply());
			model.addAttribute("action", "create");
			model.addAttribute("auction", auction); 
			return "auctionApplyItems/auctionApplyForm";
		}else
		{
			redirectAttributes.addFlashAttribute("message", "您的积分不够!");
		}
		return "redirect:/auction/";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid AuctionApply newAuctionApply, RedirectAttributes redirectAttributes, ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {
			User user = accountService.getUser(getCurrentUserId());
			Long auctionId =Long.parseLong(request.getParameter("auctionId"));
			Auction auction=auctionService.getAuction(auctionId);
			if(user.getIntegral()>=(auction.getIntegral()*newAuctionApply.getNumber()))
			{
				newAuctionApply.setIntegral(auction.getIntegral()*newAuctionApply.getNumber());
				newAuctionApply.setAuction(auction);
				newAuctionApply.setCteateUser(createuser);
				newAuctionApply.setCteateDate(new Date()); 
				newAuctionApply.setIsdelete(0);
				newAuctionApply.setStatus("Approval");
				auctionApplyService.saveAuctionApply(newAuctionApply);

				redirectAttributes.addFlashAttribute("message", "提交申请成功");
			}else
			{
				redirectAttributes.addFlashAttribute("message", "您的积分不够!");
				return "redirect:/auction/";
			}
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "提交申请失败");
		}

		return "redirect:/auction/";
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
		AuctionApply auctionApply = auctionApplyService.getAuctionApply(id);
		String status=request.getParameter("status");
		User user = new User(getCurrentUserId());
		if(("pass".equals(status) || "reject".equals(status)) && "Approval".equals(auctionApply.getStatus()))
		{
			User createuser = accountService.getUser(auctionApply.getCteateUser().getId());
 
			if(createuser.getIntegral()>=(auctionApply.getIntegral()))
			{
				auctionApply.setApprovalUser(user);
				auctionApply.setStatus(status);
				auctionApplyService.saveAuctionApplyApproval(auctionApply); 
				redirectAttributes.addFlashAttribute("message", "审批成功");
			}
			else
			{
				redirectAttributes.addFlashAttribute("message", "会员积分不够,无法兑换物品!");
				return "redirect:/auctionApply/approvalList";
			}

		}
		else
		{
			redirectAttributes.addFlashAttribute("message", "非法操作");
		}

		return "redirect:/auctionApply/approvalList";
	}
	
}
