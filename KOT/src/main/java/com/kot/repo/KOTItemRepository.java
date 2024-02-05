package com.kot.repo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kot.binding.KOTItem;





public interface KOTItemRepository extends JpaRepository<KOTItem, Integer> {

	List<KOTItem> findByAccountId(String accountId);
		
	KOTItem findByAccountIdAndOutletIdAndKotItemId(String accountId, String outletId, int kotItemId);
	
	List<KOTItem> findByAccountIdAndOutletId(String accountId, String outletId);
	
	List<KOTItem> findByAccountIdAndOutletIdAndCreated(String accountId, String outletId, LocalDate created);;

}
