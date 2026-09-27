package com.ofss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ofss.entity.CreditAccount;
import java.util.List;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount,Long>{
	List <CreditAccount> findByCustomerId(Long customerId);
}
