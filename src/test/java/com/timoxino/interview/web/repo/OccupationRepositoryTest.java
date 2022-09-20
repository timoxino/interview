package com.timoxino.interview.web.repo;

import com.timoxino.interview.web.model.Occupation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest()
class OccupationRepositoryTest {

    @Autowired
    OccupationRepository occupationRepository;

    @ParameterizedTest
    @ValueSource(strings = {"Delivery Manager"})
    void findByName(String input) {
        Occupation occupation = occupationRepository.findByName(input);
        assertNotNull("Occupation with name " + input + " must exist.", occupation);
    }

    @ParameterizedTest
    @CsvSource({"Project Management,Delivery Manager"})
    void findByCategoriesName(String input, String expected) {
        List<Occupation> occupations = occupationRepository.findByCategoriesName(input);
        assertFalse("Occupation with category " + input + " must be found.", occupations.isEmpty());
        assertEquals("Occupation with name " + expected + " must exist.", expected, occupations.get(0).getName());
    }
}