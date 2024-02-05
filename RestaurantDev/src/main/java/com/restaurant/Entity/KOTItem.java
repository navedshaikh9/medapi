package com.restaurant.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class KOTItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int kotItemId;
	private int kotId;
	private String itemName;
	private int qty;
	private double price;
	private double itemTotal;
	private String comment;
	private boolean cancelStatus;
	private boolean status;
	private LocalDate created;
	
	public KOTItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KOTItem(int kotItemId, int kotId, String itemName, int qty, double price, double itemTotal,
			boolean status) {
		super();
		this.kotItemId = kotItemId;
		this.kotId = kotId;
		this.itemName = itemName;
		this.qty = qty;
		this.price = price;
		this.itemTotal = itemTotal;
		this.status = status;
	}

	public int getKotItemId() {
		return kotItemId;
	}

	public void setKotItemId(int kotItemId) {
		this.kotItemId = kotItemId;
	}

	public int getKotId() {
		return kotId;
	}

	public void setKotId(int kotId) {
		this.kotId = kotId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(double itemTotal) {
		this.itemTotal = itemTotal;
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

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}
	
	
	
}
