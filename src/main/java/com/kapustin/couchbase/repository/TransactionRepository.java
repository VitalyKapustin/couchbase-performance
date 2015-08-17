package com.kapustin.couchbase.repository;

import org.springframework.data.repository.CrudRepository;

import com.kapustin.couchbase.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, String> {
}