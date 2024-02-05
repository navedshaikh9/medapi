package com.user.service;

import java.util.List;

import com.user.entity.User;





public interface UserService {
	
	public String getByAccountIdAndPassword(String accountId, String password);
	
	public List<String> getOutletIdByAccountId(String accountId);
	
}
