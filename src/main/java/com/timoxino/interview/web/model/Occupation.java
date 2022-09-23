package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node(primaryLabel = "OCCUPATION")
public class Occupation extends StoredRecord{

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Category> categories;

    public Occupation() {
    }

    public Occupation(String name) {
        this.name = name;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
