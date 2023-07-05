package com.timoxino.interview.web.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;

import lombok.Builder;
import lombok.Data;

@Node(primaryLabel = "ROLE_NODE")
@Builder
@Data
public class RoleNode {

    @Id
    @GeneratedValue(generatorClass = UUIDGenerator.class)
    private UUID uuid;
    private String name;
    @Relationship(value = "HELD_BY", direction = Relationship.Direction.INCOMING)
    private List<DataNode> competencies;
}
