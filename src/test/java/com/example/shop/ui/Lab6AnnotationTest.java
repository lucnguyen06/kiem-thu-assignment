package com.example.shop.ui;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Lab 6: TestNG Annotations va Grouping
 * 
 * Cac annotation cua TestNG:
 * 1. @BeforeSuite / @AfterSuite
 * 2. @BeforeTest / @AfterTest
 * 3. @BeforeClass / @AfterClass
 * 4. @BeforeMethod / @AfterMethod
 * 5. @Test - test method
 * 
 * Grouping:
 * - groups: nhom test
 * - dependsOnMethods: phu thuoc test khac
 * - enabled: vo hieu hoa test
 */
public class Lab6AnnotationTest {

    // Bien static de theo doi thu tu chay
    private static StringBuilder executionLog = new StringBuilder();
    
    // ==================== SUITE LEVEL ====================
    
    @BeforeSuite
    public void beforeSuite() {
        executionLog.append("[BeforeSuite] Chay 1 lan truoc tat ca trong suite\n");
        System.out.println("[Lab 6] BeforeSuite - khoi tao moi truong");
    }
    
    @AfterSuite
    public void afterSuite() {
        executionLog.append("[AfterSuite] Chay 1 lan sau tat ca trong suite\n");
        System.out.println("\n[Lab 6] === Thuc tu thuc thi (Suite) ===");
        System.out.println(executionLog.toString());
        System.out.println("=== Ket thuc ===");
    }
    
    // ==================== TEST LEVEL ====================
    
    @BeforeTest
    public void beforeTest() {
        executionLog.append("[BeforeTest] Chay truoc moi <test> trong suite.xml\n");
    }
    
    @AfterTest
    public void afterTest() {
        executionLog.append("[AfterTest] Chay sau moi <test> trong suite.xml\n");
    }
    
    // ==================== CLASS LEVEL ====================
    
    @BeforeClass
    public void beforeClass() {
        executionLog.append("[BeforeClass] Chay 1 lan truoc tat ca test trong class nay\n");
        System.out.println("[Lab 6] BeforeClass - khoi tao resources cho class");
    }
    
    @AfterClass
    public void afterClass() {
        executionLog.append("[AfterClass] Chay 1 lan sau tat ca test trong class nay\n");
    }
    
    // ==================== METHOD LEVEL ====================
    
    @BeforeMethod
    public void beforeMethod() {
        executionLog.append("[BeforeMethod] Chay truoc moi test method\n");
    }
    
    @AfterMethod
    public void afterMethod() {
        executionLog.append("[AfterMethod] Chay sau moi test method\n");
    }
    
    // ==================== TEST METHODS ====================
    
    /**
     * Lab 6 - Bai 1: @Test co ban
     */
    @Test(groups = {"smoke", "auth"})
    public void testLoginValid() {
        executionLog.append("  [TEST] testLoginValid (smoke, auth)\n");
        Assert.assertTrue(true, "Login valid should pass");
    }
    
    @Test(groups = {"regression", "auth"})
    public void testLoginInvalidPassword() {
        executionLog.append("  [TEST] testLoginInvalidPassword (regression, auth)\n");
        Assert.assertTrue(true, "Invalid password should show error");
    }
    
    @Test(groups = {"smoke", "product"})
    public void testCreateProduct() {
        executionLog.append("  [TEST] testCreateProduct (smoke, product)\n");
        Assert.assertTrue(true, "Create product should pass");
    }
    
    @Test(groups = {"regression", "product"})
    public void testUpdateProduct() {
        executionLog.append("  [TEST] testUpdateProduct (regression, product)\n");
        Assert.assertTrue(true, "Update product should pass");
    }
    
    @Test(groups = {"regression", "product"})
    public void testDeleteProduct() {
        executionLog.append("  [TEST] testDeleteProduct (regression, product)\n");
        Assert.assertTrue(true, "Delete product should pass");
    }
    
    /**
     * Lab 6 - Bai 2: @Test voi dependsOnMethods
     */
    @Test(groups = {"e2e"}, dependsOnMethods = {"testLoginValid"})
    public void testNavigateToProducts() {
        executionLog.append("  [TEST] testNavigateToProducts (e2e, depends on testLoginValid)\n");
        Assert.assertTrue(true);
    }
    
    @Test(groups = {"e2e"}, dependsOnMethods = {"testNavigateToProducts"})
    public void testCreateFirstProduct() {
        executionLog.append("  [TEST] testCreateFirstProduct (e2e, depends on testNavigateToProducts)\n");
        Assert.assertTrue(true);
    }
    
    /**
     * Lab 6 - Bai 3: @Test voi enabled = false
     */
    @Test(groups = {"pending"}, enabled = false)
    public void testPendingFeature() {
        executionLog.append("  [TEST] testPendingFeature (pending, DISABLED)\n");
        Assert.fail("This test should not run");
    }
    
    @Test(groups = {"pending"}, enabled = false)
    public void testFeatureUnderDevelopment() {
        executionLog.append("  [TEST] testFeatureUnderDevelopment (pending, DISABLED)\n");
        Assert.fail("This test should not run");
    }
    
    /**
     * Lab 6 - Bai 4: @Test voi invocationCount va threadPoolSize
     */
    @Test(groups = {"performance"}, invocationCount = 3, threadPoolSize = 2)
    public void testConcurrentLogin() {
        executionLog.append("  [TEST] testConcurrentLogin (performance, invocationCount=3)\n");
        System.out.println("[Lab 6] Running concurrent test - Thread: " + Thread.currentThread().getName());
        Assert.assertTrue(true);
    }
    
    /**
     * Lab 6 - Bai 5: @Test voi expectedExceptions
     */
    @Test(groups = {"exception"}, expectedExceptions = ArithmeticException.class)
    public void testDivideByZero() {
        executionLog.append("  [TEST] testDivideByZero (exception)\n");
        int result = 10 / 0; // Nem ArithmeticException
        Assert.assertEquals(result, 0); // Dong nay khong chay
    }
    
    @Test(groups = {"exception"}, expectedExceptions = NullPointerException.class)
    public void testNullPointer() {
        executionLog.append("  [TEST] testNullPointer (exception)\n");
        String str = null;
        str.length(); // Nem NullPointerException
    }
    
    /**
     * Lab 6 - Bai 6: @Test voi timeOut
     */
    @Test(groups = {"performance"}, timeOut = 1000) // 1 giay timeout
    public void testQuickOperation() {
        executionLog.append("  [TEST] testQuickOperation (performance, timeOut=1s)\n");
        // Operation that should complete within 1 second
        for (int i = 0; i < 1000; i++) {
            Math.sqrt(i);
        }
        Assert.assertTrue(true);
    }
    
    /**
     * Lab 6 - Bai 7: Data Provider method
     */
    @Test(dataProvider = "userCredentials", dataProviderClass = Lab6DataProvider.class, 
          groups = {"smoke", "auth"})
    public void testLoginWithCredentials(String username, String password, boolean shouldPass) {
        executionLog.append("  [TEST] testLoginWithCredentials(" + username + ", " + shouldPass + ")\n");
        // Simulate login
        boolean loggedIn = shouldPass; // Mock: success if shouldPass is true
        Assert.assertEquals(loggedIn, shouldPass);
    }
    
    @Test(dataProvider = "productData", dataProviderClass = Lab6DataProvider.class,
          groups = {"regression", "product"})
    public void testProductCRUD(String name, double price, int qty, String operation) {
        executionLog.append("  [TEST] testProductCRUD(" + name + ", " + operation + ")\n");
        Assert.assertTrue(true);
    }
}
