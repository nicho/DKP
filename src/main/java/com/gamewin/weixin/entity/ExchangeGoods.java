package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wx_exchange_goods")
public class ExchangeGoods extends IdEntity {
	private String goodsName;
	private String description;
	private Double integral;
	private User user;
	private User cteateUser;
	private Date cteateDate;
	private String status;
	private Integer isdelete;

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
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "create_user_id")
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

}
