package com.kot.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kot.binding.KOTItem;
import com.kot.service.KOTItemService;




@RestController
@RequestMapping("/api")
public class KOTItemRestController {
	
	@Autowired
	private KOTItemService kotItemService;
	
	
	
	@PostMapping("/kotItem")
	public ResponseEntity<String> createKOTItems(@RequestBody List<KOTItem> kotItemList)
	{
		String status = kotItemService.saveKOTItems(kotItemList);
		return new ResponseEntity<> (status, HttpStatus.CREATED);
	}
	
	@GetMapping("/kotItems/{accountId}")
	public ResponseEntity<List<KOTItem>> getAllKOTItems(@PathVariable String accountId)
	{
		List<KOTItem> allKOTItems = kotItemService.getAllKOTItemsByAccountId(accountId);
		return new ResponseEntity<>(allKOTItems,HttpStatus.OK);
	}
	
	
}
