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

import com.gamewin.weixin.entity.Activity;
import com.gamewin.weixin.entity.IntegralHistory;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.mybatis.ActivityUserMybatisDao;
import com.gamewin.weixin.repository.ActivityDao;
import com.gamewin.weixin.repository.ActivityUserDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class ActivityService {

	private ActivityDao activityDao;
	@Autowired
	private ActivityUserDao activityUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralHistoryDao integralHistoryDao;
	@Autowired
	private ActivityUserMybatisDao activityUserMybatisDao;

	public Activity getActivity(Long id) {
		return activityDao.findOne(id);
	}

	public void saveActivityGRCreate(Activity entity) throws Exception {
		entity.setLevel(0);
		// 个人活动,扣除积分
		User user = userDao.findOne(entity.getCreateUser().getId());
		if (user.getIntegral() >= entity.getIntegral()) {
			activityDao.save(entity);
			user.setIntegral(user.getIntegral() - entity.getIntegral());
			userDao.save(user);
			// 记录日志
			IntegralHistory ih = new IntegralHistory();
			ih.setActivity(entity);
			ih.setCreateDate(new Date());
			ih.setDescription("发布个人活动:'" + entity.getTitle() + "',扣除积分:" + entity.getIntegral());
			ih.setIntegral(entity.getIntegral());
			ih.setIsdelete(0);
			ih.setMark("-");
			ih.setUser(entity.getCreateUser());
			ih.setStatus("Y");
			ih.setTitle("发布个人活动扣除积分");
			integralHistoryDao.save(ih);
		} else {
			throw new Exception("用户积分不足!");
		}

	}

	public void saveActivity(Activity entity) {
		activityDao.save(entity);
	}

	public void saveActivityGHCreate(Activity entity) throws Exception {
		User user = userDao.findOne(entity.getCreateUser().getId());
		Double integral1 = 500.0;
		Double integral2 = 100.0;
		Double integral3 = 30.0;
		if (integral1.equals(entity.getIntegral())) {
			entity.setLevel(1);
		} else if (integral2.equals(entity.getIntegral())) {
			entity.setLevel(2);
		} else if (integral3.equals(entity.getIntegral())) {
			entity.setLevel(3);
		}

		if (integral1.equals(entity.getIntegral())
				&& (!"admin".equals(user.getRoles()) || !"Head".equals(user.getRoles()) || !"OneLevel".equals(user.getRoles()))) {
			throw new Exception("用户权限不足,无法发布≤500人公会活动");
		}
		if (integral2.equals(entity.getIntegral())
				&& (!"admin".equals(user.getRoles()) || !"Head".equals(user.getRoles()) || !"OneLevel".equals(user.getRoles()) || !"TwoLevel"
						.equals(user.getRoles()))) {
			throw new Exception("用户权限不足,无法发布≤100人公会活动");
		}
		if (integral3.equals(entity.getIntegral())
				&& (!"admin".equals(user.getRoles()) || !"Head".equals(user.getRoles()) || !"OneLevel".equals(user.getRoles())
						|| !"TwoLevel".equals(user.getRoles()) || !"ThreeLevel".equals(user.getRoles()))) {
			throw new Exception("用户权限不足,无法发布≤30人公会活动");
		}

		activityDao.save(entity);
	}

	public void saveActivityApprovalConfirm(Activity entity) throws Exception {

		List<User> userList = activityUserDao.getActConfirmuser(entity.getId());

		if (userList != null && userList.size() > 0) {
			activityDao.save(entity);
			Double integer = entity.getIntegral();
			Double pInteger = integer / userList.size();
			for (int i = 0; i < userList.size(); i++) {
				User user = userList.get(i);
				user.setIntegral(user.getIntegral() + pInteger);
				userDao.save(user);

				// 记录日志
				IntegralHistory ih = new IntegralHistory();
				ih.setActivity(entity);
				ih.setCreateDate(new Date());
				ih.setDescription("参与公会活动:'" + entity.getTitle() + "',获得积分:" + pInteger);
				ih.setIntegral(pInteger);
				ih.setIsdelete(0);
				ih.setMark("+");
				ih.setUser(user);
				ih.setStatus("Y");
				ih.setTitle("参与公会活动获得积分");
				integralHistoryDao.save(ih);
			}
		} else {
			throw new Exception("无获得用户");
		}
	}

	public void saveActivityConfirmPass(Activity entity, Long[] chk_list) throws Exception {

		Integer updatecount = activityUserMybatisDao.updateUserStatus(chk_list, entity.getId());
		if (updatecount > 0) {
			activityDao.save(entity);
			List<User> userList = activityUserDao.getActConfirmuser(entity.getId());

			if (userList != null && userList.size() > 0) {
				Double integer = entity.getIntegral();
				Double pInteger = integer / userList.size();
				for (int i = 0; i < userList.size(); i++) {
					User user = userList.get(i);
					user.setIntegral(user.getIntegral() + pInteger);
					userDao.save(user);

					// 记录日志
					IntegralHistory ih = new IntegralHistory();
					ih.setActivity(entity);
					ih.setCreateDate(new Date());
					ih.setDescription("参与个人活动:'" + entity.getTitle() + "',获得积分:" + pInteger);
					ih.setIntegral(pInteger);
					ih.setIsdelete(0);
					ih.setMark("+");
					ih.setUser(user);
					ih.setStatus("Y");
					ih.setTitle("参与个人活动获得积分");
					integralHistoryDao.save(ih);
				}
			}
		} else {
			throw new Exception("用户更新状态失败!");
		}

	}

	public void saveActivityConfirmProcess(Activity entity, Long[] chk_list) throws Exception {

		Integer updatecount = activityUserMybatisDao.updateUserStatus(chk_list, entity.getId());
		if (updatecount > 0) {
			activityDao.save(entity);
		} else {
			throw new Exception("用户更新状态失败!");
		}

	}

	public void deleteActivity(Long id) {
		activityDao.delete(id);
	}

	public List<Activity> getAllActivity() {
		return (List<Activity>) activityDao.findAll();
	}

	@Autowired
	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	public Page<Activity> getAllActivity(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<Activity> spec = buildSpecification(userId, searchParams);

		return activityDao.findAll(spec, pageRequest);
	}

	public Page<Activity> getAllProcessActivty(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType)
			throws Exception {
		User user = userDao.findOne(userId);

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "process"));

		if ("admin".equals(user.getRoles()) || "Head".equals(user.getRoles())) {
			filters.put("level", new SearchFilter("level", Operator.LTE, 1));
		} else if ("OneLevel".equals(user.getRoles())) {
			filters.put("level", new SearchFilter("level", Operator.LTE, 2));
		} else if ("TwoLevel".equals(user.getRoles())) {
			filters.put("level", new SearchFilter("level", Operator.LTE, 3));
		} else {
			throw new Exception("权限不足!");
		}

		Specification<Activity> spec = DynamicSpecifications.bySearchFilter(filters.values(), Activity.class);
		return activityDao.findAll(spec, pageRequest);
	}

	public Page<Activity> getAllConfirmProcessActivty(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType)
			throws Exception {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "ConfirmProcess"));
		User user = userDao.findOne(userId);
		if ("admin".equals(user.getRoles()) || "Head".equals(user.getRoles())) {
			filters.put("level", new SearchFilter("level", Operator.LTE, 1));
		} else if ("OneLevel".equals(user.getRoles())) {
			filters.put("level", new SearchFilter("level", Operator.LTE, 2));
		} else if ("TwoLevel".equals(user.getRoles())) {
			filters.put("level", new SearchFilter("level", Operator.LTE, 3));
		} else {
			throw new Exception("权限不足!");
		}
		Specification<Activity> spec = DynamicSpecifications.bySearchFilter(filters.values(), Activity.class);
		return activityDao.findAll(spec, pageRequest);
	}

	public Page<Activity> getAllmyfqActivity(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("createUser", new SearchFilter("createUser", Operator.EQ, userId));
		Specification<Activity> spec = DynamicSpecifications.bySearchFilter(filters.values(), Activity.class);
		return activityDao.findAll(spec, pageRequest);
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
	private Specification<Activity> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<Activity> spec = DynamicSpecifications.bySearchFilter(filters.values(), Activity.class);
		return spec;
	}
}
