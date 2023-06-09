package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Builder;

@Node(primaryLabel = "DATA_NODE")
@Builder
public class DataNode {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private DataNode parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataNode getParent() {
        return parent;
    }

    public void setParent(DataNode parent) {
        this.parent = parent;
    }
}
