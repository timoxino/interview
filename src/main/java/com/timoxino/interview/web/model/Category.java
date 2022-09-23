package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node(primaryLabel = "CATEGORY")
public class Category extends StoredRecord {

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Category> categories;

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<SubCategory> subCategories;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
}
