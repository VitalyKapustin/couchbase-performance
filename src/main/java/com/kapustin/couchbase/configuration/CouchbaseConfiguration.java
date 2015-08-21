package com.kapustin.couchbase.configuration;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbaseDocument;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.mapping.model.MappingException;

import com.kapustin.couchbase.entity.Transaction2;

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

	public MappingCouchbaseConverter mappingCouchbaseConverter() throws Exception {
		MappingCouchbaseConverter converter = new MappingCouchbaseConverter(couchbaseMappingContext(), typeKey()) {

			@Override
			public void write(Object source, CouchbaseDocument target) {
				try {
					super.write(source, target);
				} catch (MappingException ex) {
					if (target.getId() == null) {
						target.setId(Long.toString(this.applicationContext.getBean(CouchbaseTemplate.class)
								.getCouchbaseBucket().counter("DOCUMENT_ID", 1, 1).content()));
					} else {
						throw ex;
					}
				}
			}
			
	
			@Override
			public <R> R read(Class<R> clazz, CouchbaseDocument source) {
				try {
					return super.read(clazz, source);
				} catch (ConversionFailedException ex) {
					if (source.get("_class").equals("com.kapustin.couchbase.entity.Transaction2")) {
						Transaction2 transaction = new Transaction2();
						transaction.setId(source.getId());
						transaction.setLookupField(source.get("lookupField").toString());
						transaction.setData(Base64.getDecoder().decode(source.get("data").toString().getBytes()));
						return (R) transaction;
					}
					throw ex;
				}
			}
		};

		converter.setCustomConversions(customConversions());
		return converter;
	}
}
