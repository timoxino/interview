package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.Category;
import com.timoxino.interview.web.model.Occupation;

import java.util.List;

public interface CategoryRepository extends BaseRepository<Category, Long> {

    List<Occupation> findByCategoriesName(String name);
}
