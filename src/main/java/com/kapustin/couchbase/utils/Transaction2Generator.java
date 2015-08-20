package com.kapustin.couchbase.utils;

import java.util.Date;
import java.util.Random;

import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class Transaction2Generator {

	public static Transaction2 generate(int index, int dataSize) {
		Transaction2 transaction = new Transaction2();	
		transaction.setLookupField(new StringBuilder("lookupField_").append(index + 1).toString());
		Random rnd = new Random();
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(dataSize / 2);
		for (int i = 0; i < dataSize / 2; i++) {
			sb.append(abc.charAt(rnd.nextInt(26)));			
		}		
		transaction.setData(sb.toString());
		return transaction;
	}	
}
