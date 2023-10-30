package com.ticket.Ticketing.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;


// To connect bucket

@Configuration
@EnableCouchbaseRepositories(basePackages = {"com.ticket.Ticketing"})
public class UserConfig extends AbstractCouchbaseConfiguration {

    private static final String NODE_LIST = "couchbase://127.0.0.1";
    private static final String BUCKET_NAME = "user_bucket";
    private static final String USERNAME = "adminuser";
    private static final String PASSWORD = "adminuser";


    @Override
    public String getConnectionString(){
        return NODE_LIST;
    }

    @Override
    public String getBucketName(){
        return BUCKET_NAME;
    }
    public static String getStaticBucketName(){
        return BUCKET_NAME;
    }

    @Override
    public String getUserName(){
        return USERNAME;
    }

    @Override
    public String getPassword(){
        return PASSWORD;
    }

}
