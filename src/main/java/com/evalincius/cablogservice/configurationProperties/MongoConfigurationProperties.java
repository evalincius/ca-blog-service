package com.evalincius.cablogservice.configurationProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@ConfigurationProperties(prefix = "mongo") 
@Configuration
@Data
public class MongoConfigurationProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private String authenticationDb;
}