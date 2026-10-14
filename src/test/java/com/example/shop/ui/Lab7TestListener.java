package com.example.shop.ui;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lab 7: TestNG Listeners - Custom Reporting
 * 
 * ITestListener interface:
 * - onStart: truoc khi bat dau test
 * - onFinish: sau khi ket thuc test
 * - onTestStart: truoc moi test
 * - onTestSuccess: test pass
 * - onTestFailure: test fail
 * - onTestSkipped: test bi skip
 * - onTestFailedButWithinSuccessPercentage: test fail nhung van trong pham vi cho phep
 */
public class Lab7TestListener implements ITestListener {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static int skippedTests = 0;
    
    private static final String REPORT_FILE = "target/test-output/lab7-custom-report.txt";
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("[Lab 7] Test Suite STARTED: " + context.getName());
        System.out.println("Start Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("=".repeat(60));
        
        totalTests = 0;
        passedTests = 0;
        failedTests = 0;
        skippedTests = 0;
        
        // Tao thu muc bao cao
        new File("target/test-output").mkdirs();
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("[Lab 7] Test Suite FINISHED: " + context.getName());
        System.out.println("End Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("-".repeat(60));
        System.out.println("TOTAL:  " + totalTests);
        System.out.println("PASSED: " + passedTests);
        System.out.println("FAILED: " + failedTests);
        System.out.println("SKIPPED: " + skippedTests);
        System.out.println("=".repeat(60));
        
        // Ghi bao cao ra file
        writeReportToFile(context);
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        totalTests++;
        System.out.println("[START] " + result.getMethod().getMethodName() + 
                " (Group: " + result.getMethod().getGroups() + ")");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
        System.out.println("[PASS ] " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        System.out.println("[FAIL ] " + result.getMethod().getMethodName());
        
        // In stack trace neu co
        if (result.getThrowable() != null) {
            System.out.println("       Reason: " + result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
        System.out.println("[SKIP ] " + result.getMethod().getMethodName() + 
                " - " + (result.getThrowable() != null ? result.getThrowable().getMessage() : "N/A"));
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("[WARN ] " + result.getMethod().getMethodName() + 
                " - Failed but within success percentage");
    }
    
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
        System.out.println("[TIMEOUT] " + result.getMethod().getMethodName());
    }
    
    /**
     * Ghi bao cao ra file text
     */
    private void writeReportToFile(ITestContext context) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
            writer.println("=".repeat(60));
            writer.println("LAB 7 - CUSTOM TEST REPORT");
            writer.println("=".repeat(60));
            writer.println("Suite: " + context.getName());
            writer.println("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println("-".repeat(60));
            writer.println("TOTAL TESTS:  " + totalTests);
            writer.println("PASSED:       " + passedTests);
            writer.println("FAILED:       " + failedTests);
            writer.println("SKIPPED:      " + skippedTests);
            writer.println("PASS RATE:    " + (totalTests > 0 ? (passedTests * 100.0 / totalTests) : 0) + "%");
            writer.println("-".repeat(60));
            
            // Chi tiet test fail
            if (failedTests > 0) {
                writer.println("FAILED TESTS:");
                for (ITestResult fail : context.getFailedTests().getAllResults()) {
                    writer.println("  - " + fail.getMethod().getMethodName());
                    if (fail.getThrowable() != null) {
                        writer.println("    Error: " + fail.getThrowable().getMessage());
                    }
                }
            }
            
            writer.println("=".repeat(60));
            System.out.println("[Lab 7] Report saved to: " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("[Lab 7] Failed to write report: " + e.getMessage());
        }
    }
}
