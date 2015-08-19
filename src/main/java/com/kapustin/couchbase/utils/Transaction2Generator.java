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
		transaction.setLookupField(new StringBuilder("lookupField_").append(index).toString());
		transaction.setData(new byte[dataSize]);
		return transaction;
	}	
}
