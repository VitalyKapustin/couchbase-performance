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
	
	private final static int COUNT = 5000;
	
	private final StopWatch stopWatch = new StopWatch();
	
	private final Random rnd = new Random();
	
	@Autowired
	private Transaction2Repository transaction2Repository;
	
	@Test
//	@Ignore
	public void stage1TestSuite() {
		runTests(1, 1024);
	}
	
	@Test
//	@Ignore
	public void stage2TestSuite() {
		runTests(2, 1024 * 20);
	}
	
	@Test
//	@Ignore
	public void stage3TestSuite() {
		runTests(3, 1024 * 50);
	}
	
	@Test
//	@Ignore
	public void stage4TestSuite() {
		runTests(4, 1024 * 70);
	}
	
	@Test
//	@Ignore
	public void stage5TestSuite() {
		runTests(5, 1024 * 100);
	}
	
	@Test
//	@Ignore
	public void stage6TestSuite() {
		runTests(6, 1024 * 200);
	}
	
	@Test
	@Ignore
	public void stage7TestSuite() {
		runTests(7, 1024 * 500);
	}
	
	private void runTests(int stageNum, int dataSize) {
		System.out.println("------------------- stage" + stageNum + "TestSuite -------------------");
		int offset = (stageNum - 1) * COUNT;
		insertTest(offset, dataSize);
		lookupTest(offset, dataSize);
//		lookupUsingN1QLTest(offset, dataSize);
		updateTest(offset, dataSize);
		deleteTest(offset, dataSize);
	}
		
	public void insertTest(int offset, int dataSize) {		
		System.out.println("------------------- insertTest -------------------");
		List<Transaction2> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction2Generator.generate(offset, i, dataSize));
		}
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			transaction2Repository.save(transactions.get(i));						
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}	
	
	public void lookupTest(int offset, int dataSize) {
		System.out.println("------------------- lookupTest -------------------");	
		List<String> ids = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			ids.add(getRandomId(offset));
		}
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction2 transaction = transaction2Repository.findOne(ids.get(i));
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
		
	public void lookupUsingN1QLTest(int offset, int dataSize) {
		System.out.println("------------------- lookupUsingN1QLTest -------------------");
		List<String> ids = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			ids.add(new StringBuilder("lookupField_").append(getRandomId(offset)).toString());
		}
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			List<Transaction2> transactions = transaction2Repository.lookupByLookupField(ids.get(i));			
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("retrieve using N1QL - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void updateTest(int offset, int dataSize) {
		System.out.println("------------------- updateTest -------------------");
		List<Transaction2> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction2Generator.generate(offset, i, dataSize));
		}
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {		
			Transaction2 transaction = transactions.get(i);
			transaction.setId(getRandomId(offset));
			transaction2Repository.save(transaction);
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("update transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void deleteTest(int offset, int dataSize) {
		System.out.println("------------------- deleteTest -------------------");
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {
			String id = getRandomId(offset);
			try {
				transaction2Repository.delete(id);
			} catch (DocumentDoesNotExistException ex) { }
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("delete transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	private String getRandomId(int offset) {
		return String.valueOf(rnd.nextInt(COUNT) + 1 + offset);
	}
}