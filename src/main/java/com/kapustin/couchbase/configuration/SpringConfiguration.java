package com.kapustin.couchbase.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by v.kapustin on Aug 14, 2015.
 */
@Configuration
@ComponentScan("com.kapustin.couchbase")
public class SpringConfiguration {

	@Bean
    public PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer() {
        return new PropertyPlaceholderConfigurer();
    }
}
