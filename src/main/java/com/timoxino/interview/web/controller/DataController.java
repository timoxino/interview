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
            if (parent.getId() != null) {
                dataNodeRepository.findById(parent.getId()).ifPresent(dataNode::setParent);
            } else if (StringUtils.hasText(parent.getName())) {
                dataNodeRepository.findByName(parent.getName()).ifPresent(dataNode::setParent);
            } else throw new ParentDetailsMissingException();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        dataNodeRepository.deleteById(id);
    }

    @PatchMapping
    DataNode update(@RequestBody DataNode updatedDataNode) throws MissingIdException, ObjectNotFoundException {
        Optional<DataNode> storedNode;
        try {
            Assert.notNull(updatedDataNode.getId(), MissingIdException.message);

            storedNode = dataNodeRepository.findById(updatedDataNode.getId());
            storedNode.ifPresentOrElse((node) -> {
                node.setName(updatedDataNode.getName());
                node.setDescription(updatedDataNode.getDescription());
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
