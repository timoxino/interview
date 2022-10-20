package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Node(primaryLabel = "CATEGORY")
public class Category extends ContainerRecord<SubCategory> {

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<SubCategory> subCategories;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @Override
    public void addChild(SubCategory subCategory) {
        if (CollectionUtils.isEmpty(subCategories)) {
            subCategories = new HashSet<>();
        }
        subCategories.add(subCategory);
    }
}
