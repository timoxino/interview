package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Node(primaryLabel = "ROLE")
public class Role extends ContainerRecord<Category> {

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Category> categories;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public void addChild(Category category) {
        if (CollectionUtils.isEmpty(categories)) {
            categories = new HashSet<>();
        }
        categories.add(category);
    }
}
