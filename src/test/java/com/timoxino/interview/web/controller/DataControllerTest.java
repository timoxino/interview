package com.timoxino.interview.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

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
    ArgumentCaptor<UUID> longCaptor;

    @Test
    void create_without_parent() throws ParentDetailsMissingException, DuplicateNodeNameException {
        DataNode passedNode = DataNode.builder().build();
        when(dataNodeRepository.save(passedNode)).thenReturn(passedNode);

        DataNode result = controller.create(passedNode);

        verify(dataNodeRepository).save(passedNode);
        assertEquals(result, passedNode, "Returend object must be equial to the one from repo");
    }
    
    @Test
    void create_with_empty_parent() {
        assertThrows(ParentDetailsMissingException.class, () -> controller.create(DataNode.builder().parent(DataNode.builder().build()).build()),
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
        UUID randomUUID = UUID.randomUUID();
        DataNode parentDataNode = DataNode.builder().uuid(randomUUID).description("passed parent desc").build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findById(randomUUID)).thenReturn(Optional
                .ofNullable(DataNode.builder().name("found parent name").description("found parent desc").build()));
        when(dataNodeRepository.save(dataNode)).thenReturn(dataNode);

        DataNode result = controller.create(dataNode);

        verify(dataNodeRepository).save(argCaptor.capture());

        assertEquals("found parent desc", argCaptor.getValue().getParent().getDescription());
        assertEquals("found parent desc", result.getParent().getDescription());
    }

    @Test
    void create_with_search_by_parent_id_and_name() throws ParentDetailsMissingException, DuplicateNodeNameException {
        UUID randomUUID = UUID.randomUUID();
        DataNode parentDataNode = DataNode.builder().uuid(randomUUID).name("passed parent name")
                .description("passed parent desc").build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findById(randomUUID)).thenReturn(Optional.ofNullable(
                DataNode.builder().name("found parent name by id").description("found parent desc by id").build()));
        when(dataNodeRepository.save(dataNode)).thenReturn(dataNode);

        DataNode result = controller.create(dataNode);

        verify(dataNodeRepository).save(argCaptor.capture());

        assertEquals("found parent desc by id", argCaptor.getValue().getParent().getDescription());
        assertEquals("found parent desc by id", result.getParent().getDescription());
    }

    @Test
    void create_with_dupliate_name() {
        UUID randomUUID = UUID.randomUUID();
        DataNode parentDataNode = DataNode.builder().uuid(randomUUID).description("passed parent desc").build();
        DataNode dataNode = DataNode.builder().parent(parentDataNode).build();

        when(dataNodeRepository.findById(randomUUID)).thenReturn(Optional
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
        UUID randomUUID = UUID.randomUUID();
        DataNode node = DataNode.builder().uuid(randomUUID).build();

        when(dataNodeRepository.findById(randomUUID)).thenReturn(Optional.empty());
        
        assertThrows(ObjectNotFoundException.class, () -> controller.update(node), "Method must throw MissingIdException when no object found by 'id'");
    }
    
    @Test
    void all() {
        controller.all();

        verify(dataNodeRepository).findAll();
    }

    @Test
    void delete() {
        UUID randomUUID = UUID.randomUUID();
        controller.delete(randomUUID.toString());

        verify(dataNodeRepository).deleteById(longCaptor.capture());
        assertEquals(randomUUID, longCaptor.getValue());
    }

    @Test
    void update_and_save() throws ObjectNotFoundException, MissingIdException {
        UUID updatedUUID = UUID.randomUUID();
        UUID storedUUID = UUID.randomUUID();
        DataNode updatedNode = DataNode.builder().uuid(updatedUUID).name("updated name").description("updated description").build();
        DataNode storedNode = DataNode.builder().uuid(storedUUID).name("name").description("description").build();

        when(dataNodeRepository.findById(updatedUUID)).thenReturn(Optional.of(storedNode));

        DataNode result = controller.update(updatedNode);

        verify(dataNodeRepository).save(argCaptor.capture());
        assertEquals("updated name", argCaptor.getValue().getName());
        assertEquals("updated description", argCaptor.getValue().getDescription());
        assertEquals("updated name", result.getName());
        assertEquals("updated description", result.getDescription());
    }
}
