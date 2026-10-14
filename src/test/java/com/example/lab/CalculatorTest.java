package com.example.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Lab 2 bai 3 + Lab 3 bai 1 (vong doi) + bai 3 (parameterized). */
class CalculatorTest {

    private Calculator calc;

    @BeforeAll
    static void beforeAll() {
        // static vi JUnit goi truoc khi tao bat ky instance nao (mac dinh moi test method 1 instance moi)
        System.out.println("[LIFECYCLE] @BeforeAll - chay 1 lan truoc tat ca");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("[LIFECYCLE] @AfterAll - chay 1 lan sau tat ca");
    }

    @BeforeEach
    void setUp() {
        calc = new Calculator();
        System.out.println("[LIFECYCLE]   @BeforeEach - tao Calculator moi");
    }

    @AfterEach
    void tearDown() {
        System.out.println("[LIFECYCLE]   @AfterEach - don dep");
    }

    @Test
    @DisplayName("add: hai so duong")
    void should_ReturnSum_When_AddTwoPositiveNumbers() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void should_ReturnSum_When_AddNegativeAndPositive() {
        assertEquals(-1, calc.add(-3, 2));
        assertNotEquals(0, calc.add(1, 1));
    }

    @Test
    void should_ThrowArithmeticException_When_AddOverflows() {
        assertThrows(ArithmeticException.class, () -> calc.add(Integer.MAX_VALUE, 1));
    }

    @ParameterizedTest(name = "{0} - {1} = {2}")
    @CsvSource({"5,3,2", "0,0,0", "-5,-3,-2", "3,5,-2", "0,7,-7"})
    void should_ReturnDifference_When_Subtract(int a, int b, int expected) {
        assertEquals(expected, calc.subtract(a, b));
    }

    @ParameterizedTest(name = "{0} * {1} = {2}")
    @CsvSource({"3,4,12", "0,99,0", "-3,4,-12", "-3,-4,12", "1,7,7"})
    void should_ReturnProduct_When_Multiply(int a, int b, int expected) {
        assertEquals(expected, calc.multiply(a, b));
    }

    @Test
    void should_ThrowArithmeticException_When_MultiplyOverflows() {
        assertThrows(ArithmeticException.class, () -> calc.multiply(Integer.MAX_VALUE, 2));
    }

    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({"10,2,5.0", "7,2,3.5", "-9,3,-3.0", "0,5,0.0", "1,3,0.3333333333333333"})
    void should_ReturnQuotient_When_Divide(int a, int b, double expected) {
        assertEquals(expected, calc.divide(a, b), 1e-9);
    }

    @Test
    void should_ThrowWithMessage_When_DivideByZero() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
        assertNotNull(ex.getMessage());
        assertEquals("Khong the chia cho 0", ex.getMessage());
    }

    @ParameterizedTest(name = "{0} la so chan")
    @ValueSource(ints = {0, 2, 4, -2, 1000})
    void should_ReturnTrue_When_NumberIsEven(int n) {
        assertTrue(calc.isEven(n));
    }

    @ParameterizedTest(name = "{0} la so le")
    @ValueSource(ints = {1, 3, -1, 999})
    void should_ReturnFalse_When_NumberIsOdd(int n) {
        assertFalse(calc.isEven(n));
    }
}
