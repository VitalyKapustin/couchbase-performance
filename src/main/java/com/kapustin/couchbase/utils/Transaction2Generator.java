package com.kapustin.couchbase.utils;

import java.util.UUID;

import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.deps.io.netty.buffer.Unpooled;
import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class Transaction2Generator {
		
	private static String[] data;
	private static byte[] byteData;
	
	public static Transaction2 generate(int offset, int index, int dataSize) {		
		Transaction2 transaction = new Transaction2();				
		if (data == null || (data != null && data.length < dataSize / 2)) {
			data = new String[dataSize / 2];
			for (int i = 0; i < dataSize / 2; i++) {
				data[i] = "b";
			}
		}
		transaction.setId(UUID.randomUUID().toString());
		transaction.setData(data);
		return transaction;
	}
	
	public static ByteBuf generate(int dataSize) {
		if (byteData == null || (byteData != null && byteData.length < dataSize)) {
			byteData = new byte[dataSize];
			for (int i = 0; i < dataSize; i++) {
				byteData[i] = 127;
			}			
		}
		return Unpooled.copiedBuffer(byteData);
	}
}
