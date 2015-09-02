package com.kapustin.couchbase.thread.test21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;

import com.kapustin.couchbase.entity.Transaction2;
import com.kapustin.couchbase.repository.Transaction2OneBucketRepository;
import com.kapustin.couchbase.repository.Transaction2TwoBucketRepository;

public class LookupThread implements Runnable {
	
	private Random rnd = new Random();
	
	private int count;
	
	private int fromId;

	private Transaction2OneBucketRepository transaction2OneBucketRepository;
	
	private Transaction2TwoBucketRepository transaction2TwoBucketRepository;
	
	public LookupThread(int count, int fromId, Transaction2OneBucketRepository transaction2OneBucketRepository, Transaction2TwoBucketRepository transaction2TwoBucketRepository) {
		this.count = count;
		this.fromId = fromId;		
		this.transaction2OneBucketRepository = transaction2OneBucketRepository;
		this.transaction2TwoBucketRepository = transaction2TwoBucketRepository;
	}

	@Override
	public void run() {		
		List<String> ids = new ArrayList<>(count);		
		for (int i = 0; i < count; i++) {
			ids.add(String.valueOf(i + 1));
		}
		Collections.shuffle(ids);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < count; i++) {
			if (transaction2TwoBucketRepository != null) {
				Transaction2 transaction2 = transaction2TwoBucketRepository.findOne(ids.get(i));
			} else {
				Transaction2 transaction2 = transaction2OneBucketRepository.findOne(ids.get(i));
			}
		}
		stopWatch.stop();
		System.out.println("LookupThread time: " + (double)stopWatch.getNanoTime() / (double)count);
	}
}
