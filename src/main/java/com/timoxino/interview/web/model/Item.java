package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;

@Node(primaryLabel = "ITEM")
public class Item extends StoredRecord {

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }
}
