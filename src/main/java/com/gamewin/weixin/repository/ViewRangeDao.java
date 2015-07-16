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

import com.gamewin.weixin.entity.ViewRange;

public interface ViewRangeDao extends PagingAndSortingRepository<ViewRange, Long>, JpaSpecificationExecutor<ViewRange> {
 
	@Query("select v from ViewRange v where v.task.id=?1")
	List<ViewRange> getViewRangeUserByTask(Long id);
	
	@Modifying
	@Query("delete from ViewRange v where v.task.id=?1")
	void deleteViewRangeUserByTask(Long id);
}
