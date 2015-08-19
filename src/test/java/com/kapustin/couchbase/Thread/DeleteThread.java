package com.kapustin.couchbase.Thread;

import java.util.Map;
import java.util.Random;

import org.springframework.util.StopWatch;

import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.kapustin.couchbase.repository.TransactionRepository;
import com.kapustin.couchbase.utils.TransactionGenerator;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class DeleteThread implements Runnable {
	
	private TransactionRepository transactionRepository;
	
	private int count;
	
	private Map<Integer, Double> times;
	
	public DeleteThread(TransactionRepository transactionRepository, int count, Map<Integer, Double> times) {
		this.transactionRepository = transactionRepository;
		this.count = count;
		this.times = times;
	}

	@Override
	public void run() {
		Random rnd = new Random();
		StopWatch stopWatch = new StopWatch();		
		for (int i = 0; i < count; i++) {		
			stopWatch.start();
			try {
				transactionRepository.delete(String.valueOf(rnd.nextInt(count) + 1));	
			} catch (DocumentDoesNotExistException ex) {}			
			stopWatch.stop();
		}					
		times.put(ThreadType.DELETE.getOrderNum(), (double)stopWatch.getTotalTimeMillis() / (double)stopWatch.getTaskCount());
	}
	
}