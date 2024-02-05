package com.restaurant.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int trailId;
	private String accountId;
	private String outletId;
	private String ipAddress;
	private String userLicenceKey;
	private LocalDate start;
	private LocalDate end;
	private boolean status;
	
	public Trial() {
		super();
		
	}


	
	public int getTrailId() {
		return trailId;
	}



	public void setTrailId(int trailId) {
		this.trailId = trailId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}



	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserLicenceKey() {
		return userLicenceKey;
	}

	public void setUserLicenceKey(String userLicenceKey) {
		this.userLicenceKey = userLicenceKey;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	
}
