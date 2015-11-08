/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.activity;

import java.util.Date;
import java.util.List;
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
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

/**
 * AuctionApply管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /auctionApply/ Create page : GET /auctionApply/create Create
 * action : POST /auctionApply/create Update page : GET
 * /auctionApply/update/{id} Update action : POST /auctionApply/update Delete
 * action : GET /auctionApply/delete/{id}
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

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		
		// 我的申请
		List<AuctionApply> auctionApplys = auctionApplyService.getAllAuctionApply(userId, searchParams, pageNumber,
				pageSize, sortType);
	 
		PageInfo<AuctionApply> page = new PageInfo<AuctionApply>(auctionApplys);
		model.addAttribute("page", page); 

		model.addAttribute("auctionApplys", auctionApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionApplyItems/myAuctionApplyList";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "approvalList", method = RequestMethod.GET)
	public String approvalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
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

	
	@RequestMapping(value = "user/approvalList", method = RequestMethod.GET)
	public String userapprovalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();
		// 我的申请
		Page<AuctionApply> auctionApplys = auctionApplyService.getAllAuctionApprovalListMy(userId, searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("auctionApplys", auctionApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionApplyItems/auctionApplyListMy";
	}
	
	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "approvalAllList", method = RequestMethod.GET)
	public String approvalAllList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<AuctionApply> auctionApplys = auctionApplyService.getAllAuctionAllList(userId, searchParams, pageNumber,
				pageSize, sortType);

		model.addAttribute("auctionApplys", auctionApplys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionApplyItems/allAuctionApplyList";
	}

	@RequestMapping(value = "create/{id}", method = RequestMethod.GET)
	public String createForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Auction auction = auctionService.getAuction(id);

		if (auction.getNumber() <= 0) {
			redirectAttributes.addFlashAttribute("message", "拍卖物品库存不足!");
			return "redirect:/auction/";
		}
		User user = accountService.getUser(getCurrentUserId());

		// 我的申请
		Integer count = auctionApplyService.getAuctionApplyCountByUser(id, user.getId());
		if (count > 0) {
			redirectAttributes.addFlashAttribute("message", "您的申请正在审核中,无需重复申请!");
			return "redirect:/auction/";
		}

		if (user.getIntegral() < auction.getIntegral()) {
			redirectAttributes.addFlashAttribute("message", "您的积分不够!");
			return "redirect:/auction/";
		}

		if (auction.getLimitedNumber() != null && auction.getLimitedNumber() > 0) {
			// 判断是否超限
			Integer sumCount = auctionApplyService.getAuctionApplyCountByAppId(id, user.getId());
			if (sumCount != null && sumCount >= auction.getLimitedNumber()) {
				redirectAttributes.addFlashAttribute("message", "拍卖物品" + auction.getGoodsName() + "超限,每人限制数量:"
						+ auction.getLimitedNumber() + ",您已申请购买数量:" + sumCount);
				return "redirect:/auction/";
			}
		}

		model.addAttribute("auctionApply", new AuctionApply());
		model.addAttribute("action", "create");
		model.addAttribute("auction", auction);
		return "auctionApplyItems/auctionApplyForm";

	}
	@RequestMapping(value = "user/create/{id}", method = RequestMethod.GET)
	public String usercreateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Auction auction = auctionService.getAuction(id);

		if (auction.getNumber() <= 0) {
			redirectAttributes.addFlashAttribute("message", "拍卖物品库存不足!");
			return "redirect:/auction/user/";
		}
		User user = accountService.getUser(getCurrentUserId());

		// 我的申请
		Integer count = auctionApplyService.getAuctionApplyCountByUser(id, user.getId());
		if (count > 0) {
			redirectAttributes.addFlashAttribute("message", "您的申请正在审核中,无需重复申请!");
			return "redirect:/auction/user/";
		}

		if (user.getIntegral() < auction.getIntegral()) {
			redirectAttributes.addFlashAttribute("message", "您的积分不够!");
			return "redirect:/auction/user/";
		}

		if (auction.getLimitedNumber() != null && auction.getLimitedNumber() > 0) {
			// 判断是否超限
			Integer sumCount = auctionApplyService.getAuctionApplyCountByAppId(id, user.getId());
			if (sumCount != null && sumCount >= auction.getLimitedNumber()) {
				redirectAttributes.addFlashAttribute("message", "拍卖物品" + auction.getGoodsName() + "超限,每人限制数量:"
						+ auction.getLimitedNumber() + ",您已申请购买数量:" + sumCount);
				return "redirect:/auction/user/";
			}
		}

		model.addAttribute("auctionApply", new AuctionApply());
		model.addAttribute("action", "create");
		model.addAttribute("auction", auction);
		return "auctionApplyItems/auctionApplyForm";

	}
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid AuctionApply newAuctionApply, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {
			User user = accountService.getUser(getCurrentUserId());
			Long auctionId = Long.parseLong(request.getParameter("auctionId"));
			Auction auction = auctionService.getAuction(auctionId);
			
			
			if (auction.getNumber() < newAuctionApply.getNumber()) {
				redirectAttributes.addFlashAttribute("message", "拍卖物品库存不足!");
				return "redirect:/auction/";
			}
			
			if(auction.getLimitedNumber()!=null && auction.getLimitedNumber()>0)
			{
				//判断是否超限
				Integer sumCount = auctionApplyService.getAuctionApplyCountByAppId(auctionId, user.getId());
				if(sumCount!=null)
				{
					if((sumCount+newAuctionApply.getNumber())>auction.getLimitedNumber())
					{
						redirectAttributes.addFlashAttribute("message", "提交物品"+auction.getGoodsName()+"超限,每人限制数量:"+auction.getLimitedNumber()+",您已申请购买数量:"+sumCount);
						return "redirect:/auction/";
					}
				}else
				{
					if(newAuctionApply.getNumber()>auction.getLimitedNumber())
					{
						redirectAttributes.addFlashAttribute("message", "提交物品"+auction.getGoodsName()+"超限,每人限制数量:"+auction.getLimitedNumber()+",当次提交数量不能超过:"+auction.getLimitedNumber());
						return "redirect:/auction/";
					}
				}
			}
			
			
			if (user.getIntegral() >= (auction.getIntegral() * newAuctionApply.getNumber())) {
				newAuctionApply.setIntegral(auction.getIntegral() * newAuctionApply.getNumber());
				newAuctionApply.setAuction(auction);
				newAuctionApply.setCteateUser(createuser);
				newAuctionApply.setCteateDate(new Date());
				newAuctionApply.setIsdelete(0);
				newAuctionApply.setStatus("Approval");
				auctionApplyService.saveAuctionApply(newAuctionApply);

				redirectAttributes.addFlashAttribute("message", "提交申请成功");
			} else {
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

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "approval/{id}")
	public String approval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, ServletRequest request) {
		AuctionApply auctionApply = auctionApplyService.getAuctionApply(id);
		String status = request.getParameter("status");
		User user = new User(getCurrentUserId());
		if (("pass".equals(status) || "reject".equals(status)) && "Approval".equals(auctionApply.getStatus())) {
			User createuser = accountService.getUser(auctionApply.getCteateUser().getId());

			if ("pass".equals(status)) {
				if (createuser.getIntegral() >= (auctionApply.getIntegral())) {
					// 扣库存
					Auction auction = auctionService.getAuction(auctionApply.getAuction().getId());
					if (auction.getNumber() >= auctionApply.getNumber()) {
						auctionApply.setApprovalUser(user);
						auctionApply.setStatus(status);
						auctionApplyService.saveAuctionApplyApproval(auctionApply, auction);
						redirectAttributes.addFlashAttribute("message", "审批通过,物品拍卖成功!");
					} else {
						redirectAttributes.addFlashAttribute("message", "物品库存不足,无法兑换物品!");
						return "redirect:/auctionApply/approvalList";
					}

				} else {
					redirectAttributes.addFlashAttribute("message", "会员积分不够,无法兑换物品!");
					return "redirect:/auctionApply/approvalList";
				}
			}else
			{
				auctionApply.setStatus(status);
				auctionApplyService.saveAuctionApply(auctionApply);
				redirectAttributes.addFlashAttribute("message", "审批拒绝!");
			}
		

		} else {
			redirectAttributes.addFlashAttribute("message", "非法操作");
		}

		return "redirect:/auctionApply/approvalList";
	}
	@RequestMapping(value = "user/approval/{id}")
	public String userapproval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, ServletRequest request) {
		AuctionApply auctionApply = auctionApplyService.getAuctionApply(id);
		if(auctionApply.getAuction().getCreateUser().getId().equals(getCurrentUserId()) && "Person".equals(auctionApply.getAuction().getType()))
		{
			String status = request.getParameter("status");
			User user = new User(getCurrentUserId());
			if (("pass".equals(status) || "reject".equals(status)) && "Approval".equals(auctionApply.getStatus())) {
				User createuser = accountService.getUser(auctionApply.getCteateUser().getId());

				if ("pass".equals(status)) {
					if (createuser.getIntegral() >= (auctionApply.getIntegral())) {
						// 扣库存
						Auction auction = auctionService.getAuction(auctionApply.getAuction().getId());
						if (auction.getNumber() >= auctionApply.getNumber()) {
							auctionApply.setApprovalUser(user);
							auctionApply.setStatus(status);
							auctionApplyService.saveAuctionApplyApproval(auctionApply, auction);  
							redirectAttributes.addFlashAttribute("message", "审批通过,物品拍卖成功!");
						} else {
							redirectAttributes.addFlashAttribute("message", "物品库存不足,无法兑换物品!");
							return "redirect:/auctionApply/user/approvalList";
						}

					} else {
						redirectAttributes.addFlashAttribute("message", "会员积分不够,无法兑换物品!");
						return "redirect:/auctionApply/user/approvalList";
					}
				}else
				{
					auctionApply.setStatus(status);
					auctionApplyService.saveAuctionApply(auctionApply);
					redirectAttributes.addFlashAttribute("message", "审批拒绝!");
				}
			

			} else {
				redirectAttributes.addFlashAttribute("message", "非法操作");
			}

			return "redirect:/auctionApply/user/approvalList";
		}else
		{
			redirectAttributes.addFlashAttribute("message", "非法操作");
			return "redirect:/auctionApply/user/approvalList";
		}
		
	}
}
