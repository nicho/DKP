/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.gamewin.weixin.entity.Auction;
import com.gamewin.weixin.entity.AuctionUser;
import com.gamewin.weixin.entity.IntegralHistory;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.repository.AuctionDao;
import com.gamewin.weixin.repository.AuctionUserDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class AuctionUserService {

	private AuctionUserDao auctionUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralHistoryDao integralHistoryDao;
	@Autowired
	private AuctionDao auctionDao;
	
	public Integer getAuctionUserCountByUser(Long id,Long userid) {
		return auctionUserDao.getAuctionUserCountByUser(id, userid);
	}
	public AuctionUser getAuctionUser(Long id) {
		return auctionUserDao.findOne(id);
	}

	public void saveAuctionUser(AuctionUser entity) {
		auctionUserDao.save(entity);
	}

	public void saveAuctionUserApproval(Auction auction,  Long[] chk_list,User nowuser) throws Exception { 
		
		if(chk_list!=null && chk_list.length>0)
		{
			//判断库存
			if(chk_list.length<=auction.getNumber())
			{
				for (int i = 0; i < chk_list.length; i++) {
					AuctionUser  auctionUser=auctionUserDao.findOne(chk_list[i]);
					
					//判断用户积分 
					if(auctionUser.getUser().getIntegral()>=auctionUser.getAuctionIntegral())
					{
						//审批通过当前用户
						auctionUser.setStatus("pass");
						auctionUserDao.save(auctionUser);
						
						//扣除积分,记录日志
						User user = auctionUser.getUser();
						user.setIntegral(user.getIntegral() - auctionUser.getAuctionIntegral());
						userDao.save(user);
						
						// 扣库存
						auction.setNumber(auction.getNumber() - auctionUser.getNumber());
						auctionDao.save(auction);

						// 记录日志
						IntegralHistory ih = new IntegralHistory();
						ih.setAuction(auction);
						ih.setCreateDate(new Date());
						ih.setDescription("竞拍物品:'" + auction.getGoodsName() + "',数量:" + auctionUser.getNumber() + ",扣除积分:"	+ auctionUser.getAuctionIntegral() );
						ih.setIntegral(auctionUser.getAuctionIntegral());
						ih.setIsdelete(0);
						ih.setMark("-");
						ih.setUser(user);
						ih.setStatus("Y");
						ih.setTitle("拍卖竞拍扣除积分");
						integralHistoryDao.save(ih);
						
						if("Person".equals(auctionUser.getAuction().getType()))
						{
							//如果是个人,给发起人,加积分
							User cuser = auctionUser.getAuction().getCreateUser();
							cuser.setIntegral(cuser.getIntegral() + auctionUser.getAuctionIntegral());
							userDao.save(cuser);
							
							
							// 记录日志
							IntegralHistory ih2 = new IntegralHistory();
							ih2.setAuction(auction);
							ih2.setCreateDate(new Date());
							ih2.setDescription("个人物品竞拍:'" + auction.getGoodsName() + "',数量:" + auctionUser.getNumber() + ",获得积分:" + auctionUser.getAuctionIntegral());
							ih2.setIntegral(auctionUser.getAuctionIntegral());
							ih2.setIsdelete(0);
							ih2.setMark("+");
							ih2.setUser(user);
							ih2.setStatus("Y");
							ih2.setTitle("个人竞拍获得积分");
							integralHistoryDao.save(ih2);
							
						}
						
					}else{
						throw new Exception("用户:"+auctionUser.getUser().getName()+",积分不足,无法兑换物品!剩余:"+auctionUser.getUser().getIntegral()+",需要"+auctionUser.getAuctionIntegral());
					}
					
				
				}
				auction.setStatus("pass");
				auction.setApprovalDate(new Date());
				auction.setApprovalUser(nowuser);
				auctionDao.save(auction);
			}else
			{
				throw new Exception("库存不足!库存为"+auction.getNumber());
			} 
		}else
		{
			throw new Exception("没有选择任何人员");
		}

	 
	}

	public void deleteAuctionUser(Long id) {
		auctionUserDao.delete(id);
	}

	public List<AuctionUser> getAllAuctionUser() {
		return (List<AuctionUser>) auctionUserDao.findAll();
	}

	@Autowired
	public void setAuctionUserDao(AuctionUserDao auctionUserDao) {
		this.auctionUserDao = auctionUserDao;
	}

	public List<AuctionUser> getAllAuctionApprovalList(Long auctionid) {

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("auction", new SearchFilter("auction", Operator.EQ, auctionid));
		Specification<AuctionUser> spec = DynamicSpecifications.bySearchFilter(filters.values(), AuctionUser.class);
		Sort sort = new Sort(Direction.DESC, "bidPrice");
		return auctionUserDao.findAll(spec, sort);
	}

}
