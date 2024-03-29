package com.kot.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kot.binding.KOT;
import com.kot.binding.KOTItem;
import com.kot.repo.KOTItemRepository;
import com.kot.repo.KOTRepository;




@Service
public class KOTItemServiceImpl implements KOTItemService {
	
	@Autowired
	private KOTItemRepository kotItemRepo;

	@Override
	public String upsert(KOTItem kotItem) {
		
		kotItemRepo.save(kotItem); 
		
		return "success";
	}

	@Override
	public KOTItem getById(Integer cid) {
	Optional<KOTItem> findById = kotItemRepo.findById(cid);
	if(findById.isPresent())
	{
		return findById.get();
	}
		return null;
	}

	@Override
	public List<KOTItem> getAllKOTItems() {
		return kotItemRepo.findAll();
		
	}

	@Override
	public String deleteById(Integer cid) {
		if(kotItemRepo.existsById(cid))
		{
			kotItemRepo.deleteById(cid);
			return "Delete Success";
		}
		else
		{
			return "No Record Found";
		}
		
	}
	
	@Override
	public String saveKOTItems(List<KOTItem> kotItemsList) {
		for (KOTItem value : kotItemsList) {
			KOTItem kot_item = kotItemRepo.findByAccountIdAndOutletIdAndKotItemId(value.getAccountId(),value.getOutletId(),value.getKotItemId());
            if (kot_item != null) {
                // Update existing item
            	kot_item.setAccountId(value.getAccountId());
    			kot_item.setOutletId(value.getOutletId());
    			kot_item.setKotId(value.getKotId());
    			kot_item.setItemName(value.getItemName());
    			kot_item.setQty(value.getQty());
    			kot_item.setPrice(value.getPrice());
    			kot_item.setItemTotal(value.getItemTotal());
    			kot_item.setStatus(value.isStatus());
    			kot_item.setCancelStatus(value.isCancelStatus());
    			kot_item.setCreated(value.getCreated());
    			kotItemRepo.save(kot_item);
            } else {
                // Create new item
            
    			kotItemRepo.save(value);
            }
        }
		return "success";
	}

	@Override
	public List<KOTItem> getAllKOTItemsByAccountId(String accountId) {
		
		return kotItemRepo.findByAccountId(accountId);
	}

	@Override
	public List<KOTItem> getAllKOTItemsByAccountId(String accountId, String outletId) {
		// TODO Auto-generated method stub
		return kotItemRepo.findByAccountIdAndOutletIdAndCreated(accountId, outletId, LocalDate.now());
	}

}
