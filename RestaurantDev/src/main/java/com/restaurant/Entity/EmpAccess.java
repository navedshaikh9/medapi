package com.restaurant.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmpAccess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int subUserid;
	private String userId;
	private String password;
	private String userName;
	private boolean status;
	
	public int getSubUserid() {
		return subUserid;
	}
	public void setSubUserid(int subUserid) {
		this.subUserid = subUserid;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
