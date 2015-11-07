package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wx_auction")
public class Auction extends IdEntity {
	private String goodsName;
	private String description;
	private Double integral; 
	private User createUser;
	private Date createDate;
	private String status;
	private String isAuction;
	private Integer isdelete;
	private Integer number;
	private Integer limitedNumber;
	private User approvalUser;
	private Date approvalDate;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "approval_user_id")
	public User getApprovalUser() {
		return approvalUser;
	}

	public void setApprovalUser(User approvalUser) {
		this.approvalUser = approvalUser;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getIsAuction() {
		return isAuction;
	}

	public void setIsAuction(String isAuction) {
		this.isAuction = isAuction;
	}

	public Integer getLimitedNumber() {
		return limitedNumber;
	}

	public void setLimitedNumber(Integer limitedNumber) {
		this.limitedNumber = limitedNumber;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getIntegral() {
		return integral;
	}

	public void setIntegral(Double integral) {
		this.integral = integral;
	}

 
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


 
}
