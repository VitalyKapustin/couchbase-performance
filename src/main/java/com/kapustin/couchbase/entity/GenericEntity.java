package com.kapustin.couchbase.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

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
