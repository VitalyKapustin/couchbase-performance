package com.kapustin.couchbase.repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.BinaryDocument;
import com.couchbase.client.java.document.SerializableDocument;
import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 28, 2015.
 */
@Component
public class Transaction2GZIPRepository {
	
	@Autowired
	@Qualifier("saveBucket")
	private Bucket bucket;

	public Transaction2 save(Transaction2 doc) {		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
			ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
			objectOut.writeObject(doc);
			objectOut.close();
			byte[] bytes = baos.toByteArray();
			bucket.upsert(SerializableDocument.create(doc.getId(), bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return doc;
	}

	public Transaction2 findOne(String id) {
		Transaction2 transaction2 = null;
		try {
			SerializableDocument found = bucket.get(id, SerializableDocument.class);
			byte[] data = (byte[])found.content();
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			GZIPInputStream gzipIn;
			gzipIn = new GZIPInputStream(bais);
			ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
			transaction2 = (Transaction2)objectIn.readObject();		
			objectIn.close();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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