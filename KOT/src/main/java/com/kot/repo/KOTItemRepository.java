package com.kot.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kot.binding.KOTItem;




public interface KOTItemRepository extends JpaRepository<KOTItem, Integer> {

	List<KOTItem> findByAccountId(String accountId);
	
}
