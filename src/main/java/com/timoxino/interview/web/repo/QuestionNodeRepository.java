package com.timoxino.interview.web.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.timoxino.interview.web.model.DataNode;

@Repository
public interface QuestionNodeRepository extends Neo4jRepository<DataNode, UUID> {

    @Query("MATCH (competencies:DATA_NODE) -[:HELD_BY]-> (role:ROLE_NODE {name: $roleName})\n" + //
            "MATCH (all_children:DATA_NODE) -[:BELONGS_TO*]-> (competencies)\n" + //
            "MATCH (questions:DATA_NODE {type: \"QUESTION\"}) -[:BELONGS_TO]-> (all_children)\n" + //
            "RETURN questions")
    List<DataNode> findQuestionsByRoleName(@Param("roleName") String roleName);
}
