package com.kapustin.couchbase.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.BinaryDocument;
import com.couchbase.client.java.document.SerializableDocument;
import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
@Component
public class Transaction2Repository {
	
	@Autowired
	Bucket bucket;

	public Transaction2 save(Transaction2 doc) {
		bucket.upsert(SerializableDocument.create(doc.getId(), doc));
		return doc;
	}

	public Transaction2 findOne(String id) {
		Transaction2 transaction2 = null;
		SerializableDocument found = bucket.get(id, SerializableDocument.class);
		transaction2 = ((Transaction2) found.content());
		return transaction2;
	}

	public String save(ByteBuf doc) {
		String id = UUID.randomUUID().toString();
		bucket.upsert(BinaryDocument.create(id, doc));

		return id;
	}

	public BinaryDocument findBuff(String id) {

		BinaryDocument transaction2 = bucket.get(id, BinaryDocument.class);
		return transaction2;

	}
}
