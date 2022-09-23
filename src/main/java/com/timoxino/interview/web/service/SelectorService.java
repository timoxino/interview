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

    public Optional<StoredRecord> retrieveParent(Selector parentSelector) {
        String parentType = parentSelector.getType();
        BaseRepository<? extends StoredRecord, Long> repository = repositoryProvider.provideRepository(parentType);
        Optional<StoredRecord> parent = repository.findByName(parentSelector.getName());
        return parent;
    }
}
