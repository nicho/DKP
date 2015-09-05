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

	public Org getOrg(Long id) {
		return orgDao.findOne(id);
	}
	public String getMyOrgNotice() {
		return orgDao.findOne(new Long(1)).getNotice();
	}
	public void updateOrg(Org org) {
		Org orgx=orgDao.findOne(org.getId());
		orgx.setNotice(org.getNotice());
		orgDao.save(orgx);
	}
}
