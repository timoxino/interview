package com.timoxino.interview.web.service;

import com.timoxino.interview.web.dto.Selector;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SelectorService {

    private final DataNodeRepository dataNodeRepository;

    public SelectorService(DataNodeRepository dataNodeRepository) {
        this.dataNodeRepository = dataNodeRepository;
    }

    private Optional<DataNode> retrieveStoredParent(Selector parentSelector) {
        return dataNodeRepository.findByName(parentSelector.getName());
    }

    public Optional<DataNode> retrieveStoredAncestor(Selector parentSelector) {
        Optional<DataNode> nullableParent = retrieveStoredParent(parentSelector);
        if (nullableParent.isEmpty()) {
            nullableParent = retrieveStoredAncestor(parentSelector.getBelongsTo().orElseThrow());
        }
        return nullableParent;
    }

    /*public StoredRecord updateRecord(Selector selector) {
        DataNode storedAncestor = retrieveStoredAncestor(selector.getBelongsTo().orElseThrow()).orElseThrow();
        LinkedList<DataNode> container = new LinkedList<>();
        flatSelectors(selector, container);
        updateRecordGraph(storedAncestor, container);
        BaseRepository repository = repositoryProvider.provideRepository(storedAncestor.getClass().getDeclaredAnnotation(Node.class).primaryLabel());
        repository.save(storedAncestor);
        return storedAncestor;
    }

    private void updateRecordGraph(DataNode storedAncestor, LinkedList<DataNode> container) {
        DataNode activeParent = storedAncestor;
        for (DataNode record : container) {
            if (record.getName().equals(storedAncestor.getName())) {
                continue;
            }
            activeParent.addChild(record);
            activeParent = record;
        }
    }

    private void flatSelectors(Selector selector, LinkedList<DataNode> container) {
        container.addFirst(StoredDataNodeFactory.createRecord(selector));
        if(selector.getBelongsTo().isPresent()) flatSelectors(selector.getBelongsTo().get(), container);
    }*/
}
