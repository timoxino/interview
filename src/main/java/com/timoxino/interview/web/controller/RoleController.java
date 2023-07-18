package com.timoxino.interview.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timoxino.interview.web.exception.CompetencyDetailsMissingException;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.RoleNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import com.timoxino.interview.web.repo.RoleNodeRepository;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleNodeRepository roleNodeRepository;
    private final DataNodeRepository dataNodeRepository;

    public RoleController(RoleNodeRepository roleNodeRepository, DataNodeRepository dataNodeRepository) {
        this.roleNodeRepository = roleNodeRepository;
        this.dataNodeRepository = dataNodeRepository;
    }

    @GetMapping
    List<RoleNode> findAll() {
        return roleNodeRepository.findAll();
    }

    @PostMapping
    RoleNode create(@RequestBody RoleNode roleNode) throws MissingIdException {
        try {
            if (roleNode.getCompetencies() != null) {
                roleNode.setCompetencies(lookupCompetencies(roleNode.getCompetencies()));
            }
        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
        return roleNodeRepository.save(roleNode);
    }

    @PatchMapping
    RoleNode update(@RequestBody RoleNode nodeToUpdate) throws MissingIdException {
        Optional<RoleNode> storedNode;
        try {
            Assert.notNull(nodeToUpdate.getUuid(), MissingIdException.message);

            storedNode = roleNodeRepository.findById(nodeToUpdate.getUuid());
            storedNode.ifPresentOrElse(node -> {
                node.setName(nodeToUpdate.getName());
                node.setCompetencies(lookupCompetencies(nodeToUpdate.getCompetencies()));
                node.setLevel(nodeToUpdate.getLevel());
                roleNodeRepository.save(node);
            }, () -> {
                throw new ObjectNotFoundException();
            });

        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
        return storedNode.get();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable String id) {
        roleNodeRepository.deleteById(UUID.fromString(id));
    }

    private List<DataNode> lookupCompetencies(List<DataNode> competencies) {

        List<DataNode> storedCompetencies = new ArrayList<>();
        competencies.stream().forEach(passed -> {
            Assert.notNull(passed.getUuid(), CompetencyDetailsMissingException.message);
            dataNodeRepository.findById(passed.getUuid()).ifPresentOrElse(stored -> storedCompetencies.add(stored),
                    () -> {
                        throw new ObjectNotFoundException();
                    });
        });
        return storedCompetencies;
    }
}
