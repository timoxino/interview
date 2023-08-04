package com.timoxino.interview.web.controller;

import java.util.List;
import java.util.UUID;

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
        questionAwareNodeRepo.findById(questionAwareNodeUuid)
                .ifPresent((questionAware) -> {
                    List<QuestionsAware> questionAwareNodes = questionAwareNodeRepo
                            .findByQuestion(questionData.getUuid().toString());
                    questionAwareNodes.forEach((questionAwareNode) -> {
                        questionAwareNode.getQuestions().remove(questionData);
                        questionAwareNodeRepo.save(questionAwareNode);
                    });
                    questionAware.getQuestions().add(questionData);
                    questionAwareNodeRepo.save(questionAware);
                });
    }
}
