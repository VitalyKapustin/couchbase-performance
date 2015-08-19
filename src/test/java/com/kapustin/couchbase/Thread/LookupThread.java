package com.kapustin.couchbase.Thread;

import java.util.Map;
import java.util.Random;

import org.springframework.util.StopWatch;

import com.kapustin.couchbase.entity.Transaction;
import com.kapustin.couchbase.repository.TransactionRepository;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class LookupThread implements Runnable {
	
	private Integer threadNum;
	
	private TransactionRepository transactionRepository;
	
	private int count;	
	
	private Map<Integer, Double> timesMap;
	
	private LookupThread(TransactionRepository transactionRepository, int count) {
		this.transactionRepository = transactionRepository;
		this.count = count;
	}
	
	public LookupThread(TransactionRepository transactionRepository, int count, Map<Integer, Double> timesMap) {
		this(transactionRepository, count);
		this.timesMap = timesMap;
	}
	
	public LookupThread(Integer threadNum, TransactionRepository transactionRepository, int count, Map<Integer, Double> timesMap) {
		this(transactionRepository, count);
		this.threadNum = threadNum;
		this.timesMap = timesMap;
	}	

	@Override
	public void run() {
		Random rnd = new Random(count);
		StopWatch stopWatch = new StopWatch();					
		for (int i = 0; i < count; i++) {
			stopWatch.start();
			Transaction transaction = transactionRepository.findOne(String.valueOf(rnd.nextLong()));
			stopWatch.stop();
		}							
		timesMap.put(threadNum != null ? threadNum : ThreadType.LOOKUP.getOrderNum(), (double)stopWatch.getTotalTimeMillis() / (double)stopWatch.getTaskCount());		
	}
	
}