package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wx_activity")
public class Activity extends IdEntity {
	private String title;
	private String type;
	private String personCount;
	private String explain;
	private Date startDate;
	private Date endDate;
	private Date qrCodeUrl;
	private Date webUrl;
	private User createUser;
	private Date createDate;
	private Date updateDate;
	private Double integral;
	private User approveUser;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPersonCount() {
		return personCount;
	}
	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
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
	public Date getQrCodeUrl() {
		return qrCodeUrl;
	}
	public void setQrCodeUrl(Date qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}
	public Date getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(Date webUrl) {
		this.webUrl = webUrl;
	}
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
	public User getApproveUser() {
		return approveUser;
	}
	public void setApproveUser(User approveUser) {
		this.approveUser = approveUser;
	}

}
