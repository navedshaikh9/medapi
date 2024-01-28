package com.sale.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sale.binding.Sale;
import com.sale.repo.SaleRepository;



@Service
public class SaleServiceImpl implements SaleService {
	
	@Autowired
	private SaleRepository saleRepo;

	@Override
	public String upsert(Sale sale) {
		
		saleRepo.save(sale); 
		
		return "success";
	}

	@Override
	public Sale getById(Integer cid) {
	Optional<Sale> findById = saleRepo.findById(cid);
	if(findById.isPresent())
	{
		return findById.get();
	}
		return null;
	}

	@Override
	public List<Sale> getAllSales() {
		return saleRepo.findAll();
		
	}

	@Override
	public String deleteById(Integer cid) {
		if(saleRepo.existsById(cid))
		{
			saleRepo.deleteById(cid);
			return "Delete Success";
		}
		else
		{
			return "No Record Found";
		}
		
	}
	
	@Override
	public String saveSales(List<Sale> saleList) {
		saleRepo.saveAll(saleList);
		return "success";
	}

	@Override
	public List<Sale> getAllSalesByAccountId(String accountId) {
		
		return saleRepo.findByAccountId(accountId);
	}

}
