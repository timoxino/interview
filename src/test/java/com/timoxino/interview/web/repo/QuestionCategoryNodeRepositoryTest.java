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

import com.timoxino.interview.web.model.QuestionCategoryNode;

@SpringBootTest
@RunWith(SpringRunner.class)
@Testcontainers
public class QuestionCategoryNodeRepositoryTest {

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
    QuestionCategoryNodeRepository repository;

    @Test
    void findByQuestion() {
        List<QuestionCategoryNode> categories = repository.findByQuestion("837a74d5-cfb4-4846-9db6-b04178aa6f6b");
        assertFalse(categories.isEmpty(), "Category list should not be empty");
        assertTrue(categories.size() == 1, "Category list should only has 1 element");
        assertTrue("Practical".equals(categories.get(0).getName()), "Wrong Question Category found");
        assertTrue(categories.get(0).getQuestions().size() == 4, "Unexpected number of questions of this Category");
    }
}
