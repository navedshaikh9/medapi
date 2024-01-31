package com.sale.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sale.binding.Sale;
import com.sale.binding.SaleItem;




public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

	List<SaleItem> findByAccountId(String accountId);
	SaleItem findByValue(SaleItem value);
}
