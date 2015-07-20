/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.activity;

import java.util.Date;
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

import com.gamewin.weixin.entity.ExchangeGoods;
import com.gamewin.weixin.entity.IntegralHistory;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.repository.ExchangeGoodsDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ExchangeGoodsService {

	private ExchangeGoodsDao exchangeGoodsDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralHistoryDao integralHistoryDao;

	public void saveExchangeGoodsToo(ExchangeGoods entity, User user) {
		exchangeGoodsDao.save(entity);
		// 扣除用户积分
		user.setIntegral(user.getIntegral() - entity.getIntegral());
		userDao.save(user);
		// 记录日志
		IntegralHistory ih=new IntegralHistory();
		ih.setExchangeGoods(entity);
		ih.setCreateDate(new Date());
		ih.setDescription("拍得物品:'"+entity.getGoodsName()+"',扣除积分:"+entity.getIntegral());
		ih.setIntegral(entity.getIntegral());
		ih.setIsdelete(0);
		ih.setMark("-");
		ih.setUser(user);
		ih.setStatus("Y");
		ih.setTitle("拍卖扣积分");
		integralHistoryDao.save(ih);
	}

	public ExchangeGoods getExchangeGoods(Long id) {
		return exchangeGoodsDao.findOne(id);
	}

	public void saveExchangeGoods(ExchangeGoods entity) {
		exchangeGoodsDao.save(entity);
	}

	public void deleteExchangeGoods(Long id) {
		exchangeGoodsDao.delete(id);
	}

	public List<ExchangeGoods> getAllExchangeGoods() {
		return (List<ExchangeGoods>) exchangeGoodsDao.findAll();
	}

	@Autowired
	public void setExchangeGoodsDao(ExchangeGoodsDao exchangeGoodsDao) {
		this.exchangeGoodsDao = exchangeGoodsDao;
	}

	public Page<ExchangeGoods> getAllExchangeGoods(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<ExchangeGoods> spec = buildSpecification(userId, searchParams);

		return exchangeGoodsDao.findAll(spec, pageRequest);
	}
	public Page<ExchangeGoods> getAllExchangeGoodsMy(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("createUser", new SearchFilter("createUser", Operator.EQ, userId));
		Specification<ExchangeGoods> spec = DynamicSpecifications.bySearchFilter(filters.values(), ExchangeGoods.class);
		return exchangeGoodsDao.findAll(spec, pageRequest);
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
	private Specification<ExchangeGoods> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<ExchangeGoods> spec = DynamicSpecifications.bySearchFilter(filters.values(), ExchangeGoods.class);
		return spec;
	}
}
