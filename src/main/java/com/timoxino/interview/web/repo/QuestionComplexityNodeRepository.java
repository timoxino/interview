package com.timoxino.interview.web.repo;

import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.timoxino.interview.web.model.QuestionComplexityNode;

public interface QuestionComplexityNodeRepository extends Neo4jRepository<QuestionComplexityNode, UUID> {
}
