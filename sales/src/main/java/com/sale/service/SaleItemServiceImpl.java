package com.sale.service;

import java.time.LocalDate;
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
		for (SaleItem value : saleItemsList) {
			SaleItem saleItem = saleItemRepo.findByAccountIdAndOutletIdAndSaleItemId(value.getAccountId(),value.getOutletId(),value.getSaleItemId());
	        if (saleItem != null) {
                // Update existing item
            	saleItem.setAccountId(value.getAccountId());
    			saleItem.setOutletId(value.getOutletId());
    			saleItem.setSaleId(value.getSaleId());
    			saleItem.setItemName(value.getItemName());
    			saleItem.setQty(value.getQty());
    			saleItem.setPrice(value.getPrice());
    			saleItem.setItemTotal(value.getItemTotal());
    			saleItem.setCreated(value.getCreated());
    			saleItemRepo.save(saleItem);
            } else {
             	
    			saleItemRepo.save(value);
            }
        }
		return "success";
	}

	@Override
	public List<SaleItem> getAllSaleItemsByAccountId(String accountId) {
		
		return saleItemRepo.findByAccountId(accountId);
	}

	@Override
	public List<SaleItem> getAllSaleItemsByAccountId(String accountId, String outletId) {
		// TODO Auto-generated method stub
		return saleItemRepo.findByAccountIdAndOutletIdAndCreated(accountId, outletId, LocalDate.now());
	}

	
}
