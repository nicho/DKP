package com.gamewin.weixin.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wx_org")
public class Org extends IdEntity {
	private String orgName; 
	private String orgDetails;
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgDetails() {
		return orgDetails;
	}
	public void setOrgDetails(String orgDetails) {
		this.orgDetails = orgDetails;
	} 
	
}
