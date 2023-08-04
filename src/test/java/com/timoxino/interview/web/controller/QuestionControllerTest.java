package com.timoxino.interview.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.timoxino.interview.web.dto.QuestionUpdateRequest;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.QuestionCategoryNode;
import com.timoxino.interview.web.model.QuestionComplexityNode;
import com.timoxino.interview.web.repo.QuestionCategoryNodeRepository;
import com.timoxino.interview.web.repo.QuestionComplexityNodeRepository;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {

    @Mock
    DataController dataController;

    @Mock
    QuestionCategoryNodeRepository questionCategoryNodeRepository;

    @Mock
    QuestionComplexityNodeRepository questionComplexityNodeRepository;

    @InjectMocks
    QuestionController controller;

    @Test
    void updateQuestionMisssingUuid() throws ObjectNotFoundException, MissingIdException {
        QuestionUpdateRequest request = new QuestionUpdateRequest();
        assertThrows(MissingIdException.class, () -> {
            controller.updateQuestion(request);
        }, "Method must throw MissingIdException when no 'uuid' passed");

        request.setCategoryUuid("d0714a68-8755-476e-bdb6-6ff8138cd22c");
        assertThrows(MissingIdException.class, () -> {
            controller.updateQuestion(request);
        }, "Method must throw MissingIdException when no 'uuid' passed");
    }

    @Test
    void updateQuestionMissingObject() throws ObjectNotFoundException, MissingIdException {
        QuestionUpdateRequest request = new QuestionUpdateRequest();
        request.setComplexityUuid("d0714a68-8755-476e-bdb6-6ff8138cd22c");
        request.setCategoryUuid("d0714a68-8755-476e-bdb6-6ff8138cd22c");

        when(questionCategoryNodeRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThrows(ObjectNotFoundException.class, () -> {
            controller.updateQuestion(request);
        }, "Method must return ObjectNotFoundException when no object found by 'id'");
    }

    @Test
    void updateQuestion() throws ObjectNotFoundException, MissingIdException {
        QuestionUpdateRequest request = new QuestionUpdateRequest();
        request.setComplexityUuid("d0714a68-8755-476e-bdb6-6ff8138cd22c");
        request.setCategoryUuid("d0714a68-8755-476e-bdb6-6ff8138cd22c");
        DataNode question = DataNode.builder().uuid(UUID.fromString("6c59d7d7-b765-4e02-a1b3-ca84acf1d5ea")).build();
        request.setDataNode(question);
        DataNode updatedQuestion = DataNode.builder().build();
        QuestionCategoryNode questionCategoryNode = QuestionCategoryNode.builder().questions(new ArrayList<>()).build();
        QuestionComplexityNode questionComplexityNode = QuestionComplexityNode.builder().questions(new ArrayList<>())
                .build();

        List<QuestionCategoryNode> categories = new ArrayList<>();
        List<DataNode> questions1 = new ArrayList<>();
        DataNode categorizedQuestion1 = DataNode.builder().build();
        questions1.add(categorizedQuestion1);
        QuestionCategoryNode storedCategory = QuestionCategoryNode.builder().questions(questions1).build();
        categories.add(storedCategory);

        List<QuestionComplexityNode> complexities = new ArrayList<>();
        DataNode categorizedQuestion2 = DataNode.builder().build();
        List<DataNode> questions2 = new ArrayList<>();
        questions2.add(categorizedQuestion2);
        QuestionComplexityNode storedComplexity = QuestionComplexityNode.builder().questions(questions2).build();
        complexities.add(storedComplexity);

        when(questionCategoryNodeRepository.existsById(any(UUID.class))).thenReturn(true);
        when(questionComplexityNodeRepository.existsById(any(UUID.class))).thenReturn(true);
        when(dataController.update(question)).thenReturn(updatedQuestion);
        when(questionCategoryNodeRepository.findById(any(UUID.class))).thenReturn(Optional.of(questionCategoryNode));
        when(questionCategoryNodeRepository.findCategoryByQuestion(anyString())).thenReturn(categories);
        when(questionComplexityNodeRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(questionComplexityNode));
        when(questionComplexityNodeRepository.findComplexitiesByQuestion(anyString())).thenReturn(complexities);

        DataNode result = controller.updateQuestion(request);

        verify(questionCategoryNodeRepository).save(storedCategory);
        verify(questionCategoryNodeRepository).save(questionCategoryNode);
        verify(questionComplexityNodeRepository).save(storedComplexity);
        verify(questionComplexityNodeRepository).save(questionComplexityNode);

        assertFalse(questionCategoryNode.getQuestions().isEmpty(), "List of questions should not be empty");
        assertFalse(questionComplexityNode.getQuestions().isEmpty(), "List of questions should not be empty");
        assertEquals(updatedQuestion, result, "Operation must return the object stored in DB");
        assertTrue(questions1.isEmpty(),
                "Question must be deleted from the the other Category prior to be added to another one");
        assertTrue(questions2.isEmpty(),
                "Question must be deleted from the the other Complexity prior to be added to another one");
    }
}
