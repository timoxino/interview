package com.timoxino.interview.web.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest()
class OccupationRepositoryTest {

    @Autowired
    DataNodeRepository occupationRepository;

    /*@ParameterizedTest
    @ValueSource(strings = {"Delivery Manager"})
    void findByName(String input) {
        Optional<ContainerRecord> optional = occupationRepository.findByName(input);
        assertTrue("Occupation with name " + input + " must exist.", optional.isPresent());
    }

    @ParameterizedTest
    @CsvSource({"Project Management,Delivery Manager"})
    void findByCategoriesName(String input, String expected) {
        List<Occupation> occupations = occupationRepository.findByRolesName(input);
        assertFalse("Occupation with category " + input + " must be found.", occupations.isEmpty());
        assertEquals("Occupation with name " + expected + " must exist.", expected, occupations.get(0).getName());
    }

    @Test
    void createOccupationCategory(){
        Occupation entity = new Occupation("Project Manager");
        entity.setRoles(new HashSet<>());
        entity.getRoles().add(new Role("People Management"));
        occupationRepository.save(entity);
        assertNotNull("Id must be assigned", entity.getId());
    }

    @Test
    void deleteOccupationCategory() {
        Optional<Occupation> optional = occupationRepository.findByName("Project Manager");
        optional.ifPresent(record -> occupationRepository.delete(record));
        Optional<ContainerRecord> deletedoptional = occupationRepository.findByName("Project Manager");
        assertTrue("Occupation must not be found", deletedoptional.isEmpty());
    }*/
}