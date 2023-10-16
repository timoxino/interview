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

    @Query("MATCH (competencies:DATA_NODE) -[:HELD_BY]-> (role:ROLE_NODE {name: $roleName})\n" + //
            "MATCH (children_nodes:DATA_NODE) -[:BELONGS_TO*]-> (competencies)\n" + //
            "MATCH (questions:DATA_NODE {type: \"QUESTION\"}) -[:BELONGS_TO]-> (children_nodes)\n" + //
            "MATCH (questions) -[:GRADED_AS]-> (complexity:QUESTION_COMPLEXITY_NODE)\n" + //
            "WHERE complexity.index = $complexity\n" + //
            "MATCH(questions) -[:CLASSIFIED_AS]-> (category:QUESTION_CATEGORY_NODE {name: $category})\n" + //
            "MATCH(topic) <-[belongs:BELONGS_TO]- (questions)\n" + //
            "RETURN questions, collect(belongs), collect(topic)")
    List<DataNode> findQuestionsByRoleNameAndCategoryAndComplexity(@Param("roleName") String roleName,
            @Param("category") String category, @Param("complexity") Integer complexity);
}
