/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gamewin.weixin.entity.AuctionApply;

public interface AuctionApplyDao extends PagingAndSortingRepository<AuctionApply, Long>,
		JpaSpecificationExecutor<AuctionApply> {
	@Query("SELECT count(*) FROM AuctionApply t WHERE  t.auction.id = ?1 AND t.cteateUser.id=?2 AND t.status='Approval'  ")
	Integer getAuctionApplyCountByUser(Long id,Long userid);
	
	@Query("SELECT SUM(t.number) FROM AuctionApply t WHERE t.auction.id=?1 AND t.cteateUser.id=?2 AND (t.status='Approval' or  t.status='pass') ")
	Integer getAuctionApplyCountByAppId(Long auction_id,Long cteate_user_id);
}
