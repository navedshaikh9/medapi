package com.sale.rest;

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

import com.sale.binding.SaleItem;
import com.sale.service.SaleItemService;




@RestController
@RequestMapping("/api")
public class SaleItemRestController {
	
	@Autowired
	private SaleItemService saleItemService;
	
	
	@PostMapping("/saleItem")
	public ResponseEntity<String> createSaleItems(@RequestBody List<SaleItem> saleItemList)
	{
		String status = saleItemService.saveSaleItems(saleItemList);
		return new ResponseEntity<> (status, HttpStatus.CREATED);
	}
	
	@GetMapping("/saleItems/{accountId}")
	public ResponseEntity<List<SaleItem>> getAllSaleItems(@PathVariable String accountId)
	{
		List<SaleItem> allSaleItems = saleItemService.getAllSaleItemsByAccountId(accountId);
		return new ResponseEntity<>(allSaleItems,HttpStatus.OK);
	}
	
	@GetMapping("/saleItems/{accountId}/{outletId}")
	public ResponseEntity<List<SaleItem>> getAllSaleItems(@PathVariable String accountId, @PathVariable String outletId)
	{
		List<SaleItem> allSaleItems = saleItemService.getAllSaleItemsByAccountId(accountId, outletId);
		return new ResponseEntity<>(allSaleItems,HttpStatus.OK);
	}
	
}
