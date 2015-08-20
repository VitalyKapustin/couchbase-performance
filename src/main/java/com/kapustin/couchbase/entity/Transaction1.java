package com.kapustin.couchbase.entity;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

@Document
public class Transaction1 extends GenericEntity {

	@Field
	private String account;
	
	@Field
	private String bank;
	
	@Field
	private Long date;
	
	@Field
	private Long actualDate;
	
	@Field
	private String type;
	
	@Field
	private double amount;
	
	@Field
	private String code;
	
	@Field
	private String area;
	
	@Field
	private String region;
	
	@Field
	private String country;	
	
	@Override
	public String toString() {
		return new StringBuilder().append("transaction[id=").append(getId()).append(", account=").append(getAccount()).append(", bank=").append(getBank()).append(", date=").append(getDate()).append("]").toString();
	}

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

	public Long getActualDate() {
		return actualDate;
	}

	public void setActualDate(Long actualDate) {
		this.actualDate = actualDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	
}
