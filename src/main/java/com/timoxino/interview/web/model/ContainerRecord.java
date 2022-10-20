package com.timoxino.interview.web.model;

public abstract class ContainerRecord<T extends StoredRecord> extends StoredRecord {

    public abstract void addChild(T storedRecord);
}
