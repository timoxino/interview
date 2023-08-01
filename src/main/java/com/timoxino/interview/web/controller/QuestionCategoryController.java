package com.timoxino.interview.web.controller;

import java.util.List;
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

import com.timoxino.interview.web.dto.DataPatchRequest;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.QuestionCategoryNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import com.timoxino.interview.web.repo.QuestionCategoryNodeRepository;

@RestController
@RequestMapping("data/question/category")
public class QuestionCategoryController extends DataNodePatchAwareController {

    private QuestionCategoryNodeRepository questionCategoryNodeRepository;

    public QuestionCategoryController(QuestionCategoryNodeRepository questionCategoryNodeRepository, DataNodeRepository dataNodeRepository) {
        super(dataNodeRepository);
        this.questionCategoryNodeRepository = questionCategoryNodeRepository;
    }

    @GetMapping
    public List<QuestionCategoryNode> findAll() {
        return questionCategoryNodeRepository.findAll();
    }

    @PostMapping
    public QuestionCategoryNode create(@RequestBody QuestionCategoryNode node) {
        return questionCategoryNodeRepository.save(node);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        questionCategoryNodeRepository.deleteById(UUID.fromString(id));
    }

    @PutMapping
    public QuestionCategoryNode update(@RequestBody QuestionCategoryNode node) throws MissingIdException {
        try {
            Assert.notNull(node.getUuid(), MissingIdException.message);

            if (!questionCategoryNodeRepository.existsById(node.getUuid())) {
                throw new ObjectNotFoundException();
            }

            throw new HttpServerErrorException(HttpStatusCode.valueOf(501));

        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
    }

    @PatchMapping
    public QuestionCategoryNode patch(@RequestBody DataPatchRequest request) throws MissingIdException {
        QuestionCategoryNode category;
        try {
            Assert.notNull(request.getUuid(), MissingIdException.message);
            Optional<QuestionCategoryNode> nullableCategory = questionCategoryNodeRepository
                    .findById(UUID.fromString(request.getUuid()));
            category = nullableCategory.orElseThrow(ObjectNotFoundException::new);
            request.getPatch().stream().forEach(operation -> executeOperation(operation, category));
            questionCategoryNodeRepository.save(category);
        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
        return category;
    }
}
