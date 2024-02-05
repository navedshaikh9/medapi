package com.restaurant.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderTypeId;
	private String orderType;
	private boolean status;

	public OrderType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(int orderTypeId) {
		this.orderTypeId = orderTypeId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	

}
