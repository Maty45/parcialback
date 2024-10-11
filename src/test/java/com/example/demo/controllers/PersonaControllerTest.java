package com.example.demo.controllers;

import com.example.demo.entities.Persona;
import com.example.demo.services.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PersonaControllerTest {

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private PersonaController personaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDetectMutant_whenMutant() {
        // Mocking DNA sequence for a mutant
        Persona persona = new Persona();
        persona.setDna(Arrays.asList("ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"));

        // Mocking the service to return true
        when(personaService.isMutant(any(ArrayList.class))).thenReturn(true);

        // Calling the method and asserting the response
        ResponseEntity<String> response = personaController.detectMutant(persona);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mutant detected", response.getBody());
    }

    @Test
    void testDetectMutant_whenNotMutant() {
        // Mocking DNA sequence for a non-mutant
        Persona persona = new Persona();
        persona.setDna(Arrays.asList("ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"));

        // Mocking the service to return false
        when(personaService.isMutant(any(ArrayList.class))).thenReturn(false);

        // Calling the method and asserting the response
        ResponseEntity<String> response = personaController.detectMutant(persona);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not a mutant", response.getBody());
    }

    @Test
    void testDetectMutant_whenInvalidDNA() {
        // Mocking invalid DNA sequence
        Persona persona = new Persona();
        persona.setDna(Arrays.asList("ATG", "CAGTGC", "TTATTT"));

        // Mocking the service to throw an exception
        when(personaService.isMutant(any(ArrayList.class))).thenThrow(new IllegalArgumentException("Invalid DNA sequence"));

        // Calling the method and asserting the response
        ResponseEntity<String> response = personaController.detectMutant(persona);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Invalid DNA sequence", response.getBody());
    }
}
