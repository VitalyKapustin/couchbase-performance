package com.kapustin.couchbase.configuration;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by v.kapustin on Aug 17, 2015.
 */
public class ApplicationContextProvider {

    private static transient volatile ApplicationContextProvider instance;

    private transient ConfigurableApplicationContext springContext;

    private ApplicationContextProvider() {
        springContext = new AnnotationConfigApplicationContext(SpringConfiguration.class, CouchbaseConfiguration.class);
    }

    public static ApplicationContextProvider getInstance() {
        if (instance == null) {
            synchronized (ApplicationContextProvider.class) {
                if (instance == null) {
                    instance = new ApplicationContextProvider();
                }
            }
        }
        return instance;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return springContext;
    }

    public <T> T getBean(Class<T> clazz) {
        return springContext.getBean(clazz);
    }

    public static void autowireObject(Object object) {
        getInstance().getApplicationContext().getAutowireCapableBeanFactory().autowireBean(object);
    }
}