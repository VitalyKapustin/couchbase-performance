package com.kapustin.couchbase.entity;

import org.springframework.data.couchbase.core.mapping.Document;

@Document
public class Transaction extends GenericEntity {

	private String account;
	private String bank;
	private Long date;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}	
}
