package com.kot.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kot.binding.KOT;
import com.kot.binding.KOTItem;
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
		for (KOT value : kotList) {
			KOT kot = kotRepo.findByAccountIdAndOutletIdAndKotId(value.getAccountId(),value.getOutletId(),value.getKotId());
            if (kot != null) {
                // Update existing item
            	kot.setKotId(value.getKotId());
    			kot.setAccountId(value.getAccountId());
    			kot.setOutletId(value.getOutletId());
    			kot.setKotOrder(value.getKotOrder());
    			kot.setOrderType(value.getOrderType());
    			kot.setTotalPrice(value.getTotalPrice());
    			kot.setComment(value.getComment());
    			kot.setCreated(value.getCreated());
    			kot.setTime(value.getTime());
    			kot.setStatus(value.isStatus());

    			kotRepo.save(kot);
            } else {
               
    			kotRepo.save(value);
            }
        }
		return "success";
	}

	@Override
	public List<KOT> getAllKOTsByAccountId(String accountId) {
		
		return kotRepo.findByAccountId(accountId);
	}

	@Override
	public List<KOT> getAllKOTsByAccountId(String accountId, String outletId) {
		// TODO Auto-generated method stub
		return kotRepo.findByAccountIdAndOutletIdAndCreated(accountId, outletId, LocalDate.now());
	}

}
