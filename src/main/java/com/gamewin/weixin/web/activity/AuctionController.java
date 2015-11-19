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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.entity.AuctionUser;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.AuctionService;
import com.gamewin.weixin.service.activity.AuctionUserService;
import com.google.common.collect.Maps;

/**
 * Auction管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /auction/ Create page : GET /auction/create Create action :
 * POST /auction/create Update page : GET /auction/update/{id} Update action :
 * POST /auction/update Delete action : GET /auction/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/auction")
public class AuctionController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private AuctionService auctionService;

	@Autowired
	private AccountService accountService;
	@Autowired
	private AuctionUserService auctionUserService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Auction> auctions = auctionService.getAllAuction(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("auctions", auctions);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionItems/auctionList";
	}

	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String userlist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Auction> auctions = auctionService.getAllAuctionToUser(userId, searchParams, pageNumber, pageSize,
				sortType);

		model.addAttribute("auctions", auctions);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("myuserId", userId);
		return "auctionItems/auctionMyList";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "approvalAuctionList", method = RequestMethod.GET)
	public String approvalAuctionList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Auction> auctions = auctionService
				.getApprovalAuction(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("auctions", auctions);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionItems/approvalAuctionList";
	}
	
	@RequestMapping(value = "user/approvalAuctionList", method = RequestMethod.GET)
	public String userapprovalAuctionList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Auction> auctions = auctionService
				.getApprovalAuctionByMy(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("auctions", auctions);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auctionItems/approvalAuctionListMy";
	}
	
	@RequestMapping(value = "mylist", method = RequestMethod.GET)
	public String mylist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Auction> auctions = auctionService.getAllAuctionMy(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("auctions", auctions);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auction/myAuctionList";
	}

	@RequestMapping(value = "alllist", method = RequestMethod.GET)
	public String alllist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Long userId = getCurrentUserId();

		Page<Auction> auctions = auctionService.getAllAuction(userId, searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("auctions", auctions);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "auction/myAuctionList";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("auction", new Auction());
		model.addAttribute("action", "create");

		model.addAttribute("userList", accountService.getAllUserDto());
		return "auctionItems/auctionForm";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Auction newAuction, RedirectAttributes redirectAttributes, ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {

			newAuction.setCreateUser(createuser);
			newAuction.setCreateDate(new Date());
			newAuction.setIsdelete(0);
			newAuction.setStatus("Y");
			newAuction.setType("Guild");
			auctionService.saveAuction(newAuction);

			redirectAttributes.addFlashAttribute("message", "拍卖物品添加成功");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "拍卖物品添加成功");
		}

		return "redirect:/auction/";
	}

	@RequestMapping(value = "user/create", method = RequestMethod.GET)
	public String usercreateForm(Model model) {
		model.addAttribute("auction", new Auction());
		model.addAttribute("action", "create");

		model.addAttribute("userList", accountService.getAllUserDto());
		return "auctionItems/auctionMyForm";
	}

	@RequestMapping(value = "user/create", method = RequestMethod.POST)
	public String usercreate(@Valid Auction newAuction, RedirectAttributes redirectAttributes, ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {

			newAuction.setCreateUser(createuser);
			newAuction.setCreateDate(new Date());
			newAuction.setIsdelete(0);
			newAuction.setStatus("Y");
			newAuction.setType("Person");
			auctionService.saveAuction(newAuction);

			redirectAttributes.addFlashAttribute("message", "个人拍卖物品添加成功");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "个人拍卖物品添加成功");
		}

		return "redirect:/auction/user";
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
		Auction auction = auctionService.getAuction(id);
		auction.setStatus("N");
		auctionService.saveAuction(auction);
		redirectAttributes.addFlashAttribute("message", "失效拍卖'" + auction.getGoodsName() + "'成功");
		return "redirect:/auction/";
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Auction auction = auctionService.getAuction(id);
		auction.setIsdelete(1);
		auctionService.saveAuction(auction);
		redirectAttributes.addFlashAttribute("message", "删除拍卖物品'" + auction.getGoodsName() + "'成功");
		return "redirect:/auction/";
	}

	@RequestMapping(value = "user/delete/{id}")
	public String userdelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Auction auction = auctionService.getAuction(id);
		if (("Person".equals(auction.getType()) && auction.getCreateUser().getId().equals(getCurrentUserId())) || ("admin".equals(user.getRoles()) || "Head".equals(user.getRoles()))) {
			auction.setIsdelete(1);
			auctionService.saveAuction(auction);
			redirectAttributes.addFlashAttribute("message", "删除个人拍卖物品'" + auction.getGoodsName() + "'成功");
			return "redirect:/auction/user/";
		} else {
			redirectAttributes.addFlashAttribute("message", "非法操作");
			return "redirect:/auction/user/";
		}
	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("auction", auctionService.getAuction(id));
		model.addAttribute("action", "update");
		return "auctionItems/auctionForm";
	}

	@RequestMapping(value = "user/update/{id}", method = RequestMethod.GET)
	public String updateMyForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Auction ac = auctionService.getAuction(id);
		if ("Person".equals(ac.getType()) && ac.getCreateUser().getId().equals(getCurrentUserId())) {
			model.addAttribute("auction", ac);
			model.addAttribute("action", "update");
			return "auctionItems/auctionMyForm";
		} else {
			redirectAttributes.addFlashAttribute("message", "非法操作");
			return "redirect:/auction/user/";
		}

	}

	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("newAuction") Auction newAuction,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		User user = new User(getCurrentUserId());
		newAuction.setCreateUser(user);
		newAuction.setCreateDate(new Date());
		newAuction.setIsdelete(0);
		newAuction.setStatus("Y");
		auctionService.saveAuction(newAuction);
		redirectAttributes.addFlashAttribute("message", "更新拍卖物品成功");
		return "redirect:/auction/";
	}
	
	@RequestMapping(value = "user/update", method = RequestMethod.POST)
	public String userupdate(@Valid @ModelAttribute("newAuction") Auction newAuction,
			RedirectAttributes redirectAttributes, ServletRequest request) {
		User user = new User(getCurrentUserId());
		newAuction.setCreateUser(user);
		newAuction.setCreateDate(new Date());
		newAuction.setIsdelete(0);
		newAuction.setStatus("Y");
		if ("Person".equals(newAuction.getType()) && newAuction.getCreateUser().getId().equals(getCurrentUserId())) {
			auctionService.saveAuction(newAuction);
			redirectAttributes.addFlashAttribute("message", "更新拍卖物品成功");
			return "redirect:/auction/user/";
		}else
		{
			redirectAttributes.addFlashAttribute("message", "非法操作");
			return "redirect:/auction/user/";
		}

	}

	@RequestMapping(value = "viewAuctionUser/{id}")
	public String viewAuctionUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Auction auction = auctionService.getAuction(id);
		model.addAttribute("auction", auction);

		if ("Y".equals(auction.getStatus())) {
			redirectAttributes.addFlashAttribute("message", "活动正在拍卖中,无法查看！非法操作!");
			return "redirect:/auction/";
		}
		List<AuctionUser> auctionUsers = auctionUserService.getAllAuctionApprovalList(id);
		model.addAttribute("auctionUsers", auctionUsers);

		return "auctionUserItems/viewAuctionUserConfirm";
	}

	@RequestMapping(value = "user/viewAuctionUser/{id}")
	public String userviewAuctionUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Auction auction = auctionService.getAuction(id);
		model.addAttribute("auction", auction);

		if ("Y".equals(auction.getStatus())) {
			redirectAttributes.addFlashAttribute("message", "活动正在拍卖中,无法查看！非法操作!");
			return "redirect:/auction/user/";
		}
		List<AuctionUser> auctionUsers = auctionUserService.getAllAuctionApprovalList(id);
		model.addAttribute("auctionUsers", auctionUsers);

		return "auctionUserItems/viewAuctionUserConfirm";
	}
	
	@RequiresRoles(value = { "admin", "Head" }, logical = Logical.OR)
	@RequestMapping(value = "approvalConfirm/{id}")
	public String approvalConfirm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Auction auction = auctionService.getAuction(id);
		model.addAttribute("auction", auction);

		List<AuctionUser> auctionUsers = auctionUserService.getAllAuctionApprovalList(id);
		model.addAttribute("auctionUsers", auctionUsers);

		if (!"Y".equals(auction.getStatus())) {
			redirectAttributes.addFlashAttribute("message", "操作失败,该活动已被审批,或非法操作!");
			return "redirect:/auction/approvalAuctionList/";
		}
		return "auctionUserItems/auctionUserConfirm";
	}
	
	@RequestMapping(value = "user/approvalConfirm/{id}")
	public String userapprovalConfirm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Auction auction = auctionService.getAuction(id);
		if("Person".equals(auction.getType()) && auction.getCreateUser().getId().equals(getCurrentUserId()))
		{
			model.addAttribute("auction", auction);

			List<AuctionUser> auctionUsers = auctionUserService.getAllAuctionApprovalList(id);
			model.addAttribute("auctionUsers", auctionUsers);

			if (!"Y".equals(auction.getStatus())) {
				redirectAttributes.addFlashAttribute("message", "操作失败,该活动已被审批,或非法操作!");
				return "redirect:/auction/user/approvalAuctionList/";
			}
			return "auctionUserItems/auctionUserConfirmMy";
		}else
		{
			redirectAttributes.addFlashAttribute("message", "非法操作!");
			return "redirect:/auction/user/approvalAuctionList/";
		}

	}
}
