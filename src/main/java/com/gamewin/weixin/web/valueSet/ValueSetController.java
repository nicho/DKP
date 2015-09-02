/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.web.valueSet;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

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

import com.gamewin.weixin.entity.ValueSet;
import com.gamewin.weixin.service.valueSet.ValueSetService;
import com.google.common.collect.Maps;

/**
 * ValueSet管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /valueSet/ Create page : GET /valueSet/create Create action :
 * POST /valueSet/create Update page : GET /valueSet/update/{id} Update action :
 * POST /valueSet/update Delete action : GET /valueSet/delete/{id}
 * 
 * @author ly
 */
@Controller
@RequestMapping(value = "/valueSet")
public class ValueSetController {

	private static final String PAGE_SIZE = "10";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("title", "标题");
	}

	@Autowired
	private ValueSetService valueSetService;

	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<ValueSet> valueSets = valueSetService.getAllValueSetByCode("ActivityType", searchParams, pageNumber,
				pageSize, sortType);

		model.addAttribute("valueSets", valueSets);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "valueSet/valueSetList";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("valueSet", new ValueSet());
		model.addAttribute("action", "create");
		return "valueSet/valueSetForm";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid ValueSet newValueSet, RedirectAttributes redirectAttributes, ServletRequest request) {

		newValueSet.setIsdelete(0);
		newValueSet.setStatus("Y");
		valueSetService.saveValueSet(newValueSet);
		redirectAttributes.addFlashAttribute("message", "创建成功");

		return "redirect:/valueSet/";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "disabled/{id}")
	public String disabled(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ValueSet valueSet = valueSetService.getValueSet(id);
		valueSet.setStatus("disabled");
		valueSetService.saveValueSet(valueSet);
		redirectAttributes.addFlashAttribute("message", "失效'" + valueSet.getTypeName() + "'成功");
		return "redirect:/valueSet/";
	}
	@RequiresRoles(value = { "admin", "Head"}, logical = Logical.OR)
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ValueSet valueSet = valueSetService.getValueSet(id);
		valueSet.setStatus("disabled");
		valueSet.setIsdelete(1);
		valueSetService.saveValueSet(valueSet);
		redirectAttributes.addFlashAttribute("message", "删除'" + valueSet.getTypeName() + "'成功");
		return "redirect:/valueSet/";
	}
}
