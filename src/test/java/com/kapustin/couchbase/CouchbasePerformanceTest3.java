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

import com.kapustin.couchbase.configuration.CouchbaseConfiguration;
import com.kapustin.couchbase.configuration.SpringConfiguration;
import com.kapustin.couchbase.entity.Transaction3;
import com.kapustin.couchbase.repository.Transaction3CrudRepository;
import com.kapustin.couchbase.repository.Transaction3Repository;
import com.kapustin.couchbase.utils.Transaction3Generator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouchbasePerformanceTest3 {

private final static int COUNT = 3000;
	
	private final StopWatch stopWatch = new StopWatch();
	
	private final Random rnd = new Random();
	
	@Autowired
	private Transaction3CrudRepository transaction3CrudRepository;
	
	@Autowired
	private Transaction3Repository transaction3Repository;
	
	@Test
	@Ignore
	public void stage1TestSuite() {
		runTests(1, 1024);
	}
	
	@Test
	@Ignore
	public void stage2TestSuite() {
		runTests(2, 1024 * 20);
	}
	
	@Test
//	@Ignore
	public void stage3TestSuite() {
		runTests(3, 1024 * 100);
	}
	
	private void runTests(int stageNum, int dataSize) {
		System.out.println("------------------- stage" + stageNum + "TestSuite -------------------");
		int offset = (stageNum - 1) * COUNT;
		try {
//			insertLookupJsonTest(offset, dataSize);
			insertLookupSerializedTest(offset, dataSize);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void insertLookupJsonTest(int offset, int dataSize) throws InterruptedException {
		System.out.println("------------------- insertTest -------------------");
		List<Transaction3> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction3Generator.generate(offset, i, dataSize));
		}		
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			transaction3CrudRepository.save(transactions.get(i));						
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
		Thread.currentThread().sleep(30000);
		
		System.out.println("------------------- lookupTest -------------------");				
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction3 transaction = transaction3CrudRepository.findOne(transactions.get(i).getId());
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void insertLookupSerializedTest(int offset, int dataSize) throws InterruptedException {
		System.out.println("------------------- insertTest -------------------");
		List<Transaction3> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction3Generator.generate(offset, i, dataSize));
		}		
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			transaction3Repository.saveSerialized(transactions.get(i));						
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
		Thread.currentThread().sleep(30000);
		
		System.out.println("------------------- lookupTest -------------------");				
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction3 transaction = transaction3Repository.findOneSerialized((transactions.get(i).getId()));
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
}
