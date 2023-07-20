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

@Node(primaryLabel = "QUESTION_CATEGORY_NODE")
@Builder
@Data
public class QuestionCategoryNode {

    @Id
    @GeneratedValue(generatorClass = UUIDGenerator.class)
    private UUID uuid;
    private String name;
    private String description;
    @Relationship(value = "CLASSIFIED_AS", direction = Relationship.Direction.INCOMING)
    private List<DataNode> questions;
}
