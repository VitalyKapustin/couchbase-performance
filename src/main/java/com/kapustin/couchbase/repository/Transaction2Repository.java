package com.kapustin.couchbase.repository;

import org.springframework.data.repository.CrudRepository;

import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public interface Transaction2Repository extends CrudRepository<Transaction2, String> {
	
	public Transaction2 getByLookupField(String lookupFieldValue);
}
