package com.timoxino.interview.web.service;

import com.timoxino.interview.web.dto.Selector;
import com.timoxino.interview.web.model.ContainerRecord;
import com.timoxino.interview.web.model.StoredContainerRecordFactory;
import com.timoxino.interview.web.model.StoredRecord;
import com.timoxino.interview.web.repo.BaseRepository;
import com.timoxino.interview.web.repo.RepositoryProvider;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class SelectorService {

    private final RepositoryProvider repositoryProvider;

    public SelectorService(RepositoryProvider repositoryProvider) {
        this.repositoryProvider = repositoryProvider;
    }

    private Optional<ContainerRecord> retrieveStoredParent(Selector parentSelector) {
        String parentType = parentSelector.getType();
        BaseRepository<? extends StoredRecord, Long> repository = repositoryProvider.provideRepository(parentType);
        return repository.findByName(parentSelector.getName());
    }

    public Optional<ContainerRecord> retrieveStoredAncestor(Selector parentSelector) {
        Optional<ContainerRecord> nullableParent = retrieveStoredParent(parentSelector);
        if (nullableParent.isEmpty()) {
            nullableParent = retrieveStoredAncestor(parentSelector.getBelongsTo().orElseThrow());
        }
        return nullableParent;
    }

    public StoredRecord updateRecord(Selector selector) {
        ContainerRecord storedAncestor = retrieveStoredAncestor(selector.getBelongsTo().orElseThrow()).orElseThrow();
        LinkedList<ContainerRecord> container = new LinkedList<>();
        flatSelectors(selector, container);
        updateRecordGraph(storedAncestor, container);
        BaseRepository repository = repositoryProvider.provideRepository(storedAncestor.getClass().getDeclaredAnnotation(Node.class).primaryLabel());
        repository.save(storedAncestor);
        return storedAncestor;
    }

    private void updateRecordGraph(ContainerRecord storedAncestor, LinkedList<ContainerRecord> container) {
        ContainerRecord activeParent = storedAncestor;
        for (ContainerRecord record : container) {
            if (record.getName().equals(storedAncestor.getName())) {
                continue;
            }
            activeParent.addChild(record);
            activeParent = record;
        }
    }

    private void flatSelectors(Selector selector, LinkedList<ContainerRecord> container) {
        container.addFirst(StoredContainerRecordFactory.createRecord(selector));
        if(selector.getBelongsTo().isPresent()) flatSelectors(selector.getBelongsTo().get(), container);
    }
}
