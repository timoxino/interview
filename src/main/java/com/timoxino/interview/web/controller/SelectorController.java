package com.timoxino.interview.web.controller;

import com.timoxino.interview.web.dto.Selector;
import com.timoxino.interview.web.model.StoredRecord;
import com.timoxino.interview.web.service.SelectorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class SelectorController {

    private final SelectorService selectorService;

    public SelectorController(SelectorService selectorService) {
        this.selectorService = selectorService;
    }

    @PutMapping("/selector")
    StoredRecord update(@RequestBody Selector selector) {
        Selector parentSelector = selector.getBelongsTo().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Optional<StoredRecord> parent = selectorService.retrieveParent(parentSelector);
        return parent.orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
