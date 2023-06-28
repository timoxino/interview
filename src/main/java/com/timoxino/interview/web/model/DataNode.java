package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Builder;
import lombok.Data;

@Node(primaryLabel = "DATA_NODE")
@Builder
@Data
public class DataNode {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private DataNode parent;
}
