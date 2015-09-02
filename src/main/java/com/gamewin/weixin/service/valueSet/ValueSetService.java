/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.valueSet;

import java.util.HashMap;
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

import com.gamewin.weixin.entity.ValueSet;
import com.gamewin.weixin.repository.ValueSetDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ValueSetService {

	private ValueSetDao valueSetDao;

	public ValueSet getValueSet(Long id) {
		return valueSetDao.findOne(id);
	}

	public void saveValueSet(ValueSet entity) {
		valueSetDao.save(entity);
	}

	public void deleteValueSet(Long id) {
		valueSetDao.delete(id);
	}

	public List<ValueSet> getAllValueSet() {
		return (List<ValueSet>) valueSetDao.findAll();
	}

	@Autowired
	public void setValueSetDao(ValueSetDao valueSetDao) {
		this.valueSetDao = valueSetDao;
	}

	public Page<ValueSet> getAllValueSetByCode(String code, Map<String, Object> searchParams, int pageNumber,
			int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<ValueSet> spec = buildSpecification(code, searchParams);

		return valueSetDao.findAll(spec, pageRequest);
	}

	public List<ValueSet> getActivityType(String code) {

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("typeCode", new SearchFilter("typeCode", Operator.EQ, code));
		filters.put("status", new SearchFilter("status", Operator.EQ, "Y"));
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<ValueSet> spec = DynamicSpecifications.bySearchFilter(filters.values(), ValueSet.class);
		return valueSetDao.findAll(spec);
	}
	public List<ValueSet> getActivityTypeAll(String code) {

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("typeCode", new SearchFilter("typeCode", Operator.EQ, code)); 
		Specification<ValueSet> spec = DynamicSpecifications.bySearchFilter(filters.values(), ValueSet.class);
		return valueSetDao.findAll(spec);
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
	private Specification<ValueSet> buildSpecification(String code, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("typeCode", new SearchFilter("typeCode", Operator.EQ, code)); 
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<ValueSet> spec = DynamicSpecifications.bySearchFilter(filters.values(), ValueSet.class);
		return spec;
	}
}
