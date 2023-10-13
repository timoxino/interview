package com.timoxino.interview.web.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import com.timoxino.interview.web.model.DataNode;

@SpringBootTest
@RunWith(SpringRunner.class)
@Testcontainers
public class DataNodeRepositoryTest {

    private static final int PM_QUESTIONS_NUMBER = 7;
    private static final int DEV_TOPICS_NUMBER = 5;
    private static final int DM_TOPICS_NUMBER = 8;
    private static final int PM_TOPICS_NUMBER = 3;

    private static final String NEO4J_DUMP = "neo4j.dump";

    @Container
    @ServiceConnection
    private static Neo4jContainer<?> container = new Neo4jContainer<>("neo4j:latest")
            .withCopyFileToContainer(MountableFile.forClasspathResource(NEO4J_DUMP),
                    "/var/lib/neo4j/data/dumps/neo4j.dump")
            .withCopyToContainer(Transferable.of("""
                    #!/bin/bash -eu
                    /var/lib/neo4j/bin/neo4j-admin database load neo4j
                    /startup/docker-entrypoint.sh neo4j
                    """, 0100555), "/startup/load-dump-and-start.sh")
            .withCommand("/startup/load-dump-and-start.sh")
            .withLogConsumer(f -> System.out.print(f.getUtf8String()));

    static {
        container.start();
    }

    @Autowired
    DataNodeRepository dataNodeRepository;

    @Autowired
    QuestionNodeRepository questionNodeRepository;

    @Test
    public void findTopicsByRoleName() {
        List<String> pmTopicNames = Arrays.asList("Initiation", "Execution", "Change Management");
        List<DataNode> pmTopics = dataNodeRepository.findTopicsByRoleName("Project Manager");
        assertEquals(PM_TOPICS_NUMBER, pmTopics.size(),
                "Number of topics for Project manager must be " + PM_TOPICS_NUMBER);
        pmTopics.forEach(topic -> assertTrue(pmTopicNames.contains(topic.getName()),
                "Unexpected topic in the list: " + topic.getName()));

        List<String> dmTopicNames = Arrays.asList("Initiation", "Execution", "Change Management", "Default method",
                "Optional", "Stream API", "LinkedList", "ArrayList");
        List<DataNode> dmTopics = dataNodeRepository.findTopicsByRoleName("Delivery Manager");
        assertEquals(DM_TOPICS_NUMBER, dmTopics.size(),
                "Number of topics for Delivery manager must be " + DM_TOPICS_NUMBER);
        dmTopics.forEach(topic -> assertTrue(dmTopicNames.contains(topic.getName()),
                "Unexpected topic in the list: " + topic.getName()));

        List<String> devTopicNames = Arrays.asList("Default method", "Optional", "Stream API", "LinkedList",
                "ArrayList");
        List<DataNode> devTopics = dataNodeRepository.findTopicsByRoleName("Java Software Engineer");
        assertEquals(DEV_TOPICS_NUMBER, devTopics.size(),
                "Number of topics for Java Software Engineer must be " + DEV_TOPICS_NUMBER);
        devTopics.forEach(topic -> assertTrue(devTopicNames.contains(topic.getName()),
                "Unexpected topic in the list: " + topic.getName()));
    }

    @Test
    public void findQuestionsByRoleName() {
        List<String> pmQuestionNames = Arrays.asList("Change Log", "Change Request", "Project Charter Content", "Stakeholder Management", "Change Control System", "Change Control Board (CCB)", "Metrics");
        List<DataNode> pmQuestions = questionNodeRepository.findQuestionsByRoleName("Project Manager");
        assertEquals(PM_QUESTIONS_NUMBER, pmQuestions.size(),
                "Number of questions for Project manager must be " + PM_QUESTIONS_NUMBER);
        pmQuestions.forEach(question -> assertTrue(pmQuestionNames.contains(question.getName()),
                "Unexpected question in the list: " + question.getName()));
    }

    @Test
    public void findByName() {
        assertNotNull(dataNodeRepository.findByName("Language Constructs").orElse(null), "Expected DataNode was not not found");
    }
}