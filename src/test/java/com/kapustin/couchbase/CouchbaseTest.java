package com.kapustin.couchbase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kapustin.couchbase.configuration.CouchbaseConfiguration;
import com.kapustin.couchbase.configuration.SpringConfiguration;
import com.kapustin.couchbase.repository.TransactionRepository;

/**
 * Created by v.kapustin on Aug 17, 2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class, CouchbaseConfiguration.class })
public class CouchbaseTest {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Test
	public void writeReadTest() {
		boolean b = false;
		System.out.println("test");
	}
}