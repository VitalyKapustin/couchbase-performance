package com.kapustin.couchbase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.java.document.BinaryDocument;
import com.kapustin.couchbase.configuration.CouchbaseConfiguration;
import com.kapustin.couchbase.configuration.SpringConfiguration;
import com.kapustin.couchbase.entity.Transaction2;
import com.kapustin.couchbase.repository.Transaction2GZIPRepository;
import com.kapustin.couchbase.repository.Transaction2Repository;
import com.kapustin.couchbase.utils.Transaction2Generator;

/**
 * Created by v.kapustin on Aug 28, 2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouchbasePerformanceTest6 {

	private final static int COUNT = 50000;
	
	private final StopWatch stopWatch = new StopWatch();
	
	@Autowired
	private Transaction2Repository transaction2Repository;
	
	@Autowired
	private Transaction2GZIPRepository transaction2GZIPRepository;
	
	@Test
//	@Ignore
	public void stage1TestSuite() {
		runTests(1, 1024);
	}
	
	@Test
	@Ignore
	public void stage2TestSuite() {
		runTests(2, 1024 * 20);
	}
	
	@Test
	@Ignore
	public void stage3TestSuite() {
		runTests(3, 1024 * 100);
	}
	
	private void runTests(int stageNum, int dataSize) {
		System.out.println("------------------- stage" + stageNum + "TestSuite -------------------");
		int offset = (stageNum - 1) * COUNT;
		try {
//			insertLookupTest(offset, dataSize);
			insertLookupGZipedTest(offset, dataSize);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void insertLookupTest(int offset, int dataSize) throws InterruptedException {
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
		Thread.currentThread().sleep(30000);
		
		System.out.println("------------------- lookupTest -------------------");				
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction2 transaction = transaction2Repository.findOne(transactions.get(i).getId());
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void insertLookupGZipedTest(int offset, int dataSize) throws InterruptedException {
		System.out.println("------------------- insertTest -------------------");
		List<Transaction2> transactions = new ArrayList<>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction2Generator.generate(offset, i, dataSize));
		}							
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {							
			transaction2GZIPRepository.save(transactions.get(i));
		}		
		stopWatch.stop();						
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));		
		stopWatch.reset();
		Thread.currentThread().sleep(30000);
		
		System.out.println("------------------- lookupTest -------------------");				
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			Transaction2 transaction = transaction2GZIPRepository.findOne(transactions.get(i).getId());
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
	
	public void insertLookupBinaryTest(int offset, int dataSize) throws InterruptedException {
		System.out.println("------------------- insertTest -------------------");
		List<ByteBuf> transactions = new ArrayList<ByteBuf>(COUNT);
		for (int i = 0; i < COUNT; i++) {
			transactions.add(Transaction2Generator.generate(dataSize));
		}
		List<String> ids = new ArrayList<>(COUNT);
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			ids.add(transaction2GZIPRepository.save(transactions.get(i)));						
		}		
		stopWatch.stop();
		System.out.println(new StringBuilder("insert transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));		
		stopWatch.reset();
		Thread.currentThread().sleep(30000);
		
		System.out.println("------------------- lookupTest -------------------");			
		stopWatch.start();
		for (int i = 0; i < COUNT; i++) {			
			BinaryDocument transaction = transaction2GZIPRepository.findBuff(ids.get(i));
		}
		stopWatch.stop();
		System.out.println(new StringBuilder("lookup transaction - data size: ").append(dataSize / 1024).append("kbytes, time: ").append((double)stopWatch.getNanoTime() / (double)COUNT));
		stopWatch.reset();
	}
}
