package com.example.shop.ui;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Lab 7: TestNG Listeners - Test Class
 * 
 * Su dung @Listeners de dam bao listener duoc goi
 */
@Listeners(Lab7TestListener.class)
public class Lab7ListenerTest {

    @Test(groups = {"smoke", "lab7"})
    public void testPass1() {
        Assert.assertTrue(true, "Test should pass");
    }
    
    @Test(groups = {"smoke", "lab7"})
    public void testPass2() {
        Assert.assertEquals(2 + 2, 4, "Math should work");
    }
    
    @Test(groups = {"smoke", "lab7"})
    public void testPass3() {
        Assert.assertNotNull("Hello", "String should not be null");
    }
    
    @Test(groups = {"regression", "lab7"})
    public void testFail1() {
        Assert.fail("This test intentionally fails for demonstration");
    }
    
    @Test(groups = {"regression", "lab7"})
    public void testFail2() {
        Assert.assertEquals(1, 2, "1 should not equal 2");
    }
    
    @Test(groups = {"smoke", "lab7"}, dependsOnMethods = {"testFail1"})
    public void testSkipped() {
        // Test nay se bi skip vi phu thuoc vao testFail1
        Assert.assertTrue(true);
    }
    
    @Test(groups = {"smoke", "lab7"})
    public void testArithmetic() {
        int a = 10, b = 5;
        Assert.assertEquals(a + b, 15);
        Assert.assertEquals(a - b, 5);
        Assert.assertEquals(a * b, 50);
        Assert.assertEquals(a / b, 2);
    }
}
