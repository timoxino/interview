package com.timoxino.interview.web.service;

import com.timoxino.interview.web.dto.Selector;
import com.timoxino.interview.web.model.ContainerRecord;
import com.timoxino.interview.web.model.StoredContainerRecordFactory;
import com.timoxino.interview.web.model.StoredRecord;
import com.timoxino.interview.web.repo.BaseRepository;
import com.timoxino.interview.web.repo.RepositoryProvider;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.stereotype.Service;

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
        Optional<ContainerRecord> nullableAncestor = retrieveStoredAncestor(selector.getBelongsTo().orElseThrow());
        BaseRepository repository = repositoryProvider.provideRepository(nullableAncestor.orElseThrow().getClass().getDeclaredAnnotation(Node.class).primaryLabel());
        StoredRecord updatedGraph = buildRecordGraph(selector, nullableAncestor.orElseThrow());
        repository.save(updatedGraph);
        return updatedGraph;
    }

    private StoredRecord buildRecordGraph(Selector selector, ContainerRecord storedParent) {
        if (storedParent.getName().equals(selector.getName())) {
            return storedParent;
        }
        Selector selectorParent = selector.getBelongsTo().orElseThrow();
        String selectorParentName = selectorParent.getName();
        if (!storedParent.getName().equals(selectorParentName)) {
            buildRecordGraph(selectorParent, storedParent);
        } else {
            String selectorType = selector.getType();
            ContainerRecord newRecord = StoredContainerRecordFactory.createRecord(selectorType, selector.getName());
            storedParent.addChild(newRecord);
            buildRecordGraph(selector, newRecord);
        }
        return storedParent;
    }
}
