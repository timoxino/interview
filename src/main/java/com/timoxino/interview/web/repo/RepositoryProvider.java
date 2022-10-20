package com.timoxino.interview.web.repo;

import org.springframework.stereotype.Component;

@Component
public final class RepositoryProvider {

    private final OccupationRepository occupationRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;

    public RepositoryProvider(OccupationRepository occupationRepository, CategoryRepository categoryRepository, RoleRepository roleRepository) {
        this.occupationRepository = occupationRepository;
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
    }

    public BaseRepository provideRepository(String type) {
        switch (type.toUpperCase()) {
            case "OCCUPATION":
                return occupationRepository;
            case "ROLE":
                return roleRepository;
            case "CATEGORY":
                return categoryRepository;
            default:
                throw new IllegalArgumentException("Unsupported repository type passed: " + type);
        }
    }
}
