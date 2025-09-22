package com.a7.job.resume.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Bean
    public CommandLineRunner initCollections(MongoTemplate mongoTemplate) {
        return args -> {
            if (!mongoTemplate.collectionExists("resumes")) {
                mongoTemplate.createCollection("resumes");
            }
            if (!mongoTemplate.collectionExists("experiences")) {
                mongoTemplate.createCollection("experiences");
            }
            if (!mongoTemplate.collectionExists("educations")) {
                mongoTemplate.createCollection("educations");
            }
        };
    }
}
