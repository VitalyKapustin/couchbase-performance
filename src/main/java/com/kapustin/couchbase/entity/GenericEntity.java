package com.kapustin.couchbase.entity;

import java.io.Serializable;

import com.couchbase.client.java.repository.annotation.Id;

public abstract class GenericEntity implements Serializable {

	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
