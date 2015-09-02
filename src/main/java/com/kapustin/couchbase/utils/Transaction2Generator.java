package com.kapustin.couchbase.utils;

import java.util.Random;
import java.util.UUID;

import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.deps.io.netty.buffer.Unpooled;
import com.kapustin.couchbase.entity.Transaction2;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class Transaction2Generator {
		
	private static byte[] byteData;
	private static String stringData;
	
	public static Transaction2 generate(int offset, int index, int dataSize) {		
		Transaction2 transaction = new Transaction2();				
		if (byteData == null || (byteData != null && byteData.length < dataSize)) {
			byteData = new byte[dataSize];			
			new Random().nextBytes(byteData);						
		}
//		transaction.setData(byteData);		
		if (stringData == null || (stringData != null && stringData.length() < dataSize / 2)) {
			StringBuilder sb = new StringBuilder(dataSize / 2);
			final String alphabet = "0123456789ABCDE";
		    final int N = alphabet.length();

		    Random r = new Random();

		    for (int i = 0; i < dataSize / 2; i++) {
		        sb.append(alphabet.charAt(r.nextInt(N)));
		    }		    
		    stringData = sb.toString();
		}
		transaction.setData1(stringData);
//		transaction.setData2(stringData);
//		transaction.setData3(stringData);
//		transaction.setData4(stringData);
//		transaction.setData5(stringData);
//		transaction.setData6(stringData);
//		transaction.setData7(stringData);
//		transaction.setData8(stringData);
//		transaction.setData9(stringData);
//		transaction.setData10(stringData);
//		transaction.setData11(stringData);
//		transaction.setData12(stringData);
//		transaction.setData13(stringData);
//		transaction.setData14(stringData);
//		transaction.setData15(stringData);
//		transaction.setData16(stringData);
//		transaction.setData17(stringData);
//		transaction.setData18(stringData);
//		transaction.setData19(stringData);
//		transaction.setData20(stringData);
//		transaction.setData21(stringData);
//		transaction.setData22(stringData);
//		transaction.setData23(stringData);
//		transaction.setData24(stringData);
//		transaction.setData25(stringData);
//		transaction.setData26(stringData);
//		transaction.setData27(stringData);
//		transaction.setData28(stringData);
//		transaction.setData29(stringData);
//		transaction.setData30(stringData);
		
		transaction.setId(UUID.randomUUID().toString());
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
