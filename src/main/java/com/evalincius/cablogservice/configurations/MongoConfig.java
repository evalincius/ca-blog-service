package com.evalincius.cablogservice.configurations;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.evalincius.cablogservice.configurationProperties.MongoConfigurationProperties;
import com.evalincius.cablogservice.configurations.mongoconverters.ZonedDateTimeReadConverter;
import com.evalincius.cablogservice.configurations.mongoconverters.ZonedDateTimeWriteConverter;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {
    @Autowired
    private MongoConfigurationProperties mongoConfig;

    @Bean
    @ConditionalOnProperty(name = "mongo.isAuthenticated", havingValue = "false")
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s", mongoConfig.getHost(), mongoConfig.getPort()));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @ConditionalOnProperty(name = "mongo.isAuthenticated", havingValue = "true")
    public MongoClient mongoAuthenticated() {
        MongoCredential credential = MongoCredential.createScramSha256Credential(mongoConfig.getUsername(), mongoConfig.getAuthenticationDb(), mongoConfig.getPassword().toCharArray());
        MongoClient mongoClient = MongoClients.create(
            MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress(mongoConfig.getAuthenticationDb(), mongoConfig.getPort()))))
                .credential(credential)
                .build());
        return mongoClient;
    }


    @Bean
    public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(
        Arrays.asList(
            new ZonedDateTimeReadConverter(),
            new ZonedDateTimeWriteConverter()));
    }


    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongo, MongoCustomConversions mongoCustomConversions) throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongo, mongoConfig.getDatabase());
        
        MappingMongoConverter conv = (MappingMongoConverter) mongoTemplate.getConverter();
        // tell mongodb to use the custom converters
        conv.setCustomConversions(mongoCustomConversions); 
        conv.afterPropertiesSet();
        
        return mongoTemplate;
    }
}
