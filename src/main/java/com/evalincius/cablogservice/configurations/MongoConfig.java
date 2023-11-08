package com.evalincius.cablogservice.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.evalincius.cablogservice.configurationProperties.MongoConfigurationProperties;
import com.mongodb.ConnectionString;
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
    public MongoTemplate mongoTemplate(MongoClient mongo) throws Exception {
        return new MongoTemplate(mongo, mongoConfig.getDatabase());
    }
}
