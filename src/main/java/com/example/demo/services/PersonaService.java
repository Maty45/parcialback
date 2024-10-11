package com.example.demo.services;

import com.example.demo.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Persona;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }
    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(List<String> dna) {
        int sequenceCount = 0;
        int n = dna.size();

        // Crear y llenar la matriz
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna.get(i).toCharArray();
        }

        printMatrix(matrix);

        // Usar un Set para almacenar secuencias ya detectadas
        Set<String> detectedSequences = new HashSet<>();

        // Recorrer la matriz y verificar secuencias en cada dirección
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                // Verificar y contar secuencias
                if (hasHorizontalSequence(matrix, row, col)) {
                    detectedSequences.add("H" + row + col); // Añadir a conjunto
                    System.out.println("Secuencia horizontal detectada en fila " + row + ", columna " + col);
                }
                if (hasVerticalSequence(matrix, row, col)) {
                    detectedSequences.add("V" + row + col); // Añadir a conjunto
                    System.out.println("Secuencia vertical detectada en fila " + row + ", columna " + col);
                }
                if (hasDiagonalRightSequence(matrix, row, col)) {
                    detectedSequences.add("DR" + row + col); // Añadir a conjunto
                    System.out.println("Secuencia diagonal derecha detectada en fila " + row + ", columna " + col);
                }
                if (hasDiagonalLeftSequence(matrix, row, col)) {
                    detectedSequences.add("DL" + row + col); // Añadir a conjunto
                    System.out.println("Secuencia diagonal izquierda detectada en fila " + row + ", columna " + col);
                }

                // Si encontramos más de una secuencia, es mutante
                if (detectedSequences.size() > 1) {
                    return true;
                }
            }
        }
        return false; // No es mutante
    }

    private void printMatrix(char[][] matrix) {
        System.out.println("Matriz de ADN:");
        for (char[] row : matrix) {
            for (char col : row) {
                System.out.print(col + " ");
            }
            System.out.println();  // Salto de línea al final de cada fila
        }
    }

    // Verificar secuencia horizontal
    private boolean hasHorizontalSequence(char[][] matrix, int row, int col) {
        int n = matrix.length;
        if (col + SEQUENCE_LENGTH - 1 < n) {
            char initial = matrix[row][col];
            for (int i = 1; i < SEQUENCE_LENGTH; i++) {
                if (matrix[row][col + i] != initial) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // Verificar secuencia vertical
    private boolean hasVerticalSequence(char[][] matrix, int row, int col) {
        int n = matrix.length;
        if (row + SEQUENCE_LENGTH - 1 < n) {
            char initial = matrix[row][col];
            for (int i = 1; i < SEQUENCE_LENGTH; i++) {
                if (matrix[row + i][col] != initial) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // Verificar secuencia diagonal de izquierda a derecha
    private boolean hasDiagonalRightSequence(char[][] matrix, int row, int col) {
        int n = matrix.length;
        if (row + SEQUENCE_LENGTH - 1 < n && col + SEQUENCE_LENGTH - 1 < n) {
            char initial = matrix[row][col];
            for (int i = 1; i < SEQUENCE_LENGTH; i++) {
                if (matrix[row + i][col + i] != initial) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // Verificar secuencia diagonal de derecha a izquierda
    private boolean hasDiagonalLeftSequence(char[][] matrix, int row, int col) {
        int n = matrix.length;
        if (row + SEQUENCE_LENGTH - 1 < n && col - SEQUENCE_LENGTH + 1 >= 0) {
            char initial = matrix[row][col];
            for (int i = 1; i < SEQUENCE_LENGTH; i++) {
                if (matrix[row + i][col - i] != initial) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // Guardar la entidad Persona en la base de datos
    public void save(Persona persona) {
        System.out.println("Guardando ADN: " + persona.getDna());
        List<Persona> existingPersonas = personaRepository.findAll();

        for (Persona existing : existingPersonas) {
            System.out.println("Comparando con ADN existente: " + existing.getDna());
            if (existing.getDna().equals(persona.getDna())) {
                throw new IllegalArgumentException("A person with the same DNA already exists.");
            }
        }
        personaRepository.save(persona);
    }

    //contar mutantes y humanos normales
    public long countMutants() {
        return personaRepository.countByEsMutante(true);
    }
    public long countHumans() {
        return personaRepository.countByEsMutante(false);
    }
    public double calculateRatio(long countMutants, long countHumans) {
        try {
            if (countHumans == 0) {
                return countMutants > 0 ? 1.0 : 0.0; // Si no hay humanos pero hay mutantes, el ratio es 1.0
            }
            return (double) countMutants / countHumans; // Calcular ratio
        } catch (Exception e) {
            System.err.println("Error calculating ratio: " + e.getMessage());
            return 0.0;
        }
    }
} 