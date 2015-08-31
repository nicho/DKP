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

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.entity.AuctionApply;
import com.gamewin.weixin.entity.IntegralHistory;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.repository.AuctionApplyDao;
import com.gamewin.weixin.repository.AuctionDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class AuctionApplyService {

	private AuctionApplyDao auctionApplyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralHistoryDao integralHistoryDao;
	@Autowired
	private AuctionDao auctionDao;

	public Integer getAuctionApplyCountByUser(Long id, Long userid) {
		return auctionApplyDao.getAuctionApplyCountByUser(id, userid);
	}

	public AuctionApply getAuctionApply(Long id) {
		return auctionApplyDao.findOne(id);
	}

	public void saveAuctionApply(AuctionApply entity) {
		auctionApplyDao.save(entity);
	}

	public void saveAuctionApplyApproval(AuctionApply entity, Auction auction) {
		auctionApplyDao.save(entity);

		if ("pass".equals(entity.getStatus())) {
			// 审批通过后，扣积分
			User user = entity.getCteateUser();
			user.setIntegral(user.getIntegral() - entity.getIntegral());
			userDao.save(user);
			// 扣库存
			auction.setNumber(auction.getNumber() - entity.getNumber());
			auctionDao.save(auction);

			// 记录日志
			IntegralHistory ih = new IntegralHistory();
			// ih.setAuctionApply(entity);
			ih.setCreateDate(new Date());
			ih.setDescription("兑换物品:'" + entity.getAuction().getGoodsName() + "',数量:" + entity.getNumber() + ",扣除积分:" + entity.getIntegral());
			ih.setIntegral(entity.getIntegral());
			ih.setIsdelete(0);
			ih.setMark("-");
			ih.setUser(user);
			ih.setStatus("Y");
			ih.setTitle("拍卖扣除积分");
			integralHistoryDao.save(ih);
		}

	}

	public void deleteAuctionApply(Long id) {
		auctionApplyDao.delete(id);
	}

	public List<AuctionApply> getAllAuctionApply() {
		return (List<AuctionApply>) auctionApplyDao.findAll();
	}

	@Autowired
	public void setAuctionApplyDao(AuctionApplyDao auctionApplyDao) {
		this.auctionApplyDao = auctionApplyDao;
	}

	public Page<AuctionApply> getAllAuctionApply(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<AuctionApply> spec = buildSpecification(userId, searchParams);

		return auctionApplyDao.findAll(spec, pageRequest);
	}

	public Page<AuctionApply> getAllAuctionApprovalList(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "Approval"));
		Specification<AuctionApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), AuctionApply.class);
		return auctionApplyDao.findAll(spec, pageRequest);
	}

	public Page<AuctionApply> getAllAuctionAllList(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "pass"));
		Specification<AuctionApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), AuctionApply.class);
		return auctionApplyDao.findAll(spec, pageRequest);
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
	private Specification<AuctionApply> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("cteateUser", new SearchFilter("cteateUser", Operator.EQ, userId));
		Specification<AuctionApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), AuctionApply.class);
		return spec;
	}
}
