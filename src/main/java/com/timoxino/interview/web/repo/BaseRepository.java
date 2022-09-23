package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.StoredRecord;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface BaseRepository<T extends StoredRecord, ID extends Long> extends Neo4jRepository<T, ID> {
    <T extends StoredRecord> Optional<T> findByName(String name);
}
