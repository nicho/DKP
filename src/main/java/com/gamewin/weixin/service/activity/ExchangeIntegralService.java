/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.activity;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.gamewin.weixin.entity.ExchangeIntegral;
import com.gamewin.weixin.repository.ExchangeIntegralDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ExchangeIntegralService {

	private ExchangeIntegralDao exchangeIntegralDao;

	public ExchangeIntegral getExchangeIntegral(Long id) {
		return exchangeIntegralDao.findOne(id);
	}

	public void saveExchangeIntegral(ExchangeIntegral entity) {
		exchangeIntegralDao.save(entity);
	}

	public void deleteExchangeIntegral(Long id) {
		exchangeIntegralDao.delete(id);
	}

	public List<ExchangeIntegral> getAllExchangeIntegral() {
		return (List<ExchangeIntegral>) exchangeIntegralDao.findAll();
	}

	@Autowired
	public void setExchangeIntegralDao(ExchangeIntegralDao exchangeIntegralDao) {
		this.exchangeIntegralDao = exchangeIntegralDao;
	}

	public Page<ExchangeIntegral> getAllExchangeIntegral(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<ExchangeIntegral> spec = buildSpecification(userId, searchParams);

		return exchangeIntegralDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<ExchangeIntegral> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<ExchangeIntegral> spec = DynamicSpecifications.bySearchFilter(filters.values(), ExchangeIntegral.class);
		return spec;
	}
}
