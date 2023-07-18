package com.timoxino.interview.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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

import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.RoleNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import com.timoxino.interview.web.repo.RoleNodeRepository;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @Mock
    DataNodeRepository dataNodeRepository;

    @Mock
    RoleNodeRepository roleNodeRepository;

    @InjectMocks
    RoleController roleController;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Captor
    ArgumentCaptor<RoleNode> roleCaptor;

    @Test
    void create_competencies_null() throws MissingIdException {
        RoleNode passedRole = RoleNode.builder().build();
        RoleNode storedRole = RoleNode.builder().build();

        when(roleNodeRepository.save(passedRole)).thenReturn(storedRole);

        RoleNode result = roleController.create(passedRole);

        verify(roleNodeRepository).save(passedRole);
        assertEquals(result, storedRole, "Returned object must be equial to the one from repo");
    }

    @Test
    void create_competencies_empty() throws MissingIdException {
        RoleNode passedRole = RoleNode.builder().competencies(Collections.<DataNode>emptyList()).build();
        RoleNode storedRole = RoleNode.builder().build();

        when(roleNodeRepository.save(passedRole)).thenReturn(storedRole);

        RoleNode result = roleController.create(passedRole);

        verify(roleNodeRepository).save(passedRole);
        assertEquals(result, storedRole, "Returned object must be equial to the one from repo");
    }

    @Test
    void create_competency_id_missing() throws MissingIdException {
        RoleNode passedRole = RoleNode.builder().competencies(Arrays.asList(DataNode.builder().build())).build();

        assertThrows(MissingIdException.class, () -> roleController.create(passedRole),
                "Method must throw MissingIdException in case of missing 'id'");
    }

    @Test
    void create_competency_not_found_by_id() throws MissingIdException {
        UUID passedUuid = UUID.randomUUID();
        RoleNode passedRole = RoleNode.builder()
                .competencies(Arrays.asList(DataNode.builder().uuid(passedUuid).build())).build();

        when(dataNodeRepository.findById(passedUuid)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> roleController.create(passedRole),
                "Method must return ObjectNotFoundException when no object found by 'id'");
    }

    @Test
    void create() throws MissingIdException {
        UUID passedDataUuid = UUID.randomUUID();
        UUID passedRoleUuid = UUID.randomUUID();
        RoleNode passedRole = RoleNode.builder().uuid(passedRoleUuid)
                .competencies(Arrays.asList(DataNode.builder().uuid(passedDataUuid).build())).level(3).build();
        UUID storedDataUuid = UUID.randomUUID();
        DataNode storedDataNode = DataNode.builder().name("stored data node").description("stored desc")
                .uuid(storedDataUuid).build();
        when(dataNodeRepository.findById(passedDataUuid)).thenReturn(Optional.of(storedDataNode));

        roleController.create(passedRole);

        verify(roleNodeRepository).save(roleCaptor.capture());
        assertEquals(passedRoleUuid, roleCaptor.getValue().getUuid());
        assertEquals(3, roleCaptor.getValue().getLevel());
        DataNode returnedCompetency = roleCaptor.getValue().getCompetencies().get(0);
        assertEquals("stored data node", returnedCompetency.getName());
        assertEquals("stored desc", returnedCompetency.getDescription());
        assertEquals(storedDataUuid, returnedCompetency.getUuid());
    }

    @Test
    void update_id_missing() throws MissingIdException {
        assertThrows(MissingIdException.class, () -> roleController.update(RoleNode.builder().build()),
                "Method must throw MissingIdException in case of missing 'id'");
    }

    @Test
    void update_not_found_by_id() {
        UUID passedCompetencyUuid = UUID.randomUUID();
        UUID passedRoleUuid = UUID.randomUUID();
        RoleNode passedRole = RoleNode.builder().uuid(passedRoleUuid)
                .competencies(Arrays.asList(DataNode.builder().uuid(passedCompetencyUuid).build())).build();

        when(roleNodeRepository.findById(passedRoleUuid)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> roleController.update(passedRole),
                "Method must return ObjectNotFoundException when no object found by 'id'");
    }

    @Test
    void update() throws MissingIdException {
        UUID passedCompetencyUuid = UUID.randomUUID();
        UUID passedRoleUuid = UUID.randomUUID();
        RoleNode passedRole = RoleNode.builder().name("passed role name").uuid(passedRoleUuid).level(3)
                .competencies(Arrays.asList(DataNode.builder().uuid(passedCompetencyUuid).build())).build();
        UUID storedDataUuid = UUID.randomUUID();
        DataNode storedData = DataNode.builder().uuid(storedDataUuid).description("stored desc").name("stored name")
                .build();
        RoleNode storedRole = RoleNode.builder().name("stored role name").level(2).competencies(Arrays.asList(storedData))
                .build();

        when(roleNodeRepository.findById(passedRoleUuid)).thenReturn(Optional.of(storedRole));
        when(dataNodeRepository.findById(passedCompetencyUuid)).thenReturn(Optional.of(storedData));

        roleController.update(passedRole);

        verify(roleNodeRepository).save(roleCaptor.capture());
        assertEquals("passed role name", roleCaptor.getValue().getName());
        assertEquals(3, roleCaptor.getValue().getLevel());
        List<DataNode> competencies = roleCaptor.getValue().getCompetencies();
        assertEquals("stored desc", competencies.get(0).getDescription());
        assertEquals("stored name", competencies.get(0).getName());
    }

    @Test
    void findAll() {
        roleController.findAll();

        verify(roleNodeRepository).findAll();
    }

    @Test
    void delete() {
        UUID randomUUID = UUID.randomUUID();
        roleController.delete(randomUUID.toString());

        verify(roleNodeRepository).deleteById(uuidCaptor.capture());
        assertEquals(randomUUID, uuidCaptor.getValue());
    }
}
