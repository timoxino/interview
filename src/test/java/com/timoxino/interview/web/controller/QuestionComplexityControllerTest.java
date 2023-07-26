package com.timoxino.interview.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.timoxino.interview.web.dto.QuestionComplexityPatch;
import com.timoxino.interview.web.dto.QuestionComplexityPatchOperation;
import com.timoxino.interview.web.dto.QuestionComplexityPatchRequest;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.QuestionComplexityNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import com.timoxino.interview.web.repo.QuestionComplexityNodeRepository;

@ExtendWith(MockitoExtension.class)
public class QuestionComplexityControllerTest {

        @Mock
        DataNodeRepository dataNodeRepository;

        @Mock
        QuestionComplexityNodeRepository questionComplexityNodeRepository;

        @InjectMocks
        QuestionComplexityController controller;

        @Captor
        ArgumentCaptor<QuestionComplexityNode> complexityCaptor;

        @Captor
        ArgumentCaptor<UUID> uuidCaptor;

        @Test
        void create() {
                QuestionComplexityNode complexity = QuestionComplexityNode.builder().description("desc").build();
                controller.create(complexity);

                verify(questionComplexityNodeRepository).save(complexityCaptor.capture());
                assertEquals(complexity, complexityCaptor.getValue());
        }

        @Test
        void delete() {
                controller.delete("1b4b7272-d877-4504-8cec-b32e1bef4112");

                verify(questionComplexityNodeRepository).deleteById(uuidCaptor.capture());
                assertEquals("1b4b7272-d877-4504-8cec-b32e1bef4112", uuidCaptor.getValue().toString());
        }

        @Test
        void findAll() {
                controller.findAll();

                verify(questionComplexityNodeRepository).findAll();
        }

        @Test
        void patchMissingId() {
                QuestionComplexityPatchRequest request = new QuestionComplexityPatchRequest();
                assertThrows(MissingIdException.class, () -> controller.patch(request),
                                "Method must throw MissingIdException in case of missing 'id'");
        }

        @Test
        void patchObjectNotFound() {
                QuestionComplexityPatchRequest request = new QuestionComplexityPatchRequest();
                request.setUuid("1b4b7272-d877-4504-8cec-b32e1bef4112");

                assertThrows(ObjectNotFoundException.class, () -> controller.patch(request),
                                "Method must return ObjectNotFoundException when no object found by 'id'");
        }

        @Test
        void patchAddQuestionNotFound() throws MissingIdException {
                QuestionComplexityPatchRequest request = new QuestionComplexityPatchRequest();
                request.setUuid("1b4b7272-d877-4504-8cec-b32e1bef4112");
                LinkedHashMap<String, String> valuesMap = new LinkedHashMap<String, String>();
                valuesMap.put("questionUuid", "83d2e402-d5f3-4c1e-8e83-ee0933d0a284");
                request.setPatch(Collections.singletonList(
                                QuestionComplexityPatch.builder().op(QuestionComplexityPatchOperation.ADD)
                                                .value(valuesMap)
                                                .path("/question").build()));
                Optional<QuestionComplexityNode> nullableComplexity = Optional
                                .of(QuestionComplexityNode.builder().uuid(UUID.randomUUID())
                                                .questions(new ArrayList<>()).build());

                when(questionComplexityNodeRepository.findById(any(UUID.class))).thenReturn(nullableComplexity);

                assertThrows(ObjectNotFoundException.class, () -> controller.patch(request),
                                "Method must return ObjectNotFoundException when no object found by 'id'");
        }

        @Test
        void patchAddQuestion() throws MissingIdException {
                QuestionComplexityPatchRequest request = new QuestionComplexityPatchRequest();
                request.setUuid("1b4b7272-d877-4504-8cec-b32e1bef4112");
                LinkedHashMap<String, String> valuesMap = new LinkedHashMap<String, String>();
                valuesMap.put("questionUuid", "83d2e402-d5f3-4c1e-8e83-ee0933d0a284");
                request.setPatch(Collections.singletonList(
                                QuestionComplexityPatch.builder().op(QuestionComplexityPatchOperation.ADD)
                                                .value(valuesMap)
                                                .path("/question").build()));
                Optional<QuestionComplexityNode> nullableComplexity = Optional
                                .of(QuestionComplexityNode.builder().uuid(UUID.randomUUID())
                                                .questions(new ArrayList<>()).build());

                when(questionComplexityNodeRepository.findById(any(UUID.class))).thenReturn(nullableComplexity);
                when(dataNodeRepository.findById(any(UUID.class))).thenReturn(Optional.of(DataNode.builder()
                                .uuid(UUID.fromString("83d2e402-d5f3-4c1e-8e83-ee0933d0a284")).build()));

                QuestionComplexityNode result = controller.patch(request);

                verify(questionComplexityNodeRepository).save(complexityCaptor.capture());
                assertFalse(complexityCaptor.getValue().getQuestions().isEmpty(),
                                "New element must be added to the list of questions");
                assertEquals(complexityCaptor.getValue(), result,
                                "Result of the operation must be equal to the object stored in DB");
        }

        @Test
        void patchDeleteQuestion() throws MissingIdException {
                QuestionComplexityPatchRequest request = new QuestionComplexityPatchRequest();
                request.setUuid("1b4b7272-d877-4504-8cec-b32e1bef4112");
                LinkedHashMap<String, String> valuesMap = new LinkedHashMap<String, String>();
                valuesMap.put("questionUuid", "83d2e402-d5f3-4c1e-8e83-ee0933d0a284");
                request.setPatch(Arrays.asList(QuestionComplexityPatch.builder()
                                .op(QuestionComplexityPatchOperation.DELETE).value(valuesMap)
                                .path("/question").build()));
                DataNode question = DataNode.builder().uuid(UUID.fromString("83d2e402-d5f3-4c1e-8e83-ee0933d0a284"))
                                .build();
                List<DataNode> questions = new ArrayList<DataNode>();
                questions.add(question);
                Optional<QuestionComplexityNode> nullableComplexity = Optional
                                .of(QuestionComplexityNode.builder().uuid(UUID.randomUUID())
                                                .questions(questions).build());

                when(questionComplexityNodeRepository.findById(any(UUID.class))).thenReturn(nullableComplexity);
                when(dataNodeRepository.findById(any(UUID.class))).thenReturn(Optional.of(question));

                QuestionComplexityNode result = controller.patch(request);
                verify(questionComplexityNodeRepository).save(complexityCaptor.capture());
                assertTrue(complexityCaptor.getValue().getQuestions().isEmpty(),
                                "Element must be deleted from the list of questions");
                assertEquals(complexityCaptor.getValue(), result,
                                "Result of the operation must be equal to the object stored in DB");
        }
}
