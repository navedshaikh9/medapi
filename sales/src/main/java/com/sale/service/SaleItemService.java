package com.sale.service;

import java.util.List;

import com.sale.binding.Sale;
import com.sale.binding.SaleItem;



public interface SaleItemService {

	public String upsert(SaleItem saleItem);
	
	public SaleItem getById(Integer cid);
	
	public List<SaleItem> getAllSaleItems();
	
	public String deleteById(Integer cid);
	
	public String saveSaleItems(List<SaleItem> saleItemList);
	
	public List<SaleItem> getAllSaleItemsByAccountId(String accountId);
}
