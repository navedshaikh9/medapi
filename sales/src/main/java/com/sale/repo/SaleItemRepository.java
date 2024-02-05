package com.sale.repo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sale.binding.SaleItem;




public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

	List<SaleItem> findByAccountId(String accountId);
	SaleItem findByAccountIdAndOutletIdAndSaleItemId(String accountId, String outletId, int SaleItemId);
	
	List<SaleItem> findByAccountIdAndOutletId(String accountId, String outletId);
	
	List<SaleItem> findByAccountIdAndOutletIdAndCreated(String accountId, String outletId,LocalDate created);
}
