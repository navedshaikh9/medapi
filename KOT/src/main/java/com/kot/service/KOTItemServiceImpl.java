package com.kot.service;

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
		kotItemRepo.saveAll(kotItemsList);
		return "success";
	}

	@Override
	public List<KOTItem> getAllKOTItemsByAccountId(String accountId) {
		
		return kotItemRepo.findByAccountId(accountId);
	}

}
