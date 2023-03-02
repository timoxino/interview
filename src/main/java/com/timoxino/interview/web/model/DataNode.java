package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Node(primaryLabel = "DATA_NODE")
public class DataNode {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<DataNode> subNodes;

    public void addChild(DataNode subNode){
        if (CollectionUtils.isEmpty(subNodes)) {
            subNodes = new HashSet<>();
        }
        subNodes.add(subNode);
    }

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

    public Set<DataNode> getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(Set<DataNode> subNodes) {
        this.subNodes = subNodes;
    }
}
