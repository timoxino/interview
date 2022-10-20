package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.Occupation;
import com.timoxino.interview.web.model.Role;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, Long> {

    List<Occupation> findByCategoriesName(String name);
}
