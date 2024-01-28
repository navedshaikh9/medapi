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

import com.kot.binding.KOT;
import com.kot.service.KOTService;




@RestController
@RequestMapping("/api")
public class KOTRestController {
	
	@Autowired
	private KOTService kotService;
	
	@PostMapping("/kot")
	public ResponseEntity<String> createKOT(@RequestBody KOT kot)
	{
		String status = kotService.upsert(kot);
		return new ResponseEntity<> (status, HttpStatus.CREATED);
	}
	
	@PostMapping("/saveAllKOT")
	public ResponseEntity<String> createKOTs(@RequestBody List<KOT> kotList)
	{
		String status = kotService.saveKOTs(kotList);
		return new ResponseEntity<> (status, HttpStatus.CREATED);
	}
	
	@GetMapping("/allKOTs/{accountId}")
	public ResponseEntity<List<KOT>> getAllKOTs(@PathVariable String accountId)
	{
		List<KOT> allSaleItems = kotService.getAllKOTsByAccountId(accountId);
		return new ResponseEntity<>(allSaleItems,HttpStatus.OK);
	}
	
	@GetMapping("/kot/{cid}")
	public ResponseEntity<KOT> getKOT(@PathVariable Integer cid)
	{
		KOT saleItem = kotService.getById(cid);
		return new ResponseEntity<>(saleItem,HttpStatus.OK);
	}
	
	@GetMapping("/kot")
	public ResponseEntity<List<KOT>> getAllKOTs()
	{
		List<KOT> allKOTs = kotService.getAllKOTs();
		return new ResponseEntity<>(allKOTs,HttpStatus.OK);
	}
	
	@PutMapping("/kot")
	public ResponseEntity<String> updateKOT(@RequestBody KOT kot)
	{
		String status = kotService.upsert(kot);
		return new ResponseEntity<>(status,HttpStatus.OK);
	}
	
	@DeleteMapping("/kot/{cid}")
	public ResponseEntity<String> deleteKOT(@PathVariable Integer cid)
	{
		String status = kotService.deleteById(cid);
		return new ResponseEntity<>(status,HttpStatus.OK);
	}

}
