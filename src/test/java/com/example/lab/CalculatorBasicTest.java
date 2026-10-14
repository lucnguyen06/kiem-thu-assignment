package com.example.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Lab 1: Bai tap JUnit co ban
 * 
 * Cac noi dung:
 * 1. @Test, @BeforeEach, @AfterEach, @BeforeAll, @AfterAll
 * 2. Assertion methods (assertEquals, assertTrue, assertFalse, assertNull, assertNotNull, assertThrows...)
 * 3. @DisplayName - dat ten test dep hon
 * 4. @Disabled - bo qua test
 * 5. @Nested - nhom test long nhau
 * 6. @RepeatedTest - lap lai test nhieu lan
 * 7. TestInfo, TestReporter - truy cap thong tin test
 */
public class CalculatorBasicTest {

    // ==================== PHAN 1: CAC ANNOTATION CO BAN ====================
    
    /**
     * Lab 1 - Bai 1: @BeforeEach va @AfterEach
     * Moi test method deu chay setUp() truoc va tearDown() sau.
     */
    static class LifecycleTest {
        
        private static StringBuilder log = new StringBuilder();
        private Calculator calc;
        
        @BeforeAll
        static void beforeAll() {
            log.append("1. [BeforeAll] Chay 1 lan truoc tat ca test\n");
        }
        
        @BeforeEach
        void setUp() {
            calc = new Calculator();
            log.append("2. [BeforeEach] Tao Calculator moi\n");
        }
        
        @AfterEach
        void tearDown() {
            log.append("3. [AfterEach] Don dep sau test\n");
        }
        
        @AfterAll
        static void afterAll() {
            log.append("4. [AfterAll] Chay 1 lan sau tat ca test\n");
            System.out.println("\n=== Thuc tu chay cua vong doi ===");
            System.out.println(log.toString());
        }
        
        @Test
        void testAdd() {
            assertEquals(5, calc.add(2, 3));
        }
        
        @Test
        void testSubtract() {
            assertEquals(1, calc.subtract(3, 2));
        }
    }
    
    // ==================== PHAN 2: ASSERTION METHODS ====================
    
    /**
     * Lab 1 - Bai 2: Cac phuong thuc assertion
     */
    @Nested
    @DisplayName("Cac assertion co ban")
    class AssertionTests {
        
        private Calculator calc = new Calculator();
        
        @Test
        @DisplayName("assertEquals - kiem tra 2 gia tri bang nhau")
        void assertEqualsTest() {
            assertEquals(10, calc.add(4, 6), "4 + 6 phai bang 10");
            assertEquals(0, calc.add(-5, 5), "(-5) + 5 phai bang 0");
        }
        
        @Test
        @DisplayName("assertTrue va assertFalse")
        void assertTrueFalseTest() {
            assertTrue(calc.isEven(4), "4 phai la so chan");
            assertFalse(calc.isEven(3), "3 khong phai la so chan");
        }
        
        @Test
        @DisplayName("assertNull va assertNotNull")
        void assertNullTest() {
            String result = calc.divide(6, 2) == 3.0 ? "OK" : null;
            assertNotNull(result);
            
            String nullResult = null;
            assertTrue(nullResult == null);
        }
        
        @Test
        @DisplayName("assertSame - kiem tra cung 1 object")
        void assertSameTest() {
            String s1 = new String("test");
            String s2 = s1; // cung tham chieu
            assertSame(s1, s2, "Hai bien phai cung mot doi tuong");
        }
        
        @Test
        @DisplayName("assertNotEquals")
        void assertNotEqualsTest() {
            assertNotEquals(5, calc.add(2, 2), "2 + 2 khong bang 5");
        }
        
        @Test
        @DisplayName("assertThrows - kiem tra ngoai le")
        void assertThrowsTest() {
            // Khi chia cho 0 phai nem ArithmeticException
            ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> calc.divide(1, 0),
                "Chia cho 0 phai nem ArithmeticException"
            );
            assertEquals("Khong the chia cho 0", ex.getMessage());
        }
        
        @Test
        @DisplayName("assertDoesNotThrow - khi khong co ngoai le")
        void assertDoesNotThrowTest() {
            // Khong nem ngoai le - viet thu cong
            try {
                calc.divide(10, 2);
                // Neu khong nem ngoai le thi pass
                assertTrue(true);
            } catch (Exception e) {
                assertTrue(false, "Khong nen nem ngoai le");
            }
        }
        
        @Test
        @DisplayName("assertArrayEquals - kiem tra mang")
        void assertArrayEqualsTest() {
            int[] arr1 = {1, 2, 3};
            int[] arr2 = {1, 2, 3};
            assertEquals(arr1.length, arr2.length);
            for (int i = 0; i < arr1.length; i++) {
                assertEquals(arr1[i], arr2[i]);
            }
        }
    }
    
    // ==================== PHAN 3: @DisplayName ====================
    
    /**
     * Lab 1 - Bai 3: @DisplayName cho ten test dep hon
     */
    @Nested
    @DisplayName("Phep tinh Calculator")
    class CalculatorDisplayNameTests {
        
        private Calculator calc = new Calculator();
        
        @Test
        @DisplayName("Phep cong: 2 + 3 = 5")
        void addTwoPlusThree() {
            assertEquals(5, calc.add(2, 3));
        }
        
        @Test
        @DisplayName("Phep tru: 10 - 4 = 6")
        void subtractTenMinusFour() {
            assertEquals(6, calc.subtract(10, 4));
        }
        
        @Test
        @DisplayName("Phep nhan: 6 x 7 = 42")
        void multiplySixTimesSeven() {
            assertEquals(42, calc.multiply(6, 7));
        }
        
        @Test
        @DisplayName("Phep chia: 20 / 4 = 5.0")
        void divideTwentyByFour() {
            assertEquals(5.0, calc.divide(20, 4), 0.001);
        }
    }
    
    // ==================== PHAN 4: @Disabled ====================
    
    /**
     * Lab 1 - Bai 4: @Disabled - bo qua test
     */
    @Nested
    @DisplayName("Test bi vo hieu hoa")
    class DisabledTests {
        
        private Calculator calc = new Calculator();
        
        @Test
        @DisplayName("Test nay se chay binh thuong")
        void testWillRun() {
            assertEquals(5, calc.add(2, 3));
        }
        
        @Disabled("Tam thoi vo hieu hoa - can xem lai logic")
        @Test
        @DisplayName("Test nay bi bo qua")
        void testWillBeSkipped() {
            assertEquals(99, calc.multiply(9, 11));
        }
        
        @Disabled("Chua implement")
        @Test
        void testPendingFeature() {
            // Feature chua co - test bi bo qua
            assertTrue(false, "Chua implement");
        }
    }
    
    // ==================== PHAN 5: @Nested ====================
    
    /**
     * Lab 1 - Bai 5: @Nested - nhom test long nhau
     */
    @Nested
    @DisplayName("Kiem thu phep toan")
    class NestedArithmeticTests {
        
        private Calculator calc;
        
        @BeforeEach
        void setUp() {
            calc = new Calculator();
        }
        
        @Nested
        @DisplayName("Phep cong")
        class AddTests {
            
            @Test
            @DisplayName("Cong 2 so duong")
            void addPositive() {
                assertEquals(8, calc.add(3, 5));
            }
            
            @Test
            @DisplayName("Cong so am va duong")
            void addNegativeAndPositive() {
                assertEquals(2, calc.add(-3, 5));
            }
            
            @Test
            @DisplayName("Cong 2 so am")
            void addTwoNegatives() {
                assertEquals(-8, calc.add(-3, -5));
            }
        }
        
        @Nested
        @DisplayName("Phep chia")
        class DivideTests {
            
            @Test
            @DisplayName("Chia 2 so duong")
            void dividePositive() {
                assertEquals(3.5, calc.divide(7, 2), 0.001);
            }
            
            @Test
            @DisplayName("Chia cho 1")
            void divideByOne() {
                assertEquals(5.0, calc.divide(5, 1), 0.001);
            }
            
            @Test
            @DisplayName("Chia cho 0 - phai nem exception")
            void divideByZero() {
                assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
            }
        }
    }
    
    // ==================== PHAN 6: @RepeatedTest ====================
    
    /**
     * Lab 1 - Bai 6: @RepeatedTest - lap lai test
     */
    @Nested
    @DisplayName("Test lap lai")
    class RepeatedTests {
        
        private Calculator calc = new Calculator();
        
        @RepeatedTest(5)
        @DisplayName("Phep cong phai cho ket qua nhat quan")
        void repeatedAddTest() {
            // 2 + 2 luon = 4
            assertEquals(4, calc.add(2, 2));
        }
        
        @RepeatedTest(value = 3, name = "Lan {currentRepetion}/{totalRepetitions}")
        @DisplayName("Kiem tra isEven voi gia tri chan")
        void repeatedIsEvenTest() {
            assertTrue(calc.isEven(10));
        }
    }
    
    // ==================== PHAN 7: TestInfo, TestReporter ====================
    
    /**
     * Lab 1 - Bai 7: TestInfo va TestReporter
     */
    @Nested
    @DisplayName("Thong tin test")
    class TestInfoTests {
        
        private Calculator calc = new Calculator();
        
        @Test
        @DisplayName("Su dung TestInfo de lay thong tin test")
        void testWithTestInfo(TestInfo testInfo) {
            System.out.println("\n=== TestInfo Demo ===");
            System.out.println("Display Name: " + testInfo.getDisplayName());
            System.out.println("Tags: " + testInfo.getTags());
            System.out.println("Test Class: " + testInfo.getTestClass().map(Class::getName).orElse("N/A"));
            System.out.println("Test Method: " + testInfo.getTestMethod().map(m -> m.getName()).orElse("N/A"));
            
            assertNotNull(testInfo.getDisplayName());
            assertTrue(testInfo.getDisplayName().contains("TestInfo"));
        }
        
        @Test
        @DisplayName("Su dung TestReporter de ghi log")
        void testWithTestReporter(TestReporter testReporter) {
            // TestReporter cho phep ghi thong tin trong bao cao
            testReporter.publishEntry("Calculator Demo", "Kiem tra phep tinh co ban");
            testReporter.publishEntry("Ket qua", "2 + 3 = " + calc.add(2, 3));
            
            assertEquals(5, calc.add(2, 3));
        }
    }
    
    // ==================== PHAN 8: @ParameterizedTest ====================
    
    /**
     * Lab 1 - Bai 8: @ParameterizedTest co ban
     */
    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedBasicTests {
        
        private Calculator calc = new Calculator();
        
        @ParameterizedTest
        @ValueSource(ints = {0, 2, 4, 100, -2})
        @DisplayName("{0} phai la so chan")
        void isEvenParameterized(int number) {
            assertTrue(calc.isEven(number), number + " phai la so chan");
        }
        
        @ParameterizedTest
        @ValueSource(ints = {1, 3, 5, 99, -1})
        @DisplayName("{0} phai la so le")
        void isOddParameterized(int number) {
            assertFalse(calc.isEven(number), number + " phai la so le");
        }
        
        @ParameterizedTest
        @CsvSource({
            "2, 3, 5",
            "0, 0, 0",
            "-1, 1, 0",
            "10, 20, 30"
        })
        @DisplayName("{0} + {1} = {2}")
        void addParameterized(int a, int b, int expected) {
            assertEquals(expected, calc.add(a, b));
        }
        
        @ParameterizedTest
        @CsvSource({
            "10, 2, 5.0",
            "9, 4, 2.25",
            "7, 2, 3.5"
        })
        @DisplayName("{0} / {1} = {2}")
        void divideParameterized(double a, double b, double expected) {
            assertEquals(expected, calc.divide((int)a, (int)b), 0.01);
        }
    }
    
    // ==================== PHAN 9: @NullSource, @EmptySource ====================
    
    /**
     * Lab 1 - Bai 9: Test voi gia tri null va empty
     */
    @Nested
    @DisplayName("Test voi gia tri dac biet")
    class SpecialValueTests {
        
        private Calculator calc = new Calculator();
        
        // Chi test null
        @ParameterizedTest
        @NullSource
        @DisplayName("Test voi gia tri null")
        void testNull(Integer value) {
            assertTrue(value == null);
        }
        
        // Chi test empty string
        @ParameterizedTest
        @EmptySource
        @DisplayName("Test voi gia tri empty")
        void testEmpty(String value) {
            assertTrue(value != null && value.isEmpty());
        }
        
        // Test ca null va empty
        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Test voi gia tri null hoac empty")
        void testNullAndEmpty(String value) {
            assertTrue(value == null || value.isEmpty());
        }
    }
    
    // ==================== PHAN 10: @TestMethodOrder ====================
    
    /**
     * Lab 1 - Bai 10: Thuc thi test theo thu tu
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Test theo thu tu")
    class OrderedTests {
        
        private Calculator calc = new Calculator();
        
        @Test
        @Order(3)
        @DisplayName("Buoc 3: Phep nhan")
        void step3_Multiply() {
            assertEquals(6, calc.multiply(2, 3));
        }
        
        @Test
        @Order(1)
        @DisplayName("Buoc 1: Phep cong")
        void step1_Add() {
            assertEquals(5, calc.add(2, 3));
        }
        
        @Test
        @Order(2)
        @DisplayName("Buoc 2: Phep tru")
        void step2_Subtract() {
            assertEquals(1, calc.subtract(3, 2));
        }
    }
}
