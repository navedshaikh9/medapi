package com.kot.service;

import java.util.List;

import com.kot.binding.KOT;



public interface KOTService {

	public String upsert(KOT kot);
	
	public KOT getById(Integer cid);
	
	public List<KOT> getAllKOTs();
	
	public String deleteById(Integer cid);
	
	public String saveKOTs(List<KOT> kotList);
	
	public List<KOT> getAllKOTsByAccountId(String accountId);
	
	public List<KOT> getAllKOTsByAccountId(String accountId, String outletId);
}
