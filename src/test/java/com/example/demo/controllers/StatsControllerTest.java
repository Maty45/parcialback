package com.example.demo.controllers;

import com.example.demo.services.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaService personaService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetStats_WithValidData() throws Exception {
        // Arrange: Mock the personaService responses
        when(personaService.countMutants()).thenReturn(40L);
        when(personaService.countHumans()).thenReturn(100L);
        when(personaService.calculateRatio(40L, 100L)).thenReturn(0.4);

        // Act & Assert: Perform the GET request and check the response
        mockMvc.perform(get("/stats/"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.count_mutants").value(40)) // Check count_mutants
                .andExpect(jsonPath("$.count_humans").value(100)) // Check count_humans
                .andExpect(jsonPath("$.ratio").value(0.4)); // Check ratio
    }

    @Test
    public void testGetStats_WithNoHumans() throws Exception {
        // Arrange: Mock the service responses where there are no humans
        when(personaService.countMutants()).thenReturn(10L);
        when(personaService.countHumans()).thenReturn(0L);
        when(personaService.calculateRatio(10L, 0L)).thenReturn(1.0); // Assume ratio is 1.0 when no humans exist

        // Act & Assert: Perform the GET request and check the response
        mockMvc.perform(get("/stats/"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.count_mutants").value(10)) // Check count_mutants
                .andExpect(jsonPath("$.count_humans").value(0)) // Check count_humans
                .andExpect(jsonPath("$.ratio").value(1.0)); // Check ratio
    }

    @Test
    public void testGetStats_WithNoMutantsAndHumans() throws Exception {
        // Arrange: Mock the service responses with no mutants or humans
        when(personaService.countMutants()).thenReturn(0L);
        when(personaService.countHumans()).thenReturn(0L);
        when(personaService.calculateRatio(0L, 0L)).thenReturn(0.0); // No mutants or humans, ratio is 0

        // Act & Assert: Perform the GET request and check the response
        mockMvc.perform(get("/stats/"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.count_mutants").value(0)) // Check count_mutants
                .andExpect(jsonPath("$.count_humans").value(0)) // Check count_humans
                .andExpect(jsonPath("$.ratio").value(0.0)); // Check ratio
    }
}
