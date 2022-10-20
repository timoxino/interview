package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Node(primaryLabel = "SUB_CATEGORY")
public class SubCategory extends ContainerRecord<Item> {

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Item> items;

    public SubCategory() {
    }

    public SubCategory(String name) {
        this.name = name;
    }

    @Override
    public void addChild(Item item) {
        if (CollectionUtils.isEmpty(items)) {
            items = new HashSet<>();
        }
        items.add(item);
    }
}
