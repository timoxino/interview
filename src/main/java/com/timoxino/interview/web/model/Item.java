package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;

@Node(primaryLabel = "ITEM")
public class Item extends ContainerRecord {

    private String description;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    @Override
    public void addChild(StoredRecord storedRecord) {
        throw new UnsupportedOperationException();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
