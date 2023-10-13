package com.timoxino.interview.web.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timoxino.interview.web.exception.DuplicateNodeNameException;
import com.timoxino.interview.web.exception.MissingIdException;
import com.timoxino.interview.web.exception.ObjectNotFoundException;
import com.timoxino.interview.web.exception.ParentDetailsMissingException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import com.timoxino.interview.web.repo.QuestionNodeRepository;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataNodeRepository dataNodeRepository;
    private final QuestionNodeRepository questionNodeRepository;

    public DataController(DataNodeRepository dataNodeRepository, QuestionNodeRepository questionNodeRepository) {
        this.dataNodeRepository = dataNodeRepository;
        this.questionNodeRepository = questionNodeRepository;
    }

    @GetMapping
    List<DataNode> find() {
        return dataNodeRepository.findAll();
    }

    @GetMapping("/topic")
    List<DataNode> findTopic(@RequestParam("roleName") String roleName) {
        return dataNodeRepository.findTopicsByRoleName(roleName);
    }

    @GetMapping("/question")
    List<DataNode> findQuestion(@RequestParam("roleName") String roleName) {
        return questionNodeRepository.findQuestionsByRoleName(roleName);
    }

    @PostMapping
    DataNode create(@RequestBody DataNode dataNode) throws ParentDetailsMissingException, DuplicateNodeNameException {
        try {
            if (dataNode.getParent() != null) {
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
        } else
            throw new ParentDetailsMissingException();
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
                node.setDescription(updatedDataNode.getDescription() != null ? updatedDataNode.getDescription()
                        : node.getDescription());
                node.setType(updatedDataNode.getType() != null ? updatedDataNode.getType() : node.getType());
                // in order to keep relations while updating, parent 'id' must be passed and parent object fetched and set
                if (updatedDataNode.getParent() != null) {
                    dataNodeRepository.findById(updatedDataNode.getParent().getUuid()).ifPresentOrElse(parent -> {
                        node.setParent(parent);
                    }, () -> {
                        throw new ObjectNotFoundException();
                    });
                }
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
