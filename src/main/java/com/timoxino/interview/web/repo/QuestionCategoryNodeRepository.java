package com.timoxino.interview.web.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.timoxino.interview.web.model.QuestionCategoryNode;

public interface QuestionCategoryNodeRepository extends QuestionAwareRepository<QuestionCategoryNode> {
    @Query("MATCH(category:QUESTION_CATEGORY_NODE) <-[:CLASSIFIED_AS]-(question:DATA_NODE)\n" + //
            "WHERE question.uuid = $questionUuid\n" + //
            "RETURN category")
    List<QuestionCategoryNode> findByQuestion(@Param("questionUuid") String questionUuid);
}
