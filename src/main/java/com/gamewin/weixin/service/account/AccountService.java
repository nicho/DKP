/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.service.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Clock;
import org.springside.modules.utils.Encodes;

import com.gamewin.weixin.entity.User;
import com.gamewin.weixin.model.QueryUserDto;
import com.gamewin.weixin.model.UserDto;
import com.gamewin.weixin.mybatis.UserMybatisDao;
import com.gamewin.weixin.repository.UserDao;
import com.gamewin.weixin.service.ServiceException;
import com.gamewin.weixin.service.account.ShiroDbRealm.ShiroUser;
import com.github.pagehelper.PageHelper;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService {
	@Autowired
	private UserMybatisDao userMybatisDao;

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	private UserDao userDao;
	private Clock clock = Clock.DEFAULT;

	public List<User> getAllUser() {
		return (List<User>) userDao.findAll();
	}

	public List<UserDto> getAllUserDto() {
		List<UserDto> dtoList = new ArrayList<UserDto>();
		List<User> userList = (List<User>) userDao.findAll();
		if(userList!=null && userList.size()>0)
		{
			for (int i = 0; i < userList.size(); i++) {
				User user=userList.get(i);
				UserDto dto=new UserDto();
				dto.setId(user.getId());
				dto.setGameName(user.getGameName());
				dto.setIntegral(user.getIntegral());
				dto.setName(user.getName());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	public List<UserDto> getAllUserDtoByName(String name) {
		List<UserDto> dtoList = new ArrayList<UserDto>();
		QueryUserDto dtox=new QueryUserDto();
		dtox.setSearch_LIKE_userName(name);
		List<User> userList = (List<User>) userMybatisDao.getUserAllUserlist(dtox);
		if(userList!=null && userList.size()>0)
		{
			for (int i = 0; i < userList.size(); i++) {
				User user=userList.get(i);
				UserDto dto=new UserDto();
				dto.setId(user.getId());
				dto.setGameName(user.getGameName());
				dto.setIntegral(user.getIntegral());
				dto.setName(user.getName());
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	public User getUser(Long id) {
		return userDao.findOne(id);
	}
	public User findByGameName(String loginName) {
		return userDao.findByGameName(loginName);
	}
	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}
	public User findUserByGameName(String loginName) {
		return userDao.findByGameName(loginName);
	}
	
	public User findByWeixinOpenid(String weixinOpenid) {
		return userDao.findByWeixinOpenid(weixinOpenid);
	}
	public User findByWxWeixinOpenid(String weixinOpenid) {
		return userDao.findByWxWeixinOpenid(weixinOpenid);
	}
	public void registerUser(User user) {
		entryptPassword(user);
		user.setRoles("user");
		user.setRegisterDate(clock.getCurrentDate());

		userDao.save(user);
	}

	public void registerAdminUser(User user) {
		entryptPassword(user);
		user.setRoles("ThreeAdmin");
		user.setRegisterDate(clock.getCurrentDate());

		userDao.save(user);
	}

	public void createUser(User user) {
		entryptPassword(user);
		user.setRegisterDate(clock.getCurrentDate());

		userDao.save(user);
	}

	public void updateUser(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}
		userDao.save(user);
	}

	public void deleteUser(User user) {
		if (isSupervisor(user.getId())) {
			logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
			throw new ServiceException("不能删除超级管理员用户");
		}
		user.setIsdelete(1);
		userDao.save(user);
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	public Page<User> getAllUserlist(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

		return userDao.findAll(spec, pageRequest);
	}

	public Page<User> getUserByAuditUserlist(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("upuser.id", new SearchFilter("upuser.id", Operator.EQ, userId));
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "Audit"));
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

		return userDao.findAll(spec, pageRequest);
	}

	public Page<User> getUserByAuditUserAdminlist(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isdelete", new SearchFilter("isdelete", Operator.EQ, "0"));
		filters.put("status", new SearchFilter("status", Operator.EQ, "Audit"));
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

		return userDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "registerDate");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	public List<User> getUserAllUserlist(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType,QueryUserDto userName) {
		PageHelper.startPage(pageNumber, pageSize);
		List<User> userList = userMybatisDao.getUserAllUserlist(userName);
		return userList;
	}
	public List<User> getUserAllUserIntegrallist(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType,QueryUserDto dto) {
		PageHelper.startPage(pageNumber, pageSize);
		List<User> userList = userMybatisDao.getUserAllUserIntegrallist(dto);
		return userList;
	}
	public Double getAllIntegralSum( ) {
		return userMybatisDao.getAllIntegralSum();
	}
}
