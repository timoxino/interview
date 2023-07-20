package com.timoxino.interview.web.repo;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.timoxino.interview.web.model.QuestionCategoryNode;

public interface QuestionCategoryNodeRepository extends Neo4jRepository<QuestionCategoryNode, UUID> {
}
