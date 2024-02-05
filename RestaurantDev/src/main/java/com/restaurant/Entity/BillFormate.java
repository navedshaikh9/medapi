package com.restaurant.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BillFormate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int billFormatId;
	private String hotelName;
	private String hotelType;
	private String address;
	private String contact;
	private String message;
	private boolean status;

	public BillFormate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBillFormatId() {
		return billFormatId;
	}

	public void setBillFormatId(int billFormatId) {
		this.billFormatId = billFormatId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
