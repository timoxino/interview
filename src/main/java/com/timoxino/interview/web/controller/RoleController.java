package com.timoxino.interview.web.controller;

import com.timoxino.interview.web.model.Role;
import com.timoxino.interview.web.repo.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    List<Role> all() {
        return roleRepository.findAll();
    }

    @PostMapping
    Role create(@RequestBody Role occupation) {
        return roleRepository.save(occupation);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        roleRepository.deleteById(id);
    }
}
