package com.sale.repo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sale.binding.Sale;


public interface SaleRepository extends JpaRepository<Sale, Integer> {

	List<Sale> findByAccountId(String accountId);
	Sale findByAccountIdAndOutletIdAndSaleId(String accountId, String outletId, int SaleId);
	List<Sale> findByAccountIdAndOutletId(String accountId,String outletId);
	List<Sale> findByAccountIdAndOutletIdAndCreated(String accountId,String outletId,LocalDate created);
}
