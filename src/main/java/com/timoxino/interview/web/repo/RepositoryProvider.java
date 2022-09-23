package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.StoredRecord;
import org.springframework.stereotype.Component;

@Component
public final class RepositoryProvider {

    private final OccupationRepository occupationRepository;
    private final CategoryRepository categoryRepository;

    public RepositoryProvider(OccupationRepository occupationRepository, CategoryRepository categoryRepository) {
        this.occupationRepository = occupationRepository;
        this.categoryRepository = categoryRepository;
    }

    public BaseRepository<? extends StoredRecord, Long> provideRepository(String type) {
        switch (type) {
            case "Occupation":
                return occupationRepository;
            case "Category":
                return categoryRepository;
            default:
                throw new IllegalArgumentException("Unsupported repository type passed: " + type);
        }
    }
}
