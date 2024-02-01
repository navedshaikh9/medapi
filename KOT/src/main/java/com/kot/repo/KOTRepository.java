package com.kot.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kot.binding.KOT;



public interface KOTRepository extends JpaRepository<KOT, Integer> {

	List<KOT> findByAccountId(String accountId);
	KOT findByAccountIdAndOutletIdAndKotId(String accountId, String outletId,int KotId);
}
