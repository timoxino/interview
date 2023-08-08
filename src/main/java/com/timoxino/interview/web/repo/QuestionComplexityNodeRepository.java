package com.timoxino.interview.web.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.timoxino.interview.web.model.QuestionComplexityNode;

public interface QuestionComplexityNodeRepository extends QuestionAwareRepository<QuestionComplexityNode> {
    @Query("MATCH(complexity:QUESTION_COMPLEXITY_NODE)<-[:GRADED_AS]-(:DATA_NODE {uuid: $questionUuid})\n" + //
            "MATCH(complexity)-[graded:GRADED_AS]-(question:DATA_NODE)-[belongs:BELONGS_TO*]->(topic:DATA_NODE)\n" + //
            "OPTIONAL MATCH (topic)-[held:HELD_BY]->(role:ROLE_NODE)\n" + //
            "RETURN complexity, collect(graded), collect(question), collect(belongs), collect(topic), collect(held), collect(role)")
    List<QuestionComplexityNode> findByQuestion(@Param("questionUuid") String questionUuid);
}
