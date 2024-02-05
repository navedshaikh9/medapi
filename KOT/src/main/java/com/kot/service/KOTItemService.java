package com.kot.service;

import java.util.List;

import com.kot.binding.KOT;
import com.kot.binding.KOTItem;



public interface KOTItemService {

	public String upsert(KOTItem kotItem);
	
	public KOTItem getById(Integer cid);
	
	public List<KOTItem> getAllKOTItems();
	
	public String deleteById(Integer cid);
	
	public String saveKOTItems(List<KOTItem> kotItemList);
	
	public List<KOTItem> getAllKOTItemsByAccountId(String accountId);
	
	public List<KOTItem> getAllKOTItemsByAccountId(String accountId, String outletId);
}
