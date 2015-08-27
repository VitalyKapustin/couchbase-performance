package com.kapustin.couchbase.thread;

/**
 * Created by v.kapustin on Aug 19, 2015.
 */
public enum ThreadType {

	INSERT(0), LOOKUP(1), DELETE(2);
	
	private Integer orderNum;
	
	private ThreadType(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getOrderNum() {
		return orderNum;
	}	
}