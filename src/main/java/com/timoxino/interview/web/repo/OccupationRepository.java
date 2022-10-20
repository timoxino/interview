package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.Occupation;

import java.util.List;

public interface OccupationRepository extends BaseRepository<Occupation, Long> {

    List<Occupation> findByRolesName(String name);
}
