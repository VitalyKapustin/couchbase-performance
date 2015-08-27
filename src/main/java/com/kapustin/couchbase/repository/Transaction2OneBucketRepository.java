package com.kapustin.couchbase.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class Transaction2OneBucketRepository {
	
	@Autowired
	@Qualifier("saveBucket")
	private Bucket saveBucket;	

	public Transaction2 save(Transaction2 doc) {
		saveBucket.upsert(SerializableDocument.create(doc.getId(), doc));
		return doc;
	}

	public Transaction2 findOne(String id) {
		Transaction2 transaction2 = null;
		SerializableDocument found = saveBucket.get(id, SerializableDocument.class);
		transaction2 = ((Transaction2) found.content());
		return transaction2;
	}

	public String save(ByteBuf doc) {
		String id = UUID.randomUUID().toString();
		saveBucket.upsert(BinaryDocument.create(id, doc));

		return id;
	}

	public BinaryDocument findBuff(String id) {

		BinaryDocument transaction2 = saveBucket.get(id, BinaryDocument.class);
		return transaction2;

	}
}
