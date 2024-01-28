package com.kot.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kot.binding.KOT;
import com.kot.repo.KOTRepository;



@Service
public class KOTServiceImpl implements KOTService {
	
	@Autowired
	private KOTRepository kotRepo;

	@Override
	public String upsert(KOT sale) {
		
		kotRepo.save(sale); 
		
		return "success";
	}

	@Override
	public KOT getById(Integer cid) {
	Optional<KOT> findById = kotRepo.findById(cid);
	if(findById.isPresent())
	{
		return findById.get();
	}
		return null;
	}

	@Override
	public List<KOT> getAllKOTs() {
		return kotRepo.findAll();
		
	}

	@Override
	public String deleteById(Integer cid) {
		if(kotRepo.existsById(cid))
		{
			kotRepo.deleteById(cid);
			return "Delete Success";
		}
		else
		{
			return "No Record Found";
		}
		
	}
	
	@Override
	public String saveKOTs(List<KOT> kotList) {
		kotRepo.saveAll(kotList);
		return "success";
	}

	@Override
	public List<KOT> getAllKOTsByAccountId(String accountId) {
		
		return kotRepo.findByAccountId(accountId);
	}

}
