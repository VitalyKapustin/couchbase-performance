//package com.kapustin.couchbase.utils;
//
//import java.util.Date;
//import java.util.Random;
//
//import com.kapustin.couchbase.entity.Transaction1;
//
///**
// * Created by v.kapustin on Aug 17, 2015.
// */
//public class Transaction1Generator {
//
//	public static Transaction1 generate() {
//		Transaction1 transaction = new Transaction1();
//		
//		Random rnd = new Random();		
//		transaction.setAccount("Account_" + rnd.nextLong());
//		transaction.setBank("Bank_" + rnd.nextLong());
//		transaction.setDate(new Date().getTime());
//		transaction.setActualDate(new Date().getTime());
//		transaction.setType("type_" + rnd.nextLong());
//		transaction.setAmount(rnd.nextDouble());
//		transaction.setCode("code_" + rnd.nextLong());
//		transaction.setArea("area_" + rnd.nextLong());
//		transaction.setRegion("region_" + rnd.nextLong());
//		transaction.setCountry("country_" + rnd.nextLong());
//		
//		return transaction;
//	}	
//}