package com.timoxino.interview.web.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timoxino.interview.web.dto.QuestionUpdateRequest;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.QuestionCategoryNodeRepository;
import com.timoxino.interview.web.repo.QuestionComplexityNodeRepository;

@RestController
@RequestMapping("data/question")
public class QuestionController {

    private DataController dataController;
    private QuestionCategoryNodeRepository questionCategoryNodeRepository;
    private QuestionComplexityNodeRepository questionComplexityNodeRepository;

    public QuestionController(DataController dataController,
            QuestionCategoryNodeRepository questionCategoryNodeRepository,
            QuestionComplexityNodeRepository questionComplexityNodeRepository) {
        this.dataController = dataController;
        this.questionCategoryNodeRepository = questionCategoryNodeRepository;
        this.questionComplexityNodeRepository = questionComplexityNodeRepository;
    }

    @PutMapping
    DataNode updateQuestion(@RequestBody QuestionUpdateRequest request)
            throws ObjectNotFoundException, MissingIdException {
        if (!questionCategoryNodeRepository.existsById(UUID.fromString(request.getCategoryUuid()))
                || !questionComplexityNodeRepository.existsById(UUID.fromString(request.getComplexityUuid()))) {
            throw new ObjectNotFoundException();
        }
        final DataNode questionData = dataController.update(request.getDataNode());

        questionCategoryNodeRepository.findById(UUID.fromString(request.getCategoryUuid()))
                .ifPresent((questionAware) -> {
                    questionAware.getQuestions().add(questionData);
                    questionCategoryNodeRepository.save(questionAware);
                });

        questionComplexityNodeRepository.findById(UUID.fromString(request.getComplexityUuid()))
                .ifPresent((questionAware) -> {
                    questionAware.getQuestions().add(questionData);
                    questionComplexityNodeRepository.save(questionAware);
                });
        return questionData;
    }
}
