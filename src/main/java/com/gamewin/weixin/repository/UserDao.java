/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gamewin.weixin.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
	User findByLoginName(String loginName);
	User findByGameName(String gameName);
	User findByWeixinOpenid(String weixinOpenid);
	User findByWxWeixinOpenid(String wxweixinOpenid);

}
