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

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.repository.AuctionDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class AuctionService {

	private AuctionDao auctionDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralHistoryDao integralHistoryDao;


	
	public Auction getAuction(Long id) {
		return auctionDao.findOne(id);
	}

	public void saveAuction(Auction entity) {
		auctionDao.save(entity);
	}

	public void deleteAuction(Long id) {
		auctionDao.delete(id);
	}

	public List<Auction> getAllAuction() {
		return (List<Auction>) auctionDao.findAll();
	}

	@Autowired
	public void setAuctionDao(AuctionDao auctionDao) {
		this.auctionDao = auctionDao;
	}

	public Page<Auction> getAllAuction(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("type", new SearchFilter("type", Operator.EQ, "Guild"));
		Specification<Auction> spec = DynamicSpecifications.bySearchFilter(filters.values(), Auction.class);
		return auctionDao.findAll(spec, pageRequest);
	}
	public Page<Auction> getAllAuctionToUser(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0")); 
		filters.put("type", new SearchFilter("type", Operator.EQ, "Person"));
		Specification<Auction> spec = DynamicSpecifications.bySearchFilter(filters.values(), Auction.class);
		
		return auctionDao.findAll(spec, pageRequest);
	}
	public Page<Auction> getApprovalAuction(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0")); 
		filters.put("isAuction", new SearchFilter("isAuction", Operator.EQ, "Y")); 
		filters.put("type", new SearchFilter("type", Operator.EQ, "Guild")); 
		Specification<Auction> spec = DynamicSpecifications.bySearchFilter(filters.values(), Auction.class);
		return auctionDao.findAll(spec, pageRequest);
	}
	public Page<Auction> getApprovalAuctionByMy(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0")); 
		filters.put("isAuction", new SearchFilter("isAuction", Operator.EQ, "Y")); 
		filters.put("type", new SearchFilter("type", Operator.EQ, "Person")); 
		filters.put("createUser.id", new SearchFilter("createUser.id", Operator.EQ, userId)); 
		Specification<Auction> spec = DynamicSpecifications.bySearchFilter(filters.values(), Auction.class);
		return auctionDao.findAll(spec, pageRequest);
	}
	public Page<Auction> getAllAuctionMy(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("createUser", new SearchFilter("createUser", Operator.EQ, userId));
		Specification<Auction> spec = DynamicSpecifications.bySearchFilter(filters.values(), Auction.class);
		return auctionDao.findAll(spec, pageRequest);
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
	private Specification<Auction> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<Auction> spec = DynamicSpecifications.bySearchFilter(filters.values(), Auction.class);
		return spec;
	}
}
