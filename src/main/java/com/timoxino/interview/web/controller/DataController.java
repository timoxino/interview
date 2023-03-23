package com.timoxino.interview.web.controller;

import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.repo.DataNodeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class DataController {

    private final DataNodeRepository dataNodeRepository;

    public DataController(DataNodeRepository dataNodeRepository) {
        this.dataNodeRepository = dataNodeRepository;
    }

    @GetMapping
    List<DataNode> all() {
        //return dataNodeRepository.findAll();
        DataNode dataNode = new DataNode();
        dataNode.setId(777L);
        dataNode.setName("Test node");
        return Arrays.asList(dataNode);
    }

    @PostMapping
    DataNode create(@RequestBody DataNode occupation) {
        return dataNodeRepository.save(occupation);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        dataNodeRepository.deleteById(id);
    }
}
