package com.cp.compiler.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DiffComputerTests {

    @Test
    public void shouldReturnEmptyString() {
        // Given
        String str1 = "";
        String str2 = "";

        // When
        String diff = DiffComputer.diff(str1, str2);

        // Then
        Assertions.assertEquals("", diff);
    }

    @Test
    public void shouldReturnAddCharacter() {
        // Given
        String str1 = "test";
        String str2 = "testa";

        // When
        String diff = DiffComputer.diff(str1, str2);

        // Then
        Assertions.assertEquals("test<add>a</add>", diff);
    }

    @Test
    public void shouldReturnDeleteCharacter() {
        // Given
        String str1 = "testa";
        String str2 = "test";

        // When
        String diff = DiffComputer.diff(str1, str2);

        // Then
        Assertions.assertEquals("test<delete>a</delete>", diff);
    }

    @Test
    public void shouldThrowIf1stParamIsNull() {
        // Given
        String str2 = "";

        // When / Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> DiffComputer.diff(null, str2));
    }

    @Test
    public void shouldThrowIf2ndParamIsNull() {
        // Given
        String str1 = "";

        // When / Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> DiffComputer.diff(str1, null));
    }
}
