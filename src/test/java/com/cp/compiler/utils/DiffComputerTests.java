package com.cp.compiler.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DiffComputerTests {

    @Test
    void shouldReturnEmptyString() {
        // Given
        String str1 = "";
        String str2 = "";

        // When
        String diff = DiffComputer.diff(str1, str2);

        // Then
        Assertions.assertEquals("", diff);
    }

    @Test
    void shouldReturnEmptyStringIf1stParamIsNull() {
        // Given
        String str2 = "";

        // When
        String diff = DiffComputer.diff(null, str2);

        // Then
        Assertions.assertEquals("", diff);
    }

    @Test
    void shouldReturnEmptyStringIf2ndParamIsNull() {
        // Given
        String str1 = "";

        // When
        String diff = DiffComputer.diff(str1, null);

        // Then
        Assertions.assertEquals("", diff);
    }
}
