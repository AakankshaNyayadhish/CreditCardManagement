package com.ofss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ofss.entity.Card;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByCustomerId(Long customerId);
}
