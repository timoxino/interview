package com.timoxino.interview.web.repo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
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

import com.timoxino.interview.web.model.QuestionComplexityNode;

@SpringBootTest
@RunWith(SpringRunner.class)
@Testcontainers
public class QuestionComplexityNodeRepositoryTest {

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
    QuestionComplexityNodeRepository repository;

    @Test
    void findByQuestion() {
        List<QuestionComplexityNode> complexities = repository.findByQuestion("3731c1bc-45dc-4f7b-9801-440002020cb1");
        assertFalse(complexities.isEmpty(), "Complexity list should not be empty");
        assertTrue(complexities.size() == 1, "Complexity list should only has 1 element");
        assertTrue("Easy question".equals(complexities.get(0).getDescription()), "Wrong Question Complexity found");
        assertTrue(complexities.get(0).getQuestions().size() == 5, "Unexpected number of questions of this Complexity");
    }
}
