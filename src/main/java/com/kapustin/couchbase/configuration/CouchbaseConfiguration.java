package com.kapustin.couchbase.configuration;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

/**
 * Created by v.kapustin on Aug 14, 2015.
 */
@Configuration
@PropertySource("classpath:couchbase.properties")
@EnableCouchbaseRepositories("com.kapustin.couchbase.repository")
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

	@Value("${couchbase.host}")
	private String host;

	@Value("${couchbase.bucket.name}")
	private String bucketName;

	@Value("${couchbase.bucket.password}")
	private String bucketPassword;

	@Bean
	public Bucket getBucket() {
		Cluster cluster = CouchbaseCluster.create(host);
		Bucket bucket = cluster.openBucket(bucketName, bucketPassword);
		return bucket;
	}

	@Override
	protected List<String> bootstrapHosts() {		
		return Collections.singletonList(host);
	}

	@Override
	protected String getBucketName() {		
		return bucketName;
	}

	@Override
	protected String getBucketPassword() {		
		return bucketPassword;
	}
}