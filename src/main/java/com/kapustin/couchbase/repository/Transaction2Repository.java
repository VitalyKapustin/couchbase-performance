package com.kapustin.couchbase.repository;

import java.util.List;

import org.springframework.data.couchbase.core.view.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
@Repository
public interface Transaction2Repository extends CrudRepository<Transaction2, String> {
			
	@Query("$SELECT_ENTITY$ WHERE lookupField = $1 AND $FILTER_TYPE$")
	public List<Transaction2> lookupByLookupField(String lookupFieldValue);	
}
