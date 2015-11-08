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
import com.gamewin.weixin.model.QueryAuctionApplyDto;
import com.gamewin.weixin.mybatis.AuctionApplyMybatisDao;
import com.gamewin.weixin.repository.AuctionApplyDao;
import com.gamewin.weixin.repository.AuctionDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;
import com.github.pagehelper.PageHelper;

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
	@Autowired
	private AuctionApplyMybatisDao auctionApplyMybatisDao;

	public Integer getAuctionApplyCountByAppId(Long auction_id, Long cteate_user_id) {
		return auctionApplyDao.getAuctionApplyCountByAppId(auction_id, cteate_user_id);
	}

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
			ih.setAuctionApply(entity);
			ih.setCreateDate(new Date());
			ih.setDescription("兑换物品:'" + entity.getAuction().getGoodsName() + "',数量:" + entity.getNumber() + ",扣除积分:" + entity.getIntegral());
			ih.setIntegral(entity.getIntegral());
			ih.setIsdelete(0);
			ih.setMark("-");
			ih.setUser(user);
			ih.setStatus("Y");
			ih.setTitle("拍卖扣除积分");
			integralHistoryDao.save(ih);
			if("Person".equals(entity.getAuction().getType()))
			{
				//如果是个人,给发起人,加积分
				User cuser = entity.getAuction().getCreateUser();
				cuser.setIntegral(cuser.getIntegral() + entity.getIntegral());
				userDao.save(cuser);
				
				
				// 记录日志
				IntegralHistory ih2 = new IntegralHistory();
				ih2.setAuctionApply(entity);
				ih2.setCreateDate(new Date());
				ih2.setDescription("个人物品拍卖:'" + entity.getAuction().getGoodsName() + "',数量:" + entity.getNumber() + ",获得积分:" + entity.getIntegral());
				ih2.setIntegral(entity.getIntegral());
				ih2.setIsdelete(0);
				ih2.setMark("+");
				ih2.setUser(user);
				ih2.setStatus("Y");
				ih2.setTitle("个人拍卖获得积分");
				integralHistoryDao.save(ih2);
				
			}
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

	public List<AuctionApply> getAllAuctionApply(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageHelper.startPage(pageNumber, pageSize);
		QueryAuctionApplyDto dto = new QueryAuctionApplyDto();
		dto.setUserId(userId);
		List<AuctionApply> auctionApplyList = auctionApplyMybatisDao.getAuctionApplyAlllist(dto);
		return auctionApplyList;
	}

	public Page<AuctionApply> getAllAuctionApprovalList(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "Approval"));
		filters.put("auction.type", new SearchFilter("auction.type", Operator.EQ, "Guild"));
		Specification<AuctionApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), AuctionApply.class);
		return auctionApplyDao.findAll(spec, pageRequest);
	}
	public Page<AuctionApply> getAllAuctionApprovalListMy(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "Approval"));
		filters.put("auction.type", new SearchFilter("auction.type", Operator.EQ, "Person"));
		filters.put("auction.createUser.id", new SearchFilter("auction.createUser.id", Operator.EQ, userId)); 
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

}
