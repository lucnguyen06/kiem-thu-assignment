package com.example.shop.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lab 7: Logging - Custom Logger
 * 
 * Tao logger tay de ghi log ra file
 */
public class Lab7Logger {

    private static final String LOG_FILE = "target/test-output/lab7-test.log";
    private static PrintWriter logWriter;
    
    /**
     * Khoi tao logger
     */
    public static void initLogger() {
        try {
            new File("target/test-output").mkdirs();
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true)); // append mode
            log("INFO", "Logger initialized at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    /**
     * Dong logger
     */
    public static void closeLogger() {
        if (logWriter != null) {
            log("INFO", "Logger closed");
            logWriter.close();
        }
    }
    
    /**
     * Ghi log
     */
    public static void log(String level, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String logLine = String.format("[%s] [%s] %s", timestamp, level, message);
        
        System.out.println(logLine);
        
        if (logWriter != null) {
            logWriter.println(logLine);
            logWriter.flush();
        }
    }
    
    public static void info(String message) { log("INFO", message); }
    public static void debug(String message) { log("DEBUG", message); }
    public static void warn(String message) { log("WARN", message); }
    public static void error(String message) { log("ERROR", message); }
    public static void error(String message, Throwable t) { 
        log("ERROR", message + " - Exception: " + t.getMessage());
    }
    
    // ==================== DEMO TESTS ====================
    
    @Test(groups = {"smoke", "lab7"})
    public void testLoggerInfo() {
        Lab7Logger.initLogger();
        Lab7Logger.info("Test started: testLoggerInfo");
        
        // Do something
        String result = "Hello World";
        Lab7Logger.info("Calculated result: " + result);
        
        Assert.assertNotNull(result);
        Lab7Logger.info("Test PASSED");
        Lab7Logger.closeLogger();
    }
    
    @Test(groups = {"regression", "lab7"})
    public void testLoggerWithException() {
        Lab7Logger.initLogger();
        Lab7Logger.info("Test started: testLoggerWithException");
        
        try {
            // Simulate some operation
            int[] arr = {1, 2, 3};
            Lab7Logger.debug("Array length: " + arr.length);
            
            // Intentionally cause exception
            int x = arr[10]; // ArrayIndexOutOfBoundsException
            Lab7Logger.error("This should not be reached");
        } catch (ArrayIndexOutOfBoundsException e) {
            Lab7Logger.error("Caught ArrayIndexOutOfBoundsException", e);
            // Expected - test should still pass
        }
        
        Lab7Logger.info("Test completed successfully");
        Lab7Logger.closeLogger();
        
        Assert.assertTrue(true);
    }
    
    @Test(groups = {"smoke", "lab7"})
    public void testDataProcessing() {
        Lab7Logger.initLogger();
        Lab7Logger.info("Starting data processing test");
        
        Lab7Logger.info("Step 1: Initialize data");
        int[] data = {10, 20, 30, 40, 50};
        
        Lab7Logger.info("Step 2: Calculate sum");
        int sum = 0;
        for (int d : data) {
            sum += d;
            Lab7Logger.debug("Added " + d + ", current sum: " + sum);
        }
        Lab7Logger.info("Total sum: " + sum);
        
        Lab7Logger.info("Step 3: Calculate average");
        double avg = (double) sum / data.length;
        Lab7Logger.info("Average: " + avg);
        
        Assert.assertEquals(sum, 150);
        Assert.assertEquals(avg, 30.0, 0.001);
        
        Lab7Logger.info("Test PASSED");
        Lab7Logger.closeLogger();
    }
}
