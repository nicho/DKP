/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.punish;

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

import com.gamewin.weixin.entity.Punish;
import com.gamewin.weixin.entity.IntegralHistory;
import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.repository.PunishDao;
import com.gamewin.weixin.repository.IntegralHistoryDao;
import com.gamewin.weixin.repository.UserDao;

// Spring Bean的标识.
@Component
// 类中所有public函数都纳入事务管理的标识.
@Transactional
public class PunishService {

	private PunishDao punishDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralHistoryDao integralHistoryDao;

	public void savePunishToo(Punish entity, User user) {
		punishDao.save(entity);
		// 扣除用户积分
		user.setIntegral(user.getIntegral() - entity.getIntegral());
		userDao.save(user);
		// 记录日志
		IntegralHistory ih = new IntegralHistory();
		ih.setPunish(entity);
		ih.setCreateDate(new Date());
		ih.setDescription("惩罚项目:'" + entity.getDescription() + "',扣除积分:" + entity.getIntegral());
		ih.setIntegral(entity.getIntegral());
		ih.setIsdelete(0);
		ih.setMark("-");
		ih.setUser(user);
		ih.setStatus("Y");
		ih.setTitle("惩罚扣积分");
		integralHistoryDao.save(ih);
	}

	public Punish getPunish(Long id) {
		return punishDao.findOne(id);
	}

	public void savePunish(Punish entity) {
		punishDao.save(entity);
	}

	public void deletePunish(Long id) {
		punishDao.delete(id);
	}

	public List<Punish> getAllPunish() {
		return (List<Punish>) punishDao.findAll();
	}

	@Autowired
	public void setPunishDao(PunishDao punishDao) {
		this.punishDao = punishDao;
	}

	public Page<Punish> getAllPunish(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<Punish> spec = buildSpecification(userId, searchParams);

		return punishDao.findAll(spec, pageRequest);
	}

	public Page<Punish> getAllPunishMy(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("createUser", new SearchFilter("createUser", Operator.EQ, userId));
		Specification<Punish> spec = DynamicSpecifications.bySearchFilter(filters.values(), Punish.class);
		return punishDao.findAll(spec, pageRequest);
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
	private Specification<Punish> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<Punish> spec = DynamicSpecifications.bySearchFilter(filters.values(), Punish.class);
		return spec;
	}
}
