package com.gamewin.weixin.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wx_game")
public class Game extends IdEntity {
	private String gameName; 
	private Org org;
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	} 
	
	
}
