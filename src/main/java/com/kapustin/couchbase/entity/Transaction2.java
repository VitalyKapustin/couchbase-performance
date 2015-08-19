package com.kapustin.couchbase.entity;

import org.springframework.data.couchbase.core.mapping.Document;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
@Document
public class Transaction2 extends GenericEntity {

	private String lookupField;
	
	private byte[] data;

	public String getLookupField() {
		return lookupField;
	}

	public void setLookupField(String lookupField) {
		this.lookupField = lookupField;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}	
}
