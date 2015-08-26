package com.kapustin.couchbase.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.SerializableDocument;
import com.kapustin.couchbase.entity.Transaction3;

@Component
public class Transaction3Repository {

	@Autowired
	private Bucket bucket;
	
	public Transaction3 saveSerialized(Transaction3 doc) {
		bucket.upsert(SerializableDocument.create(doc.getId(), doc));
		return doc;
	}

	public Transaction3 findOneSerialized(String id) {
		Transaction3 transaction3 = null;
		SerializableDocument found = bucket.get(id, SerializableDocument.class);
		transaction3 = ((Transaction3) found.content());
		return transaction3;
	}
}
