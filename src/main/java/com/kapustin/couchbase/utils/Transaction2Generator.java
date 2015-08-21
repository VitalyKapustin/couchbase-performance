package com.kapustin.couchbase.utils;

import java.util.Date;
import java.util.Random;

import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class Transaction2Generator {
	
	private static byte[] data;

	public static Transaction2 generate(int offset, int index, int dataSize) {
		Transaction2 transaction = new Transaction2();	
		transaction.setLookupField(new StringBuilder("lookupField_").append(offset + index + 1).toString());		
		if (data == null || (data != null && data.length < dataSize)) {
			data = new byte[dataSize];
		}
		transaction.setData(data);
		return transaction;
	}	
}
