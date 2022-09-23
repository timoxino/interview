package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "SUB_CATEGORY")
public class SubCategory extends StoredRecord{

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private List<Item> items;

    public SubCategory() {
    }

    public SubCategory(String name) {
        this.name = name;
    }
}
