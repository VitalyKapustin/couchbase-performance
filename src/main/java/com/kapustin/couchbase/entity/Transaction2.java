package com.kapustin.couchbase.entity;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
@Document
public class Transaction2 extends GenericEntity {

	@Field
	private String lookupField;
	
	@Field
	private byte[] data;
	
	@Override
	public String toString() {		
        return new StringBuilder("transaction[id=").append(getId()).append(", lookupField=").append(lookupField).append("]").toString();
    }

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