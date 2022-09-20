package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node(primaryLabel = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Category> categories;

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<SubCategory> subCategories;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
