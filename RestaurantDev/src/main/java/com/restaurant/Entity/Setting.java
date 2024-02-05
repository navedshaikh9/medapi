package com.restaurant.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Setting {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int setting_id;
	private String setting_name;
	
	private boolean status;

	public Setting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getSetting_id() {
		return setting_id;
	}

	public void setSetting_id(int setting_id) {
		this.setting_id = setting_id;
	}

	public String getSetting_name() {
		return setting_name;
	}

	public void setSetting_name(String setting_name) {
		this.setting_name = setting_name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	

}
