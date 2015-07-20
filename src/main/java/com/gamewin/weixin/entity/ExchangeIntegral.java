package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wx_exchange_integral")
public class ExchangeIntegral extends IdEntity {
	private String goodsName;
	private String description;
	private Double integral;
	private User cteateUser;
	private Date cteateDate;
	private String status;
	private Integer isdelete;
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
	public User getCteateUser() {
		return cteateUser;
	}
	public void setCteateUser(User cteateUser) {
		this.cteateUser = cteateUser;
	}
	public Date getCteateDate() {
		return cteateDate;
	}
	public void setCteateDate(Date cteateDate) {
		this.cteateDate = cteateDate;
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
	
}
