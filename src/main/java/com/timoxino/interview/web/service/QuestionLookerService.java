package com.timoxino.interview.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.QuestionNodeRepository;

@Service
public class QuestionLookerService {

    private static final String CATEGORY_WHITEBOARDING = "Whiteboarding";
    private static final String CATEGORY_SCENARIO = "Scenario";
    private static final String CATEGORY_PRACTICAL = "Practical";
    private static final String CATEGORY_THEORETICAL = "Theoretical";
    
    @Autowired
    private final QuestionNodeRepository questionNodeRepository;

    public QuestionLookerService(QuestionNodeRepository questionNodeRepository) {
        this.questionNodeRepository = questionNodeRepository;
    }

    public Multimap<String, DataNode> prepareQuestions(String roleName, Integer level) {
        Multimap<String, DataNode> topicsQuestions = HashMultimap.create();

        populateQuestionsPerCategory(roleName, level, CATEGORY_THEORETICAL, topicsQuestions);
        populateQuestionsPerCategory(roleName, level, CATEGORY_PRACTICAL, topicsQuestions);
        populateQuestionsPerCategory(roleName, level, CATEGORY_SCENARIO, topicsQuestions);
        populateQuestionsPerCategory(roleName, level, CATEGORY_WHITEBOARDING, topicsQuestions);

        return topicsQuestions;
    }

    private void populateQuestionsPerCategory(String roleName, Integer level, String category, Multimap<String, DataNode> topicsQuestions) {
        List<DataNode> questionsByCategory = questionNodeRepository.findQuestionsByRoleNameAndCategoryAndComplexity(roleName, category, level);

        for (DataNode question : questionsByCategory) {
            topicsQuestions.put(question.getParent().getName(), question);
        }
    }
}
