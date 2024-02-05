package com.user.rest;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.user.entity.User;
import com.user.service.UserService;




@RestController
@RequestMapping("/api")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/login/{accountId}/{password}")
	public ResponseEntity<String>  login(@PathVariable String accountId,@PathVariable String password)
	{
		String result = userService.getByAccountIdAndPassword(accountId, password);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	@GetMapping("/outlet/{accountId}")
	public List<String> getOutletIdsByAccountId(@PathVariable String accountId) {
        return userService.getOutletIdByAccountId(accountId);
    }
}
