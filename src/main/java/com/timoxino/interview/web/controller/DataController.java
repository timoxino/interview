package com.timoxino.interview.web.controller;

import com.timoxino.interview.web.exception.DuplicateNodeNameException;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.exception.ParentDetailsMissingException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.DataNodeRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataNodeRepository dataNodeRepository;

    public DataController(DataNodeRepository dataNodeRepository) {
        this.dataNodeRepository = dataNodeRepository;
    }

    @GetMapping
    List<DataNode> all() {
        return dataNodeRepository.findAll();
    }

    @PostMapping
    DataNode create(@RequestBody DataNode dataNode) throws ParentDetailsMissingException, DuplicateNodeNameException {
        try {
            if(dataNode.getParent() != null) {
                rewriteParent(dataNode);
            }
            dataNode = dataNodeRepository.save(dataNode);
        } catch (DataIntegrityViolationException dve) {
            throw new DuplicateNodeNameException();
        }
        return dataNode;
    }

    private void rewriteParent(DataNode dataNode) throws ParentDetailsMissingException {
        DataNode parent = dataNode.getParent();
            if (parent.getUuid() != null) {
                dataNodeRepository.findById(parent.getUuid()).ifPresent(dataNode::setParent);
            } else if (StringUtils.hasText(parent.getName())) {
                dataNodeRepository.findByName(parent.getName()).ifPresent(dataNode::setParent);
            } else throw new ParentDetailsMissingException();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable String id) {
        dataNodeRepository.deleteById(UUID.fromString(id));
    }

    @PatchMapping
    DataNode update(@RequestBody DataNode updatedDataNode) throws MissingIdException, ObjectNotFoundException {
        Optional<DataNode> storedNode;
        try {
            Assert.notNull(updatedDataNode.getUuid(), MissingIdException.message);

            storedNode = dataNodeRepository.findById(updatedDataNode.getUuid());
            storedNode.ifPresentOrElse((node) -> {
                node.setName(updatedDataNode.getName() != null ? updatedDataNode.getName() : node.getName());
                node.setDescription(updatedDataNode.getDescription() != null ? updatedDataNode.getDescription() : node.getDescription());
                dataNodeRepository.save(node);
            }, () -> {
                throw new ObjectNotFoundException();
            });
        } catch (IllegalArgumentException iae) {
            throw new MissingIdException();
        }
        return storedNode.get();
    }
}
