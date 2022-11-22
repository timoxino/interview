package com.timoxino.interview.web.model;

public abstract class StoredContainerRecordFactory {

    public static ContainerRecord createRecord(String type, String name) {
        switch (type) {
            case "Role":
                return new Role(name);
            case "Category":
                return new Category(name);
            case "Occupation":
                return new Occupation(name);
            default:
                throw new IllegalArgumentException("Unsupported record type passed: " + type);
        }
    }
}
