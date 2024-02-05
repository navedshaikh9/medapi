package com.user.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.repo.UserRepository;




@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;


	@Override
	public String getByAccountIdAndPassword(String accountId, String password) {
		 User user = userRepo.findByAccountIdAndPassword(accountId, password);
		    if(user != null) {
		        return "Success";
		    } else {
		       
		        return "Failed";
		    }
	}


	@Override
	public List<String> getOutletIdByAccountId(String accountId) {
		return userRepo.getOutletIdByAccountId(accountId);
	}
	


}
