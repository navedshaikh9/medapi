package com.restaurant.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AdditionalCharges {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int chargesId;
	private String chargesName;
	private double charges;
	private boolean status;
		
	public AdditionalCharges() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getChargesId() {
		return chargesId;
	}

	public void setChargesId(int chargesId) {
		this.chargesId = chargesId;
	}

	public String getChargesName() {
		return chargesName;
	}

	public void setChargesName(String chargesName) {
		this.chargesName = chargesName;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
