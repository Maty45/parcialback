package com.example.demo;

import com.example.demo.entities.Persona;
import com.example.demo.repositories.PersonaRepository;
import com.example.demo.services.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DemoApplicationTests {

	private PersonaService personaService;
	private PersonaRepository personaRepository;
	@Autowired
	private DemoApplication demoApplication;

	@BeforeEach
	public void setUp() {
		personaRepository = mock(PersonaRepository.class);
		personaService = new PersonaService(personaRepository);
	}

	@Test
	void contextLoads() {
		demoApplication.iniciando();
	}

	// ADN mutante con secuencia diagonal derecha y vertical
	// Matriz 6x6
	@Test
	public void testDrV() {
		List<String> mutantDna = Arrays.asList(
				"ATGCGA",
				"CAGTGC",
				"TTATGT",
				"AGAAGG",
				"CCCCTA",
				"TCACTG"
		);

		assertTrue(personaService.isMutant(mutantDna));
	}

	// ADN mutante con secuencia diagonal izquierda y horizontal
	// Matriz 5x5
	@Test
	public void testDlH() {
		List<String> mutantDna = Arrays.asList(
				"ATCCA",
				"TTTAC",
				"ACATG",
				"CATTT",
				"GGGGA"
		);

		assertTrue(personaService.isMutant(mutantDna));
	}

	// Test ADN no mutante
	@Test
	public void testNonMutant() {
		List<String> humanDna = Arrays.asList(
				"ATGCAA",
				"CAGTGC",
				"TTATGT",
				"AGAAGT",
				"CCCTTA",
				"TCACTG"
		);

		assertFalse(personaService.isMutant(humanDna));
	}

	// Test ADN con matriz muy pequeña
	@Test
	public void testSmallMatrix() {
		List<String> smallDna = Arrays.asList(
				"ATG",
				"CAG",
				"TTA"
		);

		assertFalse(personaService.isMutant(smallDna)); // Debe devolver falso, porque no hay secuencias posibles.
	}

	// Test método save para almacenar ADN
	@Test
	public void testSaveUniqueDna() {
		Persona persona = new Persona();
		persona.setDna(Arrays.asList("ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"));
		persona.setEsMutante(true);

		when(personaRepository.findAll()).thenReturn(Collections.emptyList());

		personaService.save(persona);
		verify(personaRepository, times(1)).save(persona);
	}

	// Test que arroja error cuando el ADN ya existe
	@Test
	public void testSaveDuplicateDna() {
		Persona persona = new Persona();
		persona.setDna(Arrays.asList("ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"));

		Persona existing = new Persona();
		existing.setDna(Arrays.asList("ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"));

		when(personaRepository.findAll()).thenReturn(List.of(existing));

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			personaService.save(persona);
		});

		assertEquals("A person with the same DNA already exists.", exception.getMessage());
	}

	// Test contar mutantes
	@Test
	public void testCountMutants() {
		when(personaRepository.countByEsMutante(true)).thenReturn(10L);

		long count = personaService.countMutants();
		assertEquals(10L, count);
	}

	// Test contar humanos
	@Test
	public void testCountHumans() {
		when(personaRepository.countByEsMutante(false)).thenReturn(50L);

		long count = personaService.countHumans();
		assertEquals(50L, count);
	}

	// Test calcular ratio
	@Test
	public void testCalculateRatio() {
		long mutants = 10;
		long humans = 50;

		double ratio = personaService.calculateRatio(mutants, humans);
		assertEquals(0.2, ratio);
	}

	// Test calcular ratio sin humanos
	@Test
	public void testCalculateRatioNoHumans() {
		long mutants = 10;
		long humans = 0;

		double ratio = personaService.calculateRatio(mutants, humans);
		assertEquals(1.0, ratio); // Si no hay humanos pero hay mutantes, el ratio es 1.0
	}
}
