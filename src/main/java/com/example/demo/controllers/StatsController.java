
package com.example.demo.controllers;

import com.example.demo.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {
    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public Map<String, Object> getStats() {
        long countMutants = personaService.countMutants();
        long countHumans = personaService.countHumans();
        double ratio = personaService.calculateRatio(countMutants, countHumans);

        Map<String, Object> stats = new HashMap<>();
        stats.put("count_mutants", countMutants);
        stats.put("count_humans", countHumans);
        stats.put("ratio", ratio);

        return stats;
    }
}
