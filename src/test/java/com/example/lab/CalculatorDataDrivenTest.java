package com.example.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Lab 5: Data-Driven Testing
 * 
 * Cac nguon du lieu (Data Sources):
 * 1. @ValueSource - mang gia tri don
 * 2. @CsvSource - bang CSV inline
 * 3. @CsvFileSource - doc tu file CSV
 * 4. @EnumSource - cac gia tri cua Enum
 * 5. @MethodSource - phuong thuc cung cap du lieu
 */
public class CalculatorDataDrivenTest {

    // ==================== 1. @ValueSource ====================
    
    /**
     * Lab 5 - Bai 1: @ValueSource
     * Cung cap mang gia tri don le (primitives, String, etc.)
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 100, -2, 1000})
    @DisplayName("{0} phai la so chan")
    void isEven_WithValueSource(int number) {
        Calculator calc = new Calculator();
        assertTrue(calc.isEven(number), number + " should be even");
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"abc", "test", "hello", "java", "selenium"})
    @DisplayName("Username '{0}' co do dai hop le")
    void testUsernameLength(String username) {
        int length = username.length();
        assertTrue(length >= 3 && length <= 50, 
                "Username '" + username + "' co do dai " + length + " phai trong khoang 3-50");
    }
    
    // ==================== 2. @CsvSource ====================
    
    /**
     * Lab 5 - Bai 2: @CsvSource
     * Cung cap du lieu dang bang (comma-separated)
     */
    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
        "0, 0, 0",
        "1, 2, 3",
        "10, 20, 30",
        "-5, 3, -2",
        "100, -100, 0"
    })
    @DisplayName("Phep cong: {0} + {1} = {2}")
    void add_WithCsvSource(int a, int b, int expected) {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.add(a, b));
    }
    
    @ParameterizedTest(name = "{0} - {1} = {2}")
    @CsvSource({
        "10, 5, 5",
        "0, 0, 0",
        "3, 5, -2",
        "-5, -3, -2"
    })
    @DisplayName("Phep tru: {0} - {1} = {2}")
    void subtract_WithCsvSource(int a, int b, int expected) {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.subtract(a, b));
    }
    
    @ParameterizedTest(name = "{0} * {1} = {2}")
    @CsvSource({
        "3, 4, 12",
        "0, 100, 0",
        "-2, 5, -10",
        "-3, -4, 12"
    })
    @DisplayName("Phep nhan: {0} * {1} = {2}")
    void multiply_WithCsvSource(int a, int b, int expected) {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.multiply(a, b));
    }
    
    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvSource({
        "10, 2, 5.0",
        "7, 2, 3.5",
        "0, 5, 0.0",
        "-9, 3, -3.0"
    })
    @DisplayName("Phep chia: {0} / {1} = {2}")
    void divide_WithCsvSource(int a, int b, double expected) {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.divide(a, b), 0.001);
    }
    
    // ==================== 3. @CsvFileSource ====================
    
    /**
     * Lab 5 - Bai 3: @CsvFileSource
     * Doc du lieu tu file CSV
     * File: src/test/resources/data/calculator-data.csv
     */
    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvFileSource(resources = "/data/calculator-add.csv", numLinesToSkip = 1)
    @DisplayName("Phep cong (tu file CSV): {0} + {1} = {2}")
    void add_WithCsvFile(int a, int b, int expected) {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.add(a, b));
    }
    
    @ParameterizedTest(name = "{0} / {1} = {2}")
    @CsvFileSource(resources = "/data/calculator-divide.csv", numLinesToSkip = 1)
    @DisplayName("Phep chia (tu file CSV): {0} / {1} = {2}")
    void divide_WithCsvFile(int a, int b, double expected) {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.divide(a, b), 0.001);
    }
    
    // ==================== 4. @EnumSource ====================
    
    /**
     * Lab 5 - Bai 4: @EnumSource
     * Test voi cac gia tri cua Enum
     */
    enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
    
    @ParameterizedTest
    @EnumSource(Operation.class)
    @DisplayName("Enum Operation co gia tri: {0}")
    void testEnumValues(Operation op) {
        assertNotNull(op);
        assertNotNull(op.name());
        System.out.println("Testing operation: " + op);
    }
    
    enum ValidationType {
        USERNAME_TOO_SHORT,
        USERNAME_TOO_LONG,
        PASSWORD_TOO_SHORT,
        EMAIL_INVALID,
        VALID
    }
    
    @ParameterizedTest
    @EnumSource(value = ValidationType.class, names = {"USERNAME_TOO_SHORT", "PASSWORD_TOO_SHORT"})
    @DisplayName("Test validation types: {0}")
    void testValidationEnum(ValidationType type) {
        assertNotNull(type);
        System.out.println("Testing validation: " + type);
    }
    
    // ==================== 5. @MethodSource ====================
    
    /**
     * Lab 5 - Bai 5: @MethodSource
     * Phuong thuc tinh toan cung cap du lieu
     */
    static List<Integer> evenNumbers() {
        return List.of(0, 2, 4, 6, 8, 10, 100, -2, -100);
    }
    
    static List<Integer> oddNumbers() {
        return List.of(1, 3, 5, 7, 9, 99, -1, -99);
    }
    
    @ParameterizedTest
    @MethodSource("evenNumbers")
    @DisplayName("{0} phai la so chan (MethodSource)")
    void isEven_WithMethodSource(int number) {
        Calculator calc = new Calculator();
        assertTrue(calc.isEven(number));
    }
    
    @ParameterizedTest
    @MethodSource("oddNumbers")
    @DisplayName("{0} phai la so le (MethodSource)")
    void isOdd_WithMethodSource(int number) {
        Calculator calc = new Calculator();
        assertFalse(calc.isEven(number));
    }
    
    // MethodSource voi nhieu tham so
    static List<ArithmeticCase> arithmeticCases() {
        return List.of(
            new ArithmeticCase(10, 5, "add", 15),
            new ArithmeticCase(10, 5, "subtract", 5),
            new ArithmeticCase(10, 5, "multiply", 50),
            new ArithmeticCase(10, 5, "divide", 2.0)
        );
    }
    
    record ArithmeticCase(int a, int b, String operation, Object expected) {}
    
    @ParameterizedTest
    @MethodSource("arithmeticCases")
    @DisplayName("{0} {2} {1} = {3}")
    void arithmetic_WithMethodSource(ArithmeticCase testCase) {
        Calculator calc = new Calculator();
        switch (testCase.operation) {
            case "add" -> assertEquals((Integer)testCase.expected, calc.add(testCase.a, testCase.b));
            case "subtract" -> assertEquals((Integer)testCase.expected, calc.subtract(testCase.a, testCase.b));
            case "multiply" -> assertEquals((Integer)testCase.expected, calc.multiply(testCase.a, testCase.b));
            case "divide" -> assertEquals((Double)testCase.expected, calc.divide(testCase.a, testCase.b), 0.001);
        }
    }
    
    // ==================== 6. Custom ArgumentsProvider ====================
    
    /**
     * Lab 5 - Bai 6: Test voi Null va Empty
     */
    @ParameterizedTest
    @CsvSource({
        "'', 'Email khong duoc rong'",
        "'   ', 'Email khong duoc rong'",
        "'abc', 'Email khong hop le'",
        "'a@b', 'Email khong hop le'",
        "'@b.com', 'Email khong hop le'",
        "'a b@c.com', 'Email khong hop le'"
    })
    @DisplayName("Validation email '{0}': {1}")
    void emailValidation(String email, String expectedError) {
        // Mock validation logic
        boolean isValid = email != null && !email.isBlank() && 
                email.contains("@") && !email.contains(" ") &&
                email.indexOf("@") > 0 && email.indexOf("@") < email.length() - 1;
        if (!isValid) {
            assertNotNull(expectedError);
        }
    }
    
    // ==================== 7. Bang quyet dinh (Decision Table) ====================
    
    /**
     * Lab 5 - Bai 7: Decision Table - Login
     * usernameExists x passwordCorrect -> result
     */
    @ParameterizedTest
    @CsvSource({
        "true, true, 'products'",
        "true, false, 'error'",
        "false, true, 'error'",
        "false, false, 'error'"
    })
    @DisplayName("Login: exists={0}, correct={1} -> {2}")
    void loginDecisionTable(boolean usernameExists, boolean passwordCorrect, String expected) {
        // simulate login logic
        boolean shouldSucceed = usernameExists && passwordCorrect;
        String actual = shouldSucceed ? "products" : "error";
        assertEquals(expected, actual);
    }
    
    // ==================== 8. Boundary Value Analysis ====================
    
    /**
     * Lab 5 - Bai 8: Boundary Value Analysis
     * Test cac gia tri ranh gioi
     */
    @ParameterizedTest
    @CsvSource({
        "2, false, 'Username qua ngan'",      // Boundary: 2 < 3 (min)
        "3, true, 'Username hop le'",            // Boundary: 3 == min
        "50, true, 'Username hop le'",           // Boundary: 50 == max
        "51, false, 'Username qua dai'"         // Boundary: 51 > 50 (max)
    })
    @DisplayName("Username boundary: do dai {0} -> {2}")
    void usernameBoundary(int length, boolean shouldBeValid, String description) {
        boolean isValid = length >= 3 && length <= 50;
        assertEquals(shouldBeValid, isValid, "Test: " + description);
    }
    
    @ParameterizedTest
    @CsvSource({
        "5, false, 'Password qua ngan'",         // Boundary: 5 < 6 (min)
        "6, true, 'Password hop le'",            // Boundary: 6 == min
        "100, true, 'Password hop le'"          // No upper limit mentioned
    })
    @DisplayName("Password boundary: do dai {0} -> {2}")
    void passwordBoundary(int length, boolean shouldBeValid, String description) {
        boolean isValid = length >= 6;
        assertEquals(shouldBeValid, isValid, "Test: " + description);
    }
}
