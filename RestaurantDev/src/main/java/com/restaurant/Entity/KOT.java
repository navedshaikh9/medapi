package com.restaurant.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class KOT {
	@Id
	private int kotId;
	private String kotOrder;
	private String orderType;
	private double totalPrice;
	private LocalDate created;
	private String time;
	private String comment;
	private boolean cancelStatus;
	private boolean status;
	
	public KOT() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getKotId() {
		return kotId;
	}

	public void setKotId(int kotId) {
		this.kotId = kotId;
	}

	public String getKotOrder() {
		return kotOrder;
	}

	public void setKotOrder(String kotOrder) {
		this.kotOrder = kotOrder;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(boolean cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
}
