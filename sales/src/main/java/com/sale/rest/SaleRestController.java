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

import com.sale.binding.Sale;
import com.sale.service.SaleService;




@RestController
@RequestMapping("/api")
public class SaleRestController {
	
	@Autowired
	private SaleService saleService;
	
	
	
	@PostMapping("/sale")
	public ResponseEntity<String> createKOTs(@RequestBody List<Sale> kotList)
	{
		String status = saleService.saveSales(kotList);
		return new ResponseEntity<> (status, HttpStatus.CREATED);
	}
	
	@GetMapping("/sales/{accountId}")
	public ResponseEntity<List<Sale>> getAllKOTs(@PathVariable String accountId)
	{
		List<Sale> allSaleItems = saleService.getAllSalesByAccountId(accountId);
		return new ResponseEntity<>(allSaleItems,HttpStatus.OK);
	}
	
	@GetMapping("/sales/{accountId}/{outletId}")
	public ResponseEntity<List<Sale>> getAllKOTs(@PathVariable String accountId,@PathVariable String outletId)
	{
		List<Sale> allSaleItems = saleService.getAllSalesByAccountId(accountId, outletId);
		return new ResponseEntity<>(allSaleItems,HttpStatus.OK);
	}
	
	

}
