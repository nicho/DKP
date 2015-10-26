package com.gamewin.weixin.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wx_value_set")
public class ValueSet extends IdEntity { 
	private String typeCode; 
	private String typeName; 
	private String typeExplain; 
	private String status;
	private Integer isdelete;
	private Double integral;
	
	public Double getIntegral() {
		return integral;
	}
	public void setIntegral(Double integral) {
		this.integral = integral;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeExplain() {
		return typeExplain;
	}
	public void setTypeExplain(String typeExplain) {
		this.typeExplain = typeExplain;
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
