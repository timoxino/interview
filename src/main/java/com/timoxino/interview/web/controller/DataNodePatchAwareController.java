package com.timoxino.interview.web.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.timoxino.interview.web.dto.DataPatch;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.QuestionsAware;
import com.timoxino.interview.web.repo.DataNodeRepository;

public class DataNodePatchAwareController {

    private DataNodeRepository dataNodeRepository;

    public DataNodePatchAwareController(DataNodeRepository dataNodeRepository) {
        this.dataNodeRepository = dataNodeRepository;
    }

    public void executeOperation(DataPatch operation, QuestionsAware complexityNode) {
        LinkedHashMap<String, String> passedPatchValue = (LinkedHashMap) operation.getValue();
        Map<String, Object> patchValueMap = Collections.emptyMap();
        if ("/question".equals(operation.getPath())) {
            Optional<DataNode> question = dataNodeRepository
                    .findById(UUID.fromString((String) passedPatchValue.get("questionUuid")));
            patchValueMap = Collections.singletonMap("questionNode",
                    question.orElseThrow(ObjectNotFoundException::new));
        }
        operation.getOp().execute(complexityNode, patchValueMap);
    }
}
