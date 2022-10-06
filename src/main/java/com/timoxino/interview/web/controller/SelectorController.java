package com.timoxino.interview.web.controller;

import com.timoxino.interview.web.dto.Selector;
import com.timoxino.interview.web.model.StoredRecord;
import com.timoxino.interview.web.service.SelectorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class SelectorController {

    final ResponseStatusException NOT_FOUND_EXCEPTION = new ResponseStatusException(HttpStatus.NOT_FOUND);
    private final SelectorService selectorService;

    public SelectorController(SelectorService selectorService) {
        this.selectorService = selectorService;
    }

    @PostMapping("/selector")
    StoredRecord retrieveAncestor(@RequestBody Selector selector) {
        Optional<StoredRecord> ancestor;
        try {
            Selector parentSelector = selector.getBelongsTo().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            ancestor = selectorService.retrieveStoredAncestor(parentSelector);
        } catch (NoSuchElementException noSuchElementException) {
            throw NOT_FOUND_EXCEPTION;
        }
        return ancestor.orElseThrow((() -> NOT_FOUND_EXCEPTION));
    }

    @PutMapping("/selector")
    StoredRecord update(@RequestBody Selector selector) {
        return null;
    }
}
