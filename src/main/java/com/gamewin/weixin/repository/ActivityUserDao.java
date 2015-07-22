/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gamewin.weixin.entity.ActivityUser;
import com.gamewin.weixin.entity.User;

public interface ActivityUserDao extends PagingAndSortingRepository<ActivityUser, Long>, JpaSpecificationExecutor<ActivityUser> {
	@Query("SELECT t FROM ActivityUser t WHERE t.user.id = ?1 AND t.activity.id = ?2")
	List<ActivityUser> getByActuser(Long userid,Long actid);
	
	 
	@Modifying
	@Query("update from ActivityUser t set t.status='Y' where t.id in (?1) AND t.activity.id = ?2")
	void updateConfirmProcessByUserId(Long [] ids,Long actid);
	
	@Query("SELECT t.user FROM ActivityUser t WHERE  t.activity.id = ?1 AND t.status='Y' ")
	List<User> getActConfirmuser(Long actid);
}
