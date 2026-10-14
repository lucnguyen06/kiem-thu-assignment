package com.example.lab;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Lab 3 bai 2: xu ly ngoai le bang assertThrows / assertDoesNotThrow, kiem tra message. */
class CalculatorExceptionTest {

    private final Calculator calc = new Calculator();

    // --- ArithmeticException (chia cho 0) ---

    @Test
    @DisplayName("divide(10, 0) nem ArithmeticException")
    void should_Throw_When_DivideByZero() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }

    @Test
    @DisplayName("divide(0, 0) cung nem ArithmeticException, message ro rang")
    void should_ThrowWithMessage_When_ZeroDividedByZero() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> calc.divide(0, 0));
        assertEquals("Khong the chia cho 0", ex.getMessage());
    }

    @Test
    void should_NotThrow_When_DivideNonZero() {
        assertDoesNotThrow(() -> calc.divide(10, 3));
    }

    // --- ArithmeticException (tran so) ---

    @Test
    void should_ThrowOverflow_When_AddMaxValue() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> calc.add(Integer.MAX_VALUE, 1));
        assertTrue(ex.getMessage().contains("overflow"));
    }

    @Test
    void should_ThrowOverflow_When_SubtractBelowMin() {
        assertThrows(ArithmeticException.class, () -> calc.subtract(Integer.MIN_VALUE, 1));
    }

    // --- IllegalArgumentException (can bac hai so am) ---

    @Test
    void should_Throw_When_SqrtOfNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calc.sqrt(-4));
        assertEquals("Khong the lay can bac hai cua so am: -4", ex.getMessage());
    }

    @Test
    void should_Throw_When_SqrtOfMinValue() {
        assertThrows(IllegalArgumentException.class, () -> calc.sqrt(Integer.MIN_VALUE));
    }

    @Test
    void should_NotThrow_When_SqrtOfZeroOrPositive() {
        assertDoesNotThrow(() -> calc.sqrt(0));
        assertEquals(3, calc.sqrt(9));
    }
}
