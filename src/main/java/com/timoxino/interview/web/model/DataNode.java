package com.timoxino.interview.web.model;

import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Node(primaryLabel = "DATA_NODE")
@Builder
@Data
@Jacksonized
public class DataNode {

    @Id
    @GeneratedValue(generatorClass = UUIDGenerator.class)
    private UUID uuid;
    private String name;
    private String description;
    private DataNodeType type;
    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private DataNode parent;
}
