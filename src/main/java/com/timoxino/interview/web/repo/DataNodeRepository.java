package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.DataNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;
import java.util.UUID;

public interface DataNodeRepository extends Neo4jRepository<DataNode, UUID> {
    Optional<DataNode> findByName(String name);
}
