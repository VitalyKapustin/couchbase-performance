package com.kapustin.couchbase.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.kapustin.couchbase.entity.Transaction3;

public class Transaction3Generator {

private static List<Map<String, String>> data;
	
	public static Transaction3 generate(int offset, int index, int dataSize) {		
		Transaction3 transaction = new Transaction3();		
		if (data == null || (data != null && data.size() < dataSize / 4)) {
			data = new ArrayList<>(dataSize / 4);
			for (int i = 0; i < dataSize / 4; i++) {
				Map<String, String> map = new HashMap<>();
				map.put("a", "b");
				data.add(map);
			}
		}
		transaction.setId(UUID.randomUUID().toString());
		transaction.setData(data);
		return transaction;
	}
}
