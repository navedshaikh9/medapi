package com.sale.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sale.binding.Sale;


public interface SaleRepository extends JpaRepository<Sale, Integer> {

	List<Sale> findByAccountId(String accountId);
	
}
