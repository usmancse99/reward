package com.application.business.assessment;

import com.application.business.assessment.model.Transaction;
import com.application.business.assessment.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TransactionRepository transactionRepository;

	@BeforeEach
	public void setUp() {
		transactionRepository.deleteAll();
	}

	@Test
	public void testAddTransaction() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setCustomerId(1L);
		transaction.setAmount(120.0);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/transactions/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"customerId\":1,\"amount\":120.0}")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

		List<Transaction> transactions = transactionRepository.findAll();
		assert transactions.size() == 1;
		assert transactions.get(0).getCustomerId().equals(1L);
		assert transactions.get(0).getAmount() == 120.0;
	}

	@Test
	public void testCalculateRewardPoints() throws Exception {
		Transaction transaction1 = new Transaction();
		transaction1.setCustomerId(1L);
		transaction1.setAmount(120.0);
		transactionRepository.save(transaction1);


		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/transactions/calculateRewards/1")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("90"));
	}

	@Test
	public void testEmptyTransactionList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/transactions/list")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
	}


}
