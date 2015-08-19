package com.kapustin.couchbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.kapustin.couchbase.Thread.DeleteThread;
import com.kapustin.couchbase.Thread.InsertThread;
import com.kapustin.couchbase.Thread.LookupThread;
import com.kapustin.couchbase.Thread.ThreadType;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouchbaseTest {

	private final static int COUNT = 50000;
	Random rnd = new Random(COUNT);
	
	@Autowired
	private TransactionRepository transactionRepository;	
	
	@Test	
	public void stage1OneBulkInsertTest() throws InterruptedException {
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
		System.out.println(new StringBuilder("insert bulk of ").append(COUNT).append(" transactions write time: ").append(stopWatch.getLastTaskTimeMillis()));
	}
	
	@Test	
	public void stage2LoopInsertTest() {
		System.out.println("------------------- loopInsertTest -------------------");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {		
			Transaction transaction = TransactionGenerator.generateTransaction();
			transactionRepository.save(transaction);									
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert in loop of ").append(COUNT).append(" transactions write time: ").append(stopWatch.getTotalTimeMillis()));
	}
	
	@Test	
	public void stage3LookupByRandomTransactionId() {
		System.out.println("------------------- lookupByRandomTransactionId -------------------");		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {		
			Transaction transaction = transactionRepository.findOne(String.valueOf(rnd.nextInt(COUNT) + 1));									
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup by random transaction ID ").append(COUNT).append(" transactions average time: ").append((double)stopWatch.getTotalTimeMillis() / (double)COUNT));
	}
	
	@Test	
	public void stage4LookupByRandomTransactionIdIn10Threads() {
		System.out.println("------------------- lookupByRandomTransactionIdIn10Threads -------------------");
		Map<Integer, Double> times = new ConcurrentHashMap<>();
		
		ExecutorService taskExecutor = Executors.newFixedThreadPool(10);	
		for (int i = 0; i < 10; i++) {
			taskExecutor.execute(new LookupThread(i, transactionRepository, COUNT, times));			
		}
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);			
			for (int i = 0; i < 10; i++) {
				System.out.println(new StringBuilder("lookup by random transaction ID in ").append(i + 1).append(" thread ").append(COUNT).append(" transactions average time for thread: ").append(times.get(i)));				
			}			
		} catch (InterruptedException e) {
			System.out.println("Happened InterruptedException!!!");
		}	
	}
	
	@Test	
	public void stage5ThreeThreadsInParalel() {
		System.out.println("------------------- ThreeThreadsInParalel -------------------");
		Map<Integer, Double> times = new ConcurrentHashMap<>();
		
		ExecutorService taskExecutor = Executors.newFixedThreadPool(3);		
		taskExecutor.execute(new InsertThread(transactionRepository, COUNT, times));
		taskExecutor.execute(new LookupThread(transactionRepository, COUNT, times));
		taskExecutor.execute(new DeleteThread(transactionRepository, COUNT, times));
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			System.out.println(new StringBuilder("insert thread average insert one transaction of ").append(COUNT).append(" time: ").append(times.get(ThreadType.INSERT.getOrderNum())));
			System.out.println(new StringBuilder("lookup thread average lookup one transaction of ").append(COUNT).append(" time: ").append(times.get(ThreadType.LOOKUP.getOrderNum())));
			System.out.println(new StringBuilder("delete thread average delete one transaction of ").append(COUNT).append(" time: ").append(times.get(ThreadType.DELETE.getOrderNum())));
		} catch (InterruptedException e) {
			System.out.println("Happened InterruptedException!!!");
		}
	}
	
	@Test	
	public void stage6DeleteByRandomTransactionId() {
		System.out.println("------------------- deleteByRandomTransactionId -------------------");		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {
			try {
				transactionRepository.delete(String.valueOf(rnd.nextInt(COUNT) + 1));				
			} catch (DocumentDoesNotExistException ex) { }
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("delete by random transaction ID ").append(COUNT).append(" transactions average time: ").append((double)stopWatch.getTotalTimeMillis() / (double)COUNT));
	}
}