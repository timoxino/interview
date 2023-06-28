package com.timoxino.interview.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.timoxino.interview.web.exception.DuplicateNodeNameException;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.exception.ParentDetailsMissingException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.DataNodeRepository;

@ExtendWith(MockitoExtension.class)
public class DataControllerTest {

    @Mock
    DataNodeRepository dataNodeRepository;

    @InjectMocks
    private DataController controller;

    @Captor
    ArgumentCaptor<DataNode> argCaptor;

    @Captor
    ArgumentCaptor<Long> longCaptor;

    @Test
    void create_with_empty_object() {
        assertThrows(ParentDetailsMissingException.class, () -> controller.create(DataNode.builder().build()),
                "Methos must throw ParentDetailsMissingException in case of null parameter");
    }

    @Test
    void create_with_new_parent() throws ParentDetailsMissingException, DuplicateNodeNameException {
        DataNode parentDataNode = DataNode.builder().name("passed parent name").description("passed parent desc")
                .build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findByName("passed parent name")).thenReturn(Optional.empty());
        when(dataNodeRepository.save(dataNode)).thenReturn(dataNode);

        DataNode result = controller.create(dataNode);

        verify(dataNodeRepository).save(argCaptor.capture());

        assertEquals("passed parent desc", argCaptor.getValue().getParent().getDescription());
        assertEquals("passed parent desc", result.getParent().getDescription());
    }

    @Test
    void create_with_search_by_parent_name() throws ParentDetailsMissingException, DuplicateNodeNameException {
        DataNode parentDataNode = DataNode.builder().name("passed parent name").description("passed parent desc")
                .build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findByName("passed parent name")).thenReturn(Optional
                .ofNullable(DataNode.builder().name("found parent name").description("found parent desc").build()));
        when(dataNodeRepository.save(dataNode)).thenReturn(dataNode);

        DataNode result = controller.create(dataNode);

        verify(dataNodeRepository).save(argCaptor.capture());

        assertEquals("found parent desc", argCaptor.getValue().getParent().getDescription());
        assertEquals("found parent desc", result.getParent().getDescription());
    }

    @Test
    void create_with_search_by_parent_id() throws ParentDetailsMissingException, DuplicateNodeNameException {
        DataNode parentDataNode = DataNode.builder().id(123L).description("passed parent desc").build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findById(123L)).thenReturn(Optional
                .ofNullable(DataNode.builder().name("found parent name").description("found parent desc").build()));
        when(dataNodeRepository.save(dataNode)).thenReturn(dataNode);

        DataNode result = controller.create(dataNode);

        verify(dataNodeRepository).save(argCaptor.capture());

        assertEquals("found parent desc", argCaptor.getValue().getParent().getDescription());
        assertEquals("found parent desc", result.getParent().getDescription());
    }

    @Test
    void create_with_search_by_parent_id_and_name() throws ParentDetailsMissingException, DuplicateNodeNameException {
        DataNode parentDataNode = DataNode.builder().id(123L).name("passed parent name")
                .description("passed parent desc").build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findById(123L)).thenReturn(Optional.ofNullable(
                DataNode.builder().name("found parent name by id").description("found parent desc by id").build()));
        when(dataNodeRepository.save(dataNode)).thenReturn(dataNode);

        DataNode result = controller.create(dataNode);

        verify(dataNodeRepository).save(argCaptor.capture());

        assertEquals("found parent desc by id", argCaptor.getValue().getParent().getDescription());
        assertEquals("found parent desc by id", result.getParent().getDescription());
    }

    @Test
    void create_with_dupliate_name() {
        DataNode parentDataNode = DataNode.builder().id(123L).description("passed parent desc").build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findById(123L)).thenReturn(Optional
                .ofNullable(DataNode.builder().name("found parent name").description("found parent desc").build()));
        when(dataNodeRepository.save(dataNode)).thenThrow(new DataIntegrityViolationException("test message"));

        assertThrows(DuplicateNodeNameException.class, () -> controller.create(dataNode),
                "Methos must throw DuplicateNodeNameException in case of duplicate name");
    }

    @Test
    void update_with_missing_id() {
        assertThrows(MissingIdException.class, () -> controller.update(DataNode.builder().build()), "Method must throw MissingIdException when 'id' is missing");
    }

    @Test
    void update_with_incorrect_id() {
        DataNode node = DataNode.builder().id(123L).build();

        when(dataNodeRepository.findById(123L)).thenReturn(Optional.empty());
        
        assertThrows(ObjectNotFoundException.class, () -> controller.update(node), "Method must throw MissingIdException when no object found by 'id'");
    }
    
    @Test
    void all() {
        controller.all();

        verify(dataNodeRepository).findAll();
    }

    @Test
    void delete() {
        controller.delete(123L);

        verify(dataNodeRepository).deleteById(longCaptor.capture());
        assertEquals(123L, longCaptor.getValue());
    }

    @Test
    void update_and_save() throws ObjectNotFoundException, MissingIdException {
        DataNode updatedNode = DataNode.builder().id(123L).name("updated name").description("updated description").build();
        DataNode storedNode = DataNode.builder().id(123L).name("name").description("description").build();

        when(dataNodeRepository.findById(123L)).thenReturn(Optional.of(storedNode));

        DataNode result = controller.update(updatedNode);

        verify(dataNodeRepository).save(argCaptor.capture());
        assertEquals("updated name", argCaptor.getValue().getName());
        assertEquals("updated description", argCaptor.getValue().getDescription());
        assertEquals("updated name", result.getName());
        assertEquals("updated description", result.getDescription());
    }
}
