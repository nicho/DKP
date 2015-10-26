/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.gamewin.weixin.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gamewin.weixin.model.ActivityUserList;
import com.gamewin.weixin.model.QueryUserDto;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepository
public interface ActivityUserMybatisDao {

	Integer updateUserStatus(@Param("ids") Long [] ids,@Param("activityId") Long activityId);
	
	List<ActivityUserList> getUserAllActivityUserlist(QueryUserDto dto);
	
}
