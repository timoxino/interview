package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.DataNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataNodeRepository extends Neo4jRepository<DataNode, UUID> {
    Optional<DataNode> findByName(String name);

    @Query("MATCH (competencies:DATA_NODE) -[:HELD_BY]-> (role:ROLE_NODE {name: $roleName})\n" + //
            "MATCH (all_children:DATA_NODE) -[:BELONGS_TO*]-> (competencies)\n" + //
            "MATCH (topics:DATA_NODE {type: \"TOPIC\"}) -[:BELONGS_TO]-> (all_children)\n" + //
            "RETURN topics")
    List<DataNode> findTopicsByRoleName(@Param("roleName") String roleName);
}
