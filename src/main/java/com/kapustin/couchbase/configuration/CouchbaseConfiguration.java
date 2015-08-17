package com.kapustin.couchbase.configuration;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.convert.CustomConversions;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

/**
 * Created by v.kapustin on Aug 14, 2015.
 */
@Configuration
@PropertySource({ "classpath:couchbase.properties" })
@EnableCouchbaseRepositories("com.kapustin.couchbase.repository")
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

	@Value("${host}")
	private String host;
	
	@Value("${bucket.name}")
	private String bucketName;
	
	@Value("${bucket.password}")
	private String bucketPassword;
	
	@Override
	protected List<String> getBootstrapHosts() {		
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

	@Bean
	public PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean
	public MappingCouchbaseConverter mappingCouchbaseConverter() throws Exception {
		MappingCouchbaseConverter converter = new MappingCouchbaseConverter(couchbaseMappingContext(), typeKey());
		converter.setCustomConversions(new CustomConversions(new List<E>(new ) {
		}));
		return converter;
	}
}
