package com.timoxino.interview.web.service;

import com.timoxino.interview.web.dto.Selector;
import com.timoxino.interview.web.model.StoredRecord;
import com.timoxino.interview.web.repo.BaseRepository;
import com.timoxino.interview.web.repo.RepositoryProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SelectorService {

    private final RepositoryProvider repositoryProvider;

    public SelectorService(RepositoryProvider repositoryProvider) {
        this.repositoryProvider = repositoryProvider;
    }

    private Optional<StoredRecord> retrieveStoredParent(Selector parentSelector) {
        String parentType = parentSelector.getType();
        BaseRepository<? extends StoredRecord, Long> repository = repositoryProvider.provideRepository(parentType);
        return repository.findByName(parentSelector.getName());
    }

    public Optional<StoredRecord> retrieveStoredAncestor(Selector parentSelector) {
        Optional<StoredRecord> nullableParent = retrieveStoredParent(parentSelector);
        if (nullableParent.isEmpty()) {
            nullableParent = retrieveStoredAncestor(parentSelector.getBelongsTo().orElseThrow());
        }
        return nullableParent;
    }
}
