package com.timoxino.interview.web.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface QuestionAwareRepository<T> extends Neo4jRepository<T, UUID> {
    List<T> findByQuestion(String questionUuid);
}
