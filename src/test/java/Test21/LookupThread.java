package Test21;

import com.kapustin.couchbase.repository.Transaction2OneBucketRepository;
import com.kapustin.couchbase.repository.Transaction2TwoBucketRepository;

public class LookupThread implements Runnable {
	
	private String fromId;
	
	private String toId;

	private Transaction2OneBucketRepository transaction2OneBucketRepository;
	
	private Transaction2TwoBucketRepository transaction2TwoBucketRepository;
	
	public LookupThread(int fromId, int toId, Transaction2OneBucketRepository transaction2OneBucketRepository, Transaction2TwoBucketRepository transaction2TwoBucketRepository) {
		this.fromId = String.valueOf(fromId);
		this.toId = String.valueOf(toId);
		this.transaction2OneBucketRepository = transaction2OneBucketRepository;
		this.transaction2TwoBucketRepository = transaction2TwoBucketRepository;
	}

	@Override
	public void run() {
		
	}
}
