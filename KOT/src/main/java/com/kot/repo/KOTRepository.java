package com.kot.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kot.binding.KOT;
import com.kot.binding.KOTItem;


public interface KOTRepository extends JpaRepository<KOT, Integer> {

	List<KOT> findByAccountId(String accountId);
	KOT findByValue(KOT value);
}
