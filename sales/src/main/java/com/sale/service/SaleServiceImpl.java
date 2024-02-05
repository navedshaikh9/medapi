package com.sale.service;

import java.time.LocalDate;
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
		
	//	saleRepo.saveAll(saleList);
		for (Sale value : saleList) {
            Sale sale = saleRepo.findByAccountIdAndOutletIdAndSaleId(value.getAccountId(),value.getOutletId(),value.getSaleId());
            if (sale != null) {
                // Update existing item
            	sale.setAccountId(value.getAccountId());
    			sale.setOutletId(value.getOutletId());
    			sale.setSaleId(value.getSaleId());
    			sale.setSaleOrder(value.getSaleOrder());
    			sale.setOrderType(value.getOrderType());
    			sale.setPayMethod(value.getPayMethod());
    			sale.setTotalAmt(value.getTotalAmt());
    			sale.setDiscountAmt(value.getDiscountAmt());
    			sale.setTaxPercent(value.getTaxPercent());
    			sale.setGrandTotal(value.getGrandTotal());
    			sale.setCreated(value.getCreated());
    			sale.setTime(value.getTime());
    			sale.setStatus(value.isStatus());
                saleRepo.save(sale);
            } else {
                // Create new item
            	
                saleRepo.save(value);
            }
        }
		return "success";
	}

	@Override
	public List<Sale> getAllSalesByAccountId(String accountId) {
		
		return saleRepo.findByAccountId(accountId);
	}

	@Override
	public List<Sale> getAllSalesByAccountId(String accountId, String outletId) {
		// TODO Auto-generated method stub
		return saleRepo.findByAccountIdAndOutletIdAndCreated(accountId, outletId, LocalDate.now());
	}

}
