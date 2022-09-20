package com.timoxino.interview.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class InterviewWebAppApplication {

    private final static Logger log = LoggerFactory.getLogger(InterviewWebAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(InterviewWebAppApplication.class, args);
    }
}
