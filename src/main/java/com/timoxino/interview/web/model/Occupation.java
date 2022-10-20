package com.timoxino.interview.web.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Node(primaryLabel = "OCCUPATION")
public class Occupation extends ContainerRecord<Role> {

    @Relationship(value = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<Role> roles;

    public Occupation() {
    }

    public Occupation(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public void addChild(Role role) {
        if (CollectionUtils.isEmpty(roles)) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }
}
