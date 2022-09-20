package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.Occupation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface OccupationRepository extends Neo4jRepository<Occupation, Long> {
    Occupation findByName(String name);

    List<Occupation> findByCategoriesName(String name);
}
