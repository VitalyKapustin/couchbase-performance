package com.kapustin.couchbase.utils;

import java.util.Date;
import java.util.Random;

import com.kapustin.couchbase.entity.Transaction;

/**
 * Created by v.kapustin on Aug 17, 2015.
 */
public class TransactionGenerator {

	public static Transaction generateTransaction() {
		Transaction transaction = new Transaction();
		
		Random rnd = new Random();		
		transaction.setAccount("Account_" + rnd.nextLong());
		transaction.setBank("Bank_" + rnd.nextLong());
		transaction.setDate(new Date().getTime());
		transaction.setActualDate(new Date().getTime());
		transaction.setType("type_" + rnd.nextLong());
		transaction.setAmount(rnd.nextDouble());
		transaction.setCode("code_" + rnd.nextLong());
		transaction.setArea("area_" + rnd.nextLong());
		transaction.setRegion("region_" + rnd.nextLong());
		transaction.setCountry("country_" + rnd.nextLong());
		
		return transaction;
	}
}