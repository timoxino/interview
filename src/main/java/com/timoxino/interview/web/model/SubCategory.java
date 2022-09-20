package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "SUB_CATEGORY")
public class SubCategory {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private List<Item> items;

    public SubCategory() {
    }

    public SubCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
