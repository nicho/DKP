package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wx_activity")
public class Activity extends IdEntity {
	private String title;
	private String activityType;
	private String fType;
	private String personCount;
	private String activityExplain;
	private Date startDate;
	private Date endDate;
	private String qrCodeUrl;
	private String webUrl;
	private User createUser;
	private Date createDate;
	private Date updateDate;
	private Double integral;
	private User approveUser;
	private User confirmUser;
	private String status;
	private Integer level;
	private Integer isdelete;
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@ManyToOne
	@JoinColumn(name = "confirm_user_id")
	public User getConfirmUser() {
		return confirmUser;
	}

	public void setConfirmUser(User confirmUser) {
		this.confirmUser = confirmUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
	}

	public String getfType() {
		return fType;
	}

	public void setfType(String fType) {
		this.fType = fType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getPersonCount() {
		return personCount;
	}

	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}

	public String getActivityExplain() {
		return activityExplain;
	}

	public void setActivityExplain(String activityExplain) {
		this.activityExplain = activityExplain;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	 

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	// JPA 基于USER_ID列的多对一关系定义
	@ManyToOne
	@JoinColumn(name = "create_user_id")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Double getIntegral() {
		return integral;
	}

	public void setIntegral(Double integral) {
		this.integral = integral;
	}

	// JPA 基于USER_ID列的多对一关系定义
	@ManyToOne
	@JoinColumn(name = "approve_user_id")
	public User getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(User approveUser) {
		this.approveUser = approveUser;
	}

}
