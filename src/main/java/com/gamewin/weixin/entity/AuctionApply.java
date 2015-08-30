package com.gamewin.weixin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wx_auction_apply")
public class AuctionApply extends IdEntity {
	private Auction auction;
	private String description;
	private Integer number;
	private Double integral;
	private User cteateUser;
	private User approvalUser;
	private Date cteateDate;
	private String status;
	private Integer isdelete;

	public Double getIntegral() {
		return integral;
	}

	public void setIntegral(Double integral) {
		this.integral = integral;
	}

	public Integer getNumber() {
		return number;
	}

	@ManyToOne
	@JoinColumn(name = "auction_id")
	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@ManyToOne
	@JoinColumn(name = "approval_user_id")
	public User getApprovalUser() {
		return approvalUser;
	}

	public void setApprovalUser(User approvalUser) {
		this.approvalUser = approvalUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "cteate_user_id")
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
