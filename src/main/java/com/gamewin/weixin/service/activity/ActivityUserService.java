/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.activity;

import java.util.HashMap;
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

import com.gamewin.weixin.entity.ActivityUser;
import com.gamewin.weixin.model.ActivityUserList;
import com.gamewin.weixin.model.QueryUserDto;
import com.gamewin.weixin.mybatis.ActivityUserMybatisDao;
import com.gamewin.weixin.repository.ActivityUserDao;
import com.github.pagehelper.PageHelper;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ActivityUserService {

	private ActivityUserDao activityUserDao;
	@Autowired
	private ActivityUserMybatisDao activityUserMybatisDao;
	public Boolean findByActivityUser(Long userid, Long actid) {
		List<ActivityUser> list = activityUserDao.getByActuser(userid, actid);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ActivityUser getActivityUser(Long id) {
		return activityUserDao.findOne(id);
	}

	public void saveActivityUser(ActivityUser entity) {
		activityUserDao.save(entity);
	}

	public void deleteActivityUser(Long id) {
		activityUserDao.delete(id);
	}

	public List<ActivityUser> getAllActivityUser() {
		return (List<ActivityUser>) activityUserDao.findAll();
	}

	@Autowired
	public void setActivityUserDao(ActivityUserDao activityUserDao) {
		this.activityUserDao = activityUserDao;
	}
	public List<ActivityUser> getAllActivityUser(Long activityId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("activity", new SearchFilter("activity", Operator.EQ, activityId));
		Specification<ActivityUser> spec = DynamicSpecifications.bySearchFilter(filters.values(), ActivityUser.class);
		return activityUserDao.findAll(spec);
	}
	public Page<ActivityUser> getAllActivityUser(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<ActivityUser> spec = buildSpecification(userId, searchParams);

		return activityUserDao.findAll(spec, pageRequest);
	}
	public Page<ActivityUser> getMyActivityUser(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("user", new SearchFilter("user", Operator.EQ, userId));
		Specification<ActivityUser> spec = DynamicSpecifications.bySearchFilter(filters.values(), ActivityUser.class);
		return activityUserDao.findAll(spec, pageRequest);
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
	private Specification<ActivityUser> buildSpecification(Long activityId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("activity", new SearchFilter("activity", Operator.EQ, activityId));
		Specification<ActivityUser> spec = DynamicSpecifications.bySearchFilter(filters.values(), ActivityUser.class);
		return spec;
	}
	public List<ActivityUserList> getUserAllActivityUserlist(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType,QueryUserDto dto) {
		PageHelper.startPage(pageNumber, pageSize);
		List<ActivityUserList> userList = activityUserMybatisDao.getUserAllActivityUserlist(dto);
		return userList;
	}
}
