package com.application.business.assessment.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.application.business.assessment.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerId(Long customerId);
}