//package com.kapustin.couchbase;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.FixMethodOrder;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.util.StopWatch;
//
//import com.couchbase.client.java.error.DocumentDoesNotExistException;
//import com.kapustin.couchbase.Thread.DeleteThread;
//import com.kapustin.couchbase.Thread.InsertThread;
//import com.kapustin.couchbase.Thread.LookupThread;
//import com.kapustin.couchbase.Thread.ThreadType;
//import com.kapustin.couchbase.configuration.CouchbaseConfiguration;
//import com.kapustin.couchbase.configuration.SpringConfiguration;
//import com.kapustin.couchbase.entity.Transaction1;
//import com.kapustin.couchbase.repository.Transaction1Repository;
//import com.kapustin.couchbase.utils.Transaction1Generator;
//
///**
// * Created by v.kapustin on Aug 17, 2015.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class CouchbasePerformanceTest1 {
//
//	private final static int COUNT = 50000;
//	Random rnd = new Random();
//	
//	@Autowired
//	private Transaction1Repository transaction1Repository;	
//	
//	@Test
//	@Ignore
//	public void stage1OneBulkInsertTest() throws InterruptedException {
//		System.out.println("------------------- stage1OneBulkInsertTest -------------------");
//		List<Transaction1> transactions = new ArrayList<>(COUNT);		
//		for (int i = 0; i < COUNT; i++) {			
//			Transaction1 transaction = Transaction1Generator.generate();		
//			transactions.add(transaction);				
//		}
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		transaction1Repository.save(transactions);
//		stopWatch.stop();						
//		System.out.println(new StringBuilder("insert bulk of ").append(COUNT).append(" transactions write time: ").append(stopWatch.getTotalTimeMillis()));
//	}
//	
//	@Test
//	@Ignore
//	public void stage2LoopInsertTest() {
//		System.out.println("------------------- stage2LoopInsertTest -------------------");
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		for (int i = 0; i < COUNT; i++) {		
//			Transaction1 transaction = Transaction1Generator.generate();
//			transaction1Repository.save(transaction);									
//		}		
//		stopWatch.stop();
//		System.out.println(new StringBuilder("insert in loop of ").append(COUNT).append(" transactions write time: ").append(stopWatch.getTotalTimeMillis()));
//	}
//	
//	@Test	
//	@Ignore
//	public void stage3LookupByRandomTransactionIdTest() {
//		System.out.println("------------------- stage3LookupByRandomTransactionIdTest -------------------");		
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		for (int i = 0; i < COUNT; i++) {		
//			Transaction1 transaction = transaction1Repository.findOne(String.valueOf(rnd.nextInt(COUNT) + 1));									
//		}		
//		stopWatch.stop();
//		System.out.println(new StringBuilder("lookup by random transaction ID ").append(COUNT).append(" transactions average time: ").append((double)stopWatch.getTotalTimeMillis() / (double)COUNT));
//	}
//	
//	@Test	
//	@Ignore
//	public void stage4LookupByRandomTransactionIdIn10ThreadsTest() {
//		System.out.println("------------------- stage4LookupByRandomTransactionIdIn10ThreadsTest -------------------");
//		Map<Integer, Double> times = new ConcurrentHashMap<>();
//		
//		ExecutorService taskExecutor = Executors.newFixedThreadPool(10);	
//		for (int i = 0; i < 10; i++) {
//			taskExecutor.execute(new LookupThread(i, transaction1Repository, COUNT, times));			
//		}
//		taskExecutor.shutdown();
//		try {
//			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);			
//			for (int i = 0; i < 10; i++) {
//				System.out.println(new StringBuilder("lookup by random transaction ID in ").append(i + 1).append(" thread ").append(COUNT).append(" transactions average time for thread: ").append(times.get(i)));				
//			}			
//		} catch (InterruptedException e) {
//			System.out.println("Happened InterruptedException!!!");
//		}	
//	}
//	
//	@Test	
//	@Ignore
//	public void stage5ThreeThreadsInParalelTest() {
//		System.out.println("------------------- stage5ThreeThreadsInParalelTest -------------------");
//		Map<Integer, Double> times = new ConcurrentHashMap<>();
//		
//		ExecutorService taskExecutor = Executors.newFixedThreadPool(3);		
//		taskExecutor.execute(new InsertThread(transaction1Repository, COUNT, times));
//		taskExecutor.execute(new LookupThread(transaction1Repository, COUNT, times));
//		taskExecutor.execute(new DeleteThread(transaction1Repository, COUNT, times));
//		taskExecutor.shutdown();
//		try {
//			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
//			System.out.println(new StringBuilder("insert thread average insert one transaction of ").append(COUNT).append(" time: ").append(times.get(ThreadType.INSERT.getOrderNum())));
//			System.out.println(new StringBuilder("lookup thread average lookup one transaction of ").append(COUNT).append(" time: ").append(times.get(ThreadType.LOOKUP.getOrderNum())));
//			System.out.println(new StringBuilder("delete thread average delete one transaction of ").append(COUNT).append(" time: ").append(times.get(ThreadType.DELETE.getOrderNum())));
//		} catch (InterruptedException e) {
//			System.out.println("Happened InterruptedException!!!");
//		}
//	}
//	
//	@Test	
//	@Ignore
//	public void stage6DeleteByRandomTransactionIdTest() {
//		System.out.println("------------------- stage6DeleteByRandomTransactionIdTest -------------------");		
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		for (int i = 0; i < COUNT; i++) {
//			try {
//				transaction1Repository.delete(String.valueOf(rnd.nextInt(COUNT) + 1));				
//			} catch (DocumentDoesNotExistException ex) { }
//		}		
//		stopWatch.stop();
//		System.out.println(new StringBuilder("delete by random transaction ID ").append(COUNT).append(" transactions average time: ").append((double)stopWatch.getTotalTimeMillis() / (double)COUNT));
//	}	
//}