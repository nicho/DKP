/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gamewin.weixin.entity.Org;
import com.gamewin.weixin.mybatis.OrgMybatisDao;
import com.gamewin.weixin.repository.OrgDao;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class OrgService {

	@Autowired
	private OrgDao orgDao;
	@Autowired
	private OrgMybatisDao orgMybatisDao;
	
	public Org getOrg(Long id) {
		return orgMybatisDao.getOrg(id);
	}
	public String getMyOrgNotice(Long id) {
		return orgMybatisDao.getOrg(id).getNotice();
	}
	public void updateOrgNotice(Org org) { 
		orgMybatisDao.updateOrgNotice(org);
	}
}
