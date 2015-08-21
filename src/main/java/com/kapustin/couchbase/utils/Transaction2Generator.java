package com.kapustin.couchbase.utils;

import java.util.UUID;

import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.deps.io.netty.buffer.Unpooled;
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
			for (int i = 0; i < dataSize; i++) {
				data[i] = 127;
			}
		}
		transaction.setId(UUID.randomUUID().toString());
		transaction.setData(data);
		return transaction;
	}
	
	public static ByteBuf generate(int dataSize) {
		if (data == null || (data != null && data.length < dataSize)) {
			data = new byte[dataSize];
			for (int i = 0; i < dataSize; i++) {
				data[i] = 127;
			}			
		}
		return Unpooled.copiedBuffer(data);
	}
}
