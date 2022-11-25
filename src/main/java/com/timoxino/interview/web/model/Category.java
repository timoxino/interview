package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Node(primaryLabel = "CATEGORY")
public class Category extends ContainerRecord<ContainerRecord> {

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<ContainerRecord> subElement;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @Override
    public void addChild(ContainerRecord childElement) {
        if (CollectionUtils.isEmpty(subElement)) {
            subElement = new HashSet<>();
        }
        subElement.add(childElement);
    }
}
