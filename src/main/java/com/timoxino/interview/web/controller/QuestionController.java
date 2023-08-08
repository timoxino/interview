package com.timoxino.interview.web.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timoxino.interview.web.dto.QuestionUpdateRequest;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.QuestionsAware;
import com.timoxino.interview.web.repo.QuestionAwareRepository;
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

        if (request.getCategoryUuid() == null || request.getComplexityUuid() == null) {
            throw new MissingIdException();
        }

        if (!questionCategoryNodeRepository.existsById(UUID.fromString(request.getCategoryUuid()))
                || !questionComplexityNodeRepository.existsById(UUID.fromString(request.getComplexityUuid()))) {
            throw new ObjectNotFoundException();
        }
        final DataNode questionData = dataController.update(request.getDataNode());

        updateQuestionAwareNodes(UUID.fromString(request.getCategoryUuid()), questionData,
                (QuestionAwareRepository) questionCategoryNodeRepository);
        updateQuestionAwareNodes(UUID.fromString(request.getComplexityUuid()), questionData,
                (QuestionAwareRepository) questionComplexityNodeRepository);

        return questionData;
    }

    private void updateQuestionAwareNodes(UUID questionAwareNodeUuid, DataNode questionData,
            QuestionAwareRepository<QuestionsAware> questionAwareNodeRepo) {
        // search for complexity/category to be assigned
        questionAwareNodeRepo.findById(questionAwareNodeUuid)
                .ifPresent((questionAware) -> {
                    // search for categories/complexities the question belongs to
                    List<QuestionsAware> questionAwareNodes = questionAwareNodeRepo
                            .findByQuestion(questionData.getUuid().toString());
                    // remove the question from previous categories/complexities
                    questionAwareNodes.forEach((questionAwareNode) -> {
                        questionAwareNode.setQuestions(questionAwareNode.getQuestions().stream().filter(question -> !question.getUuid().equals(questionData.getUuid())).collect(Collectors.toList()));
                        questionAwareNodeRepo.save(questionAwareNode);
                    });
                    // add question to the new category/complexity
                    questionAware.getQuestions().add(questionData);
                    questionAwareNodeRepo.save(questionAware);
                });
    }
}
