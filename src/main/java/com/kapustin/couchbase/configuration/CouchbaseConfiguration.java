package com.kapustin.couchbase.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

/**
 * Created by v.kapustin on Aug 14, 2015.
 */
@Configuration
@PropertySource("classpath:couchbase.properties")

public class CouchbaseConfiguration {

	@Value("${couchbase.host}")
	private String host;

	@Value("${couchbase.bucket.name}")
	private String bucketName;

	@Value("${couchbase.bucket.password}")
	private String bucketPassword;

	@Bean
	public Bucket getBootstrapHosts() {
		Cluster cluster = CouchbaseCluster.create(host);
		Bucket bucket = cluster.openBucket(bucketName, bucketPassword);
		return bucket;
	}

}
