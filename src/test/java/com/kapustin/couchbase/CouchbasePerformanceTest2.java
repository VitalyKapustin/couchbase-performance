package com.kapustin.couchbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.kapustin.couchbase.configuration.CouchbaseConfiguration;
import com.kapustin.couchbase.configuration.SpringConfiguration;
import com.kapustin.couchbase.entity.Transaction2;
import com.kapustin.couchbase.repository.Transaction2Repository;
import com.kapustin.couchbase.utils.Transaction2Generator;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouchbasePerformanceTest2 {
	
	private final static int COUNT = 10000;
	
	private final StopWatch stopWatch = new StopWatch();
	
	private final Random rnd = new Random();
	
	@Autowired
	private Transaction2Repository transaction2Repository;
	
	@Test
	public void stage1TestSuite() {
		runTests(1, 1024);
	}
	
	@Test
	@Ignore
	public void stage2TestSuite() {
		runTests(1, 1024 * 20);
	}
	
	@Test
	@Ignore
	public void stage3TestSuite() {
		runTests(1, 1024 * 50);
	}
	
	@Test
	@Ignore
	public void stage4TestSuite() {
		runTests(1, 1024 * 70);
	}
	
	@Test
	@Ignore
	public void stage5TestSuite() {
		runTests(1, 1024 * 100);
	}
	
	@Test
	@Ignore
	public void stage6TestSuite() {
		runTests(1, 1024 * 200);
	}
	
	@Test
	@Ignore
	public void stage7TestSuite() {
		runTests(1, 1024 * 500);
	}
	
	private void runTests(int stageNum, int dataSize) {
		System.out.println("------------------- stage" + stageNum + "TestSuite -------------------");
		insertTest(dataSize);
		lookupTest(dataSize);
		lookupUsingN1QLTest(dataSize);
		updateTest(dataSize);
		deleteTest(dataSize);
	}
		
	public void insertTest(int dataSize) {		
		System.out.println("------------------- insertTest -------------------");
		List<Transaction2> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction2Generator.generate(i, dataSize));
		}
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			transaction2Repository.save(transactions.get(i));						
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}	
	
	public void lookupTest(int dataSize) {
		System.out.println("------------------- lookupTest -------------------");		
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction2 transaction = transaction2Repository.findOne(getRandomId());						
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
		
	public void lookupUsingN1QLTest(int dataSize) {
		System.out.println("------------------- lookupUsingN1QLTest -------------------");
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			List<Transaction2> transactions = transaction2Repository.lookupByLookupField(new StringBuilder("lookupField_").append(getRandomId()).toString());			
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("retrieve using N1QL - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void updateTest(int dataSize) {
		System.out.println("------------------- updateTest -------------------");
		List<Transaction2> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction2Generator.generate(i, dataSize));
		}
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {		
			Transaction2 transaction = transactions.get(i);
			transaction.setId(getRandomId());
			transaction2Repository.save(transaction);
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("update transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void deleteTest(int dataSize) {
		System.out.println("------------------- deleteTest -------------------");
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {
			String id = getRandomId();
			try {
				transaction2Repository.delete(id);
			} catch (DocumentDoesNotExistException ex) { }
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("delete transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	private String getRandomId() {
		return String.valueOf(rnd.nextInt(COUNT) + 1);
	}
}