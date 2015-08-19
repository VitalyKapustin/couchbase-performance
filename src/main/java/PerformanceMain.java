import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.kapustin.couchbase.configuration.ApplicationContextProvider;
import com.kapustin.couchbase.entity.Transaction1;
import com.kapustin.couchbase.repository.Transaction1Repository;
import com.kapustin.couchbase.utils.Transaction1Generator;

/**
 * Created by v.kapustin on Aug 18, 2015.
 */
public class PerformanceMain {

	@Autowired
	private Transaction1Repository transactionRepository;

	public static void main(String[] args) {
		PerformanceMain PerformanceMain = new PerformanceMain();
		PerformanceMain.run();
	}
	
	public void run() {
		ApplicationContextProvider.getInstance().autowireObject(this);
		transactionRepository.deleteAll();
		System.out.println("count: " + transactionRepository.count());		
//		for (Transaction transaction : transactionRepository.findAll()) {
//			System.out.println(transaction);
//		}
//		StopWatch stopWatch = new StopWatch();
//		for (int i = 0; i < 5; i++) {			
//			Transaction transaction = TransactionGenerator.generateTransaction(1024);						
//			stopWatch.start();
//			transaction.setId(transactionRepository.save(transaction).getId());
//			stopWatch.stop();
//			System.out.println(transaction);
//			
//			System.out.println("write time: " + stopWatch.getLastTaskTimeMillis());
//		}		
//		System.out.println("total write time: " + stopWatch.getTotalTimeMillis());
//		transactionRepository.deleteAll();		
//		System.out.println("count: " + transactionRepository.count());
	}	
}
