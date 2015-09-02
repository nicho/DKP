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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.AuctionService;
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
			auctionService.saveAuction(newAuction);

			redirectAttributes.addFlashAttribute("message", "拍卖物品添加成功");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "拍卖物品添加成功");
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
	
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("auction", auctionService.getAuction(id)); 
		model.addAttribute("action", "update"); 
		return "auctionItems/auctionForm"; 
	}

	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "update", method = RequestMethod.POST) 
	public String update(@Valid @ModelAttribute("newAuction") Auction newAuction, RedirectAttributes redirectAttributes, ServletRequest request) { 
		 User user = new User(getCurrentUserId()); 
		newAuction.setCreateUser(user);
		newAuction.setCreateDate(new Date()); 
		newAuction.setIsdelete(0);
		newAuction.setStatus("Y");
		auctionService.saveAuction(newAuction);
		redirectAttributes.addFlashAttribute("message", "更新拍卖物品成功");
		return "redirect:/auction/";
	}

}
