package com.timoxino.interview.web.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatusCode;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.timoxino.interview.web.dto.QuestionComplexityPatch;
import com.timoxino.interview.web.dto.QuestionComplexityPatchRequest;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.QuestionComplexityNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import com.timoxino.interview.web.repo.QuestionComplexityNodeRepository;

@RestController
@RequestMapping("data/question/complexity")
public class QuestionComplexityController {

    private QuestionComplexityNodeRepository questionComplexityNodeRepository;
    private DataNodeRepository dataNodeRepository;

    public QuestionComplexityController(QuestionComplexityNodeRepository questionComplexityNodeRepository,
            DataNodeRepository dataNodeRepository) {
        this.questionComplexityNodeRepository = questionComplexityNodeRepository;
        this.dataNodeRepository = dataNodeRepository;
    }

    @GetMapping
    public List<QuestionComplexityNode> findAll() {
        return questionComplexityNodeRepository.findAll();
    }

    @PostMapping
    public QuestionComplexityNode create(@RequestBody QuestionComplexityNode node) {
        return questionComplexityNodeRepository.save(node);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        questionComplexityNodeRepository.deleteById(UUID.fromString(id));
    }

    @PutMapping
    public QuestionComplexityNode update(@RequestBody QuestionComplexityNode passedNode) throws MissingIdException {
        try {
            Assert.notNull(passedNode.getUuid(), MissingIdException.message);

            if (!questionComplexityNodeRepository.existsById(passedNode.getUuid())) {
                throw new ObjectNotFoundException();
            }

            throw new HttpServerErrorException(HttpStatusCode.valueOf(501));

        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
    }

    @PatchMapping
    public QuestionComplexityNode patch(@RequestBody QuestionComplexityPatchRequest request) throws MissingIdException {
        QuestionComplexityNode complexity;
        try {
            Assert.notNull(request.getUuid(), MissingIdException.message);
            Optional<QuestionComplexityNode> nullableComplexity = questionComplexityNodeRepository
                    .findById(UUID.fromString(request.getUuid()));
            complexity = nullableComplexity.orElseThrow(ObjectNotFoundException::new);
            request.getPatch().stream().forEach(operation -> executeOperation(operation, complexity));
            questionComplexityNodeRepository.save(complexity);
        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
        return complexity;
    }

    private void executeOperation(QuestionComplexityPatch operation, QuestionComplexityNode complexityNode) {
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
