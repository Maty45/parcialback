package com.example.demo.controllers;

import com.example.demo.entities.Persona;
import com.example.demo.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/mutant")
@CrossOrigin(origins = "*")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @PostMapping("/")
    public ResponseEntity<String> detectMutant(@RequestBody Persona persona) {
        System.out.println("ADN recibido en el controlador: " + persona.getDna());  // Añadir esta línea para verificar el ADN

        try {
            boolean isMutant = personaService.isMutant(new ArrayList<>(persona.getDna()));

            persona.setEsMutante(isMutant);
            personaService.save(persona);

            if (isMutant) {
                return ResponseEntity.ok("Mutant detected");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a mutant");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}