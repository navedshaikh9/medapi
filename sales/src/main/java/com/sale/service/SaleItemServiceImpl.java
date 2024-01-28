package com.sale.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sale.binding.Sale;
import com.sale.binding.SaleItem;
import com.sale.repo.SaleItemRepository;
import com.sale.repo.SaleRepository;



@Service
public class SaleItemServiceImpl implements SaleItemService {
	
	@Autowired
	private SaleItemRepository saleItemRepo;

	@Override
	public String upsert(SaleItem kotItem) {
		
		saleItemRepo.save(kotItem); 
		
		return "success";
	}

	@Override
	public SaleItem getById(Integer cid) {
	Optional<SaleItem> findById = saleItemRepo.findById(cid);
	if(findById.isPresent())
	{
		return findById.get();
	}
		return null;
	}

	@Override
	public List<SaleItem> getAllSaleItems() {
		return saleItemRepo.findAll();
		
	}

	@Override
	public String deleteById(Integer cid) {
		if(saleItemRepo.existsById(cid))
		{
			saleItemRepo.deleteById(cid);
			return "Delete Success";
		}
		else
		{
			return "No Record Found";
		}
		
	}
	
	@Override
	public String saveSaleItems(List<SaleItem> saleItemsList) {
		saleItemRepo.saveAll(saleItemsList);
		return "success";
	}

	@Override
	public List<SaleItem> getAllSaleItemsByAccountId(String accountId) {
		
		return saleItemRepo.findByAccountId(accountId);
	}

}
