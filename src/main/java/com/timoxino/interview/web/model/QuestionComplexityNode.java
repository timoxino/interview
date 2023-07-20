package com.timoxino.interview.web.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Builder;
import lombok.Data;

@Node(primaryLabel = "QUESTION_COMPLEXITY_NODE")
@Builder
@Data
public class QuestionComplexityNode {

    @Id
    @GeneratedValue(generatorClass = UUIDGenerator.class)
    private UUID uuid;
    private Integer index;
    private String description;
    @Relationship(value = "GRADED_AS", direction = Relationship.Direction.INCOMING)
    private List<DataNode> questions;
}
