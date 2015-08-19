package com.kapustin.couchbase.Thread;

import java.util.Map;
import java.util.Random;

import org.springframework.util.StopWatch;

import com.kapustin.couchbase.repository.TransactionRepository;
import com.kapustin.couchbase.utils.TransactionGenerator;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class InsertThread implements Runnable {
	
	private TransactionRepository transactionRepository;
	
	private int count;
	
	private Map<Integer, Double> times;
	
	public InsertThread(TransactionRepository transactionRepository, int count, Map<Integer, Double> times) {
		this.transactionRepository = transactionRepository;
		this.count = count;
		this.times = times;
	}

	@Override
	public void run() {
		Random rnd = new Random(count);
		StopWatch stopWatch = new StopWatch();		
		for (int i = 0; i < count; i++) {		
			stopWatch.start();
			transactionRepository.save(TransactionGenerator.generateTransaction());
			stopWatch.stop();
		}					
		times.put(ThreadType.INSERT.getOrderNum(), (double)stopWatch.getTotalTimeMillis() / (double)stopWatch.getTaskCount());
	}
	
}