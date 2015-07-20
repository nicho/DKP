package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wx_integral_history")
public class IntegralHistory extends IdEntity {
	private String title;
	private String description;
	private Double integral;
	private String mark;
	private Date createDate;
	private User user;
	private String status; 
	private Integer isdelete;
	private Activity activity;
	private ExchangeGoods exchangeGoods;
	private ExchangeIntegral exchangeIntegral;
	
	
	
	
	@ManyToOne
	@JoinColumn(name = "activity_id")
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	@ManyToOne
	@JoinColumn(name = "exchangegoods_id")
	public ExchangeGoods getExchangeGoods() {
		return exchangeGoods;
	}

	public void setExchangeGoods(ExchangeGoods exchangeGoods) {
		this.exchangeGoods = exchangeGoods;
	}
	@ManyToOne
	@JoinColumn(name = "exchangeintegral_id")
	public ExchangeIntegral getExchangeIntegral() {
		return exchangeIntegral;
	}

	public void setExchangeIntegral(ExchangeIntegral exchangeIntegral) {
		this.exchangeIntegral = exchangeIntegral;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
