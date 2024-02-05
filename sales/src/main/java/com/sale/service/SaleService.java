package com.sale.service;

import java.util.List;

import com.sale.binding.Sale;



public interface SaleService {

	public String upsert(Sale sale);
	
	public Sale getById(Integer cid);
	
	public List<Sale> getAllSales();
	
	public String deleteById(Integer cid);
	
	public String saveSales(List<Sale> saleList);
	
	public List<Sale> getAllSalesByAccountId(String accountId);
	
	public List<Sale> getAllSalesByAccountId(String accountId, String outletId);
}
