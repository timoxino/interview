package com.timoxino.interview.web.controller;

import com.timoxino.interview.exception.ParentDetailsMissingException;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.DataNodeRepository;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    DataNode create(@RequestBody DataNode dataNode) throws ParentDetailsMissingException {
        try {
            Assert.notNull(dataNode.getParent(), ParentDetailsMissingException.message);

            if (StringUtils.hasText(dataNode.getParent().getName())) {
                dataNodeRepository.findByName(dataNode.getParent().getName()).ifPresent(dataNode::setParent);
            }

            if (dataNode.getParent().getId() != null) {
                dataNodeRepository.findById(dataNode.getParent().getId()).ifPresent(dataNode::setParent);
            }

            dataNode =  dataNodeRepository.save(dataNode);
        } catch (IllegalArgumentException exception) {
            throw new ParentDetailsMissingException();
        }
        return dataNode;
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        dataNodeRepository.deleteById(id);
    }
}
