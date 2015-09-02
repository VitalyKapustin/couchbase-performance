package com.kapustin.couchbase.thread.test21;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import com.kapustin.couchbase.entity.Transaction2;
import com.kapustin.couchbase.repository.Transaction2OneBucketRepository;
import com.kapustin.couchbase.repository.Transaction2TwoBucketRepository;
import com.kapustin.couchbase.utils.Transaction2Generator;

public class InsertThread implements Runnable {

	private int count;
	
	private int dataSize;
	
	private int fromId;		
	
	private Transaction2OneBucketRepository transaction2OneBucketRepository;
	
	private Transaction2TwoBucketRepository transaction2TwoBucketRepository; 
	
	public InsertThread(int count, int dataSize, int fromId, Transaction2OneBucketRepository transaction2OneBucketRepository, Transaction2TwoBucketRepository transaction2TwoBucketRepository) {
		this.count = count;
		this.dataSize = dataSize;
		this.fromId = fromId;		
		this.transaction2OneBucketRepository = transaction2OneBucketRepository;
		this.transaction2TwoBucketRepository = transaction2TwoBucketRepository;
	}
	
	@Override
	public void run() {		
		List<Transaction2> transactions = new ArrayList(count);
		for (int i = fromId, index = 0; i < fromId + count; i++, index++) {			
			Transaction2 transaction2 = Transaction2Generator.generate(0, 0, dataSize);
			transaction2.setId(String.valueOf(i));
			transactions.add(transaction2);
		}
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < count; i++) {
			if (transaction2TwoBucketRepository != null) {
				transaction2TwoBucketRepository.save(transactions.get(i));
			} else {
				transaction2OneBucketRepository.save(transactions.get(i));
			}
		}
		stopWatch.stop();
		System.out.println("InsertThread time: " + (double)stopWatch.getNanoTime() / (double)count);		
	}
}
