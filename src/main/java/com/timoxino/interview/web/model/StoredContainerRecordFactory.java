package com.timoxino.interview.web.model;

import com.timoxino.interview.web.dto.Selector;

public abstract class StoredContainerRecordFactory {

    public static ContainerRecord createRecord(Selector selector) {
        switch (selector.getType()) {
            case "Item":
                Item item = new Item(selector.getName());
                item.setDescription(selector.getDescription());
                return item;
            case "Role":
                return new Role(selector.getName());
            case "Category":
                return new Category(selector.getName());
            case "Occupation":
                return new Occupation(selector.getName());
            default:
                throw new IllegalArgumentException("Unsupported record type passed: " + selector.getType());
        }
    }
}
