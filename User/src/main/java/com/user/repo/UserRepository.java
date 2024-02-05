package com.user.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.user.entity.User;



public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByAccountId(String accountId);
	
	User findByAccountIdAndPassword(String accountId, String password);

//	@Query("SELECT a.outletId FROM User a WHERE a.accountId = :accountId")
	@Query("SELECT outletId FROM User WHERE accountId = :accountId")
	List<String> getOutletIdByAccountId(String accountId);
	
}
