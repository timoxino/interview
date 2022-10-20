package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.ContainerRecord;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface BaseRepository<T extends ContainerRecord, ID extends Long> extends Neo4jRepository<T, ID> {
    <T extends ContainerRecord> Optional<T> findByName(String name);
}
