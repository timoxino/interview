package com.timoxino.interview.web.dto;

import java.util.Optional;

public class Selector {
    private String type;
    private String name;
    private Optional<Selector> belongsTo = Optional.empty();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Selector> getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Selector belongsTo) {
        this.belongsTo = Optional.ofNullable(belongsTo);
    }
}
