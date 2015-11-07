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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.entity.AuctionUser;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.service.account.AccountService;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.gamewin.weixin.service.activity.AuctionService;
import com.gamewin.weixin.service.activity.AuctionUserService;
import com.google.common.collect.Maps;

/**
 * AuctionUser管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /auctionUser/ Create page : GET /auctionUser/create Create
 * action : POST /auctionUser/create Update page : GET /auctionUser/update/{id}
 * Update action : POST /auctionUser/update Delete action : GET
 * /auctionUser/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/auctionUser")
public class AuctionUserController {

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private AuctionUserService auctionUserService;
	@Autowired
	private AuctionService auctionService;
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "create/{id}", method = RequestMethod.GET)
	public String createForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Auction auction = auctionService.getAuction(id);

		User user = accountService.getUser(getCurrentUserId());

		if (user.getIntegral() < auction.getIntegral()) {
			redirectAttributes.addFlashAttribute("message", "您的积分不足起拍价!无法参与本次竞拍!");
			return "redirect:/auction/";
		}

		// 判断人员是否参与过本轮竞拍
		Integer count = auctionUserService.getAuctionUserCountByUser(id, user.getId());
		if (count > 0) {
			redirectAttributes.addFlashAttribute("message", "您已经参与过" + auction.getGoodsName() + "竞拍!无需重复参与!");
			return "redirect:/auction/";
		}
		model.addAttribute("auctionUser", new AuctionUser());
		model.addAttribute("action", "create");
		model.addAttribute("auction", auction);
		return "auctionUserItems/auctionUserForm";

	}

	@RequestMapping(value = "user/create/{id}", method = RequestMethod.GET)
	public String usercreateForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Auction auction = auctionService.getAuction(id);

		User user = accountService.getUser(getCurrentUserId());

		if (user.getIntegral() < auction.getIntegral()) {
			redirectAttributes.addFlashAttribute("message", "您的积分不足起拍价!无法参与本次竞拍!");
			return "redirect:/auction/user/";
		}

		// 判断人员是否参与过本轮竞拍
		Integer count = auctionUserService.getAuctionUserCountByUser(id, user.getId());
		if (count > 0) {
			redirectAttributes.addFlashAttribute("message", "您已经参与过" + auction.getGoodsName() + "竞拍!无需重复参与!");
			return "redirect:/auction/user/";
		}
		model.addAttribute("auctionUser", new AuctionUser());
		model.addAttribute("action", "create");
		model.addAttribute("auction", auction);
		return "auctionUserItems/auctionUserForm";

	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid AuctionUser newAuctionUser, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		User createuser = new User(getCurrentUserId());

		try {
			User user = accountService.getUser(getCurrentUserId());
			Long auctionId = Long.parseLong(request.getParameter("auctionId"));
			Auction auction = auctionService.getAuction(auctionId);

			if (user.getIntegral() < auction.getIntegral()) {
				redirectAttributes.addFlashAttribute("message", "您的积分不足起拍价!无法参与本次竞拍!");
				return "redirect:/auction/";
			}
			// 判断人员是否参与过本轮竞拍
			Integer count = auctionUserService.getAuctionUserCountByUser(auctionId, user.getId());
			if (count > 0) {
				redirectAttributes.addFlashAttribute("message", "您已经参与过" + auction.getGoodsName() + "竞拍!无需重复参与!");
				return "redirect:/auction/";
			}

			if (newAuctionUser.getNumber() > auction.getNumber()) {
				redirectAttributes.addFlashAttribute("message", "库存不足,无法购买" + newAuctionUser.getNumber() + "件!");
				return "redirect:/auction/";
			}

			if (user.getIntegral() >= (newAuctionUser.getBidPrice() * newAuctionUser.getNumber())) {
				newAuctionUser.setAuctionIntegral(newAuctionUser.getBidPrice() * newAuctionUser.getNumber());
				newAuctionUser.setAuction(auction);
				newAuctionUser.setUser(createuser);
				newAuctionUser.setCreateDate(new Date());
				newAuctionUser.setStatus("Approval");
				auctionUserService.saveAuctionUser(newAuctionUser);

				redirectAttributes.addFlashAttribute("message", "提交竞拍成功");
			} else {
				redirectAttributes.addFlashAttribute("message", "您的积分不够!无法同时购买" + newAuctionUser.getNumber() + "件");
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
	@RequestMapping(value = "approval", method = RequestMethod.POST)
	public String approval(@Valid Long auctionId, @Valid Long[] chk_list, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Auction auction = auctionService.getAuction(auctionId);
		if ("Y".equals(auction.getStatus())) {
			try {
				auctionUserService.saveAuctionUserApproval(auction, chk_list, new User(getCurrentUserId()));
				redirectAttributes.addFlashAttribute("message", "物品拍卖成功!");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
				return "redirect:/auction/approvalAuctionList";
			}

		} else {
			redirectAttributes.addFlashAttribute("message", "非法操作");
		}

		return "redirect:/auction/approvalAuctionList";
	}

	@RequestMapping(value = "user/approval", method = RequestMethod.POST)
	public String userapproval(@Valid Long auctionId, @Valid Long[] chk_list, RedirectAttributes redirectAttributes,
			ServletRequest request) {
		Auction auction = auctionService.getAuction(auctionId);
		if ("Person".equals(auction.getType()) && auction.getCreateUser().getId().equals(getCurrentUserId())) {
			if ("Y".equals(auction.getStatus())) {
				try {
					auctionUserService.saveAuctionUserApproval(auction, chk_list, new User(getCurrentUserId()));
					redirectAttributes.addFlashAttribute("message", "物品拍卖成功!");
				} catch (Exception e) {
					redirectAttributes.addFlashAttribute("message", e.getMessage());
					return "redirect:/auction/approvalAuctionList";
				}

			} else {
				redirectAttributes.addFlashAttribute("message", "非法操作");
			}

			return "redirect:/auction/user/approvalAuctionList";
		} else {
			redirectAttributes.addFlashAttribute("message", "非法操作");
			return "redirect:/auction/user/approvalAuctionList";
		}

	}
}
