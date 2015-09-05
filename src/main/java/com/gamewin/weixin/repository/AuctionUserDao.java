/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gamewin.weixin.entity.AuctionUser;

public interface AuctionUserDao extends PagingAndSortingRepository<AuctionUser, Long>,
		JpaSpecificationExecutor<AuctionUser> {
	@Query("SELECT count(*) FROM AuctionUser t WHERE  t.auction.id = ?1 AND t.user.id=?2  ")
	Integer getAuctionUserCountByUser(Long id,Long userid);
	
}
