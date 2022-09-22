package com.timoxino.interview.web.controller;

import com.timoxino.interview.web.model.Occupation;
import com.timoxino.interview.web.repo.OccupationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OccupationController {

    private final OccupationRepository occupationRepository;

    public OccupationController(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    @GetMapping("/occupations")
    List<Occupation> all() {
        return occupationRepository.findAll();
    }

    @PostMapping("/occupations")
    Occupation create(@RequestBody Occupation occupation) {
        return occupationRepository.save(occupation);
    }

    @DeleteMapping("/occupations/{id}")
    void delete(@PathVariable Long id) {
        occupationRepository.deleteById(id);
    }
}
