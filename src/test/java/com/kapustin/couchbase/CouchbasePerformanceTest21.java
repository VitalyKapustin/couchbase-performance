package com.kapustin.couchbase;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
import com.kapustin.couchbase.repository.Transaction2OneBucketRepository;
import com.kapustin.couchbase.repository.Transaction2TwoBucketRepository;
import com.kapustin.couchbase.thread.test21.InsertThread;
import com.kapustin.couchbase.thread.test21.LookupThread;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouchbasePerformanceTest21 {
	
	private final static int COUNT = 5000;
	
	private final StopWatch stopWatch = new StopWatch();
	
	private final Random rnd = new Random();
	
	@Autowired
	private Transaction2OneBucketRepository transaction2OneBucketRepository;
	
	@Autowired
	private Transaction2TwoBucketRepository transaction2TwoBucketRepository;	
	
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
	public void stage5TestSuite() {
		runTests(5, 1024 * 100);
	}	
	
	private void runTests(int stageNum, int dataSize) {
		System.out.println("------------------- stage" + stageNum + "TestSuite -------------------");
		int offset = (stageNum - 1) * COUNT;
		try {
			prepareDB(COUNT, dataSize);
//			insertLookupUsingOneBucketTest(dataSize);
			insertLookupUsingTwoBucketsTest(dataSize);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}		
	
	private void prepareDB(int count, int dataSize) {
		InsertThread insertThread = new InsertThread(COUNT, dataSize, 1, transaction2OneBucketRepository, null);
		ExecutorService taskExecutor = Executors.newFixedThreadPool(1);
		taskExecutor.execute(insertThread);
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			System.out.println("preapareDB is finished");
		} catch (InterruptedException e) { }
	}
	
	public void insertLookupUsingOneBucketTest(int dataSize) throws InterruptedException {
		System.out.println("------------------- insertLookupUsingOneBucketTest -------------------");
		LookupThread lookupThread = new LookupThread(COUNT, 1, transaction2OneBucketRepository, null);
		InsertThread insertThread = new InsertThread(COUNT, dataSize, 1, transaction2OneBucketRepository, null);
		ExecutorService taskExecutor = Executors.newFixedThreadPool(2);
		taskExecutor.execute(lookupThread);
		taskExecutor.execute(insertThread);
		taskExecutor.shutdown();
		taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
	}
	
	public void insertLookupUsingTwoBucketsTest(int dataSize) throws InterruptedException {
		System.out.println("------------------- insertLookupUsingTwoBucketsTest -------------------");
		LookupThread lookupThread = new LookupThread(COUNT, 1, transaction2OneBucketRepository,transaction2TwoBucketRepository);
		InsertThread insertThread = new InsertThread(COUNT, dataSize, 1, transaction2OneBucketRepository, transaction2TwoBucketRepository);
//		ExecutorService taskExecutor = Executors.newFixedThreadPool(2);	
		Thread t1 = new Thread(lookupThread);
		t1.start();
		t1.join();		
//		taskExecutor.execute(lookupThread);
//		taskExecutor.execute(insertThread);
//		taskExecutor.shutdown();
//		taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
	}
}