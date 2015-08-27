package com.kapustin.couchbase.entity;

import java.io.Serializable;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public class Transaction2 implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	private String[] data;
	
	@Override
	public String toString() {		
        return new StringBuilder("transaction[id=").append(getId()).append("]").toString();
    }

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}				
}