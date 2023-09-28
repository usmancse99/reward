package com.application.business.assessment.controller;
import com.application.business.assessment.model.Transaction;
import com.application.business.assessment.repository.TransactionRepository;
import com.application.business.assessment.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class RewardController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RewardService rewardService;

    @PostMapping("/add")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @GetMapping("/calculateRewards/{customerId}")
    public ResponseEntity<Integer> calculateRewardPoints(@PathVariable Long customerId) {
        // Fetch all transactions for the customer
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);

        // Calculate total reward points for the customer
        int totalPoints = transactions.stream()
                .map(transaction -> rewardService.calculateRewardPoints(transaction.getAmount()))
                .mapToInt(Integer::intValue)
                .sum();

        return new ResponseEntity<>(totalPoints, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        // Retrieve all transactions from the database
        List<Transaction> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
