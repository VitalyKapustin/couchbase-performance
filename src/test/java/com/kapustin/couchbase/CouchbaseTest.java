package com.kapustin.couchbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import com.kapustin.couchbase.configuration.CouchbaseConfiguration;
import com.kapustin.couchbase.configuration.SpringConfiguration;
import com.kapustin.couchbase.entity.Transaction;
import com.kapustin.couchbase.repository.TransactionRepository;
import com.kapustin.couchbase.utils.TransactionGenerator;

/**
 * Created by v.kapustin on Aug 17, 2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
public class CouchbaseTest {

	private final static int COUNT = 50000;	
	
	@Autowired
	private TransactionRepository transactionRepository;	
	
	@Test	
	public void oneBulkInsertTest() throws InterruptedException {
		System.out.println("------------------- oneBulkInsertTest -------------------");
		List<Transaction> transactions = new ArrayList<>(COUNT);		
		for (int i = 0; i < COUNT; i++) {			
			Transaction transaction = TransactionGenerator.generateTransaction();		
			transactions.add(transaction);				
		}
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		transactionRepository.save(transactions);
		stopWatch.stop();						
		System.out.println(new StringBuilder("insert bulk of ").append(COUNT).append(" transactions").append("write time: ").append(stopWatch.getLastTaskTimeMillis()));
	}
	
	@Test
	public void loopInsertTest() {
		System.out.println("------------------- loopInsertTest -------------------");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {		
			Transaction transaction = TransactionGenerator.generateTransaction();
			transactionRepository.save(transaction);									
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert in loop of ").append(COUNT).append(" transactions").append("write time: ").append(stopWatch.getTotalTimeMillis()));
	}
	
	@Test
	public void lookupByRandomTransactionId() {
		System.out.println("------------------- lookupByRandomTransactionId -------------------");
		Random rnd = new Random(COUNT);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {		
			Transaction transaction = transactionRepository.findOne(String.valueOf(rnd.nextLong()));									
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup by random transaction ID ").append(COUNT).append(" transactions").append("average time: ").append((double)stopWatch.getTotalTimeMillis() / stopWatch.getTaskCount()));
	}
}