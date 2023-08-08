package com.timoxino.interview.web.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.timoxino.interview.web.model.QuestionCategoryNode;

public interface QuestionCategoryNodeRepository extends QuestionAwareRepository<QuestionCategoryNode> {
    @Query("MATCH(category:QUESTION_CATEGORY_NODE)<-[:CLASSIFIED_AS]-(:DATA_NODE {uuid: $questionUuid})\n" + //
            "MATCH(category)-[graded:CLASSIFIED_AS]-(question:DATA_NODE)-[belongs:BELONGS_TO*]->(topic:DATA_NODE)\n" + //
            "OPTIONAL MATCH (topic)-[held:HELD_BY]->(role:ROLE_NODE)\n" + //
            "RETURN category, collect(graded), collect(question), collect(belongs), collect(topic), collect(held), collect(role)")
    List<QuestionCategoryNode> findByQuestion(@Param("questionUuid") String questionUuid);
}
