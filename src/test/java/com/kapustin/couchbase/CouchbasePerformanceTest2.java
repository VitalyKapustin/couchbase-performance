package com.kapustin.couchbase;

import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	public void stage2TestSuite() {
		runTests(1, 1024 * 20);
	}
	
	@Test
	public void stage3TestSuite() {
		runTests(1, 1024 * 50);
	}
	
	@Test
	public void stage4TestSuite() {
		runTests(1, 1024 * 70);
	}
	
	@Test
	public void stage5TestSuite() {
		runTests(1, 1024 * 100);
	}
	
	@Test
	public void stage6TestSuite() {
		runTests(1, 1024 * 200);
	}
	
	@Test
	public void stage7TestSuite() {
		runTests(1, 1024 * 500);
	}
	
	private void runTests(int stageNum, int dataSize) {
		System.out.println("------------------- stage" + stageNum + "TestSuite -------------------");
		insertTest(dataSize);		
		deleteTest(dataSize);
		lookupTest(dataSize);
		lookupUsingN1QLTest(dataSize);
	}
		
	public void insertTest(int dataSize) {
		System.out.println("------------------- stage1InsertTest -------------------");		
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			transaction2Repository.save(Transaction2Generator.generate(i, dataSize));						
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}	
		
	public void deleteTest(int dataSize) {
		System.out.println("------------------- stage3DeleteTest -------------------");
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			transaction2Repository.delete(String.valueOf(i + 1));			
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("delete transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void lookupTest(int dataSize) {
		System.out.println("------------------- stage2LookupTest -------------------");		
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction2 transaction = transaction2Repository.findOne(String.valueOf(i + 1));						
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
		
	public void lookupUsingN1QLTest(int dataSize) {
		System.out.println("------------------- stage4RetrieveUsingN1QLTest -------------------");
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {
			Transaction2 transaction = transaction2Repository.getByLookupField(new StringBuilder("lookupField_").append(rnd.nextInt(COUNT)).toString());
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("retrieve using N1QL - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
}