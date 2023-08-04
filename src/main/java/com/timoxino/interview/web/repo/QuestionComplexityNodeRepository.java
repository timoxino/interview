package com.timoxino.interview.web.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.timoxino.interview.web.model.QuestionComplexityNode;

public interface QuestionComplexityNodeRepository extends Neo4jRepository<QuestionComplexityNode, UUID> {
    @Query("MATCH(complexity:QUESTION_COMPLEXITY_NODE) <-[:GRADED_AS]-(question:DATA_NODE)\n" + //
    "WHERE question.uuid = $questionUuid\n" + //
    "RETURN complexity")
    List<QuestionComplexityNode> findComplexitiesByQuestion(@Param("questionUuid") String questionUuid);
}
