package com.example.shop.ui;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Lab 8: Retry Analyzer
 * 
 * Cho phep retry test neu fail (dung cho flaky tests)
 */
public class Lab8RetryAnalyzer implements IRetryAnalyzer {

    private int maxRetryCount = 3;
    private int retryCount = 0;
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println("[Lab 8] Retrying test: " + result.getMethod().getMethodName() + 
                    " (Attempt " + retryCount + "/" + maxRetryCount + ")");
            return true;
        }
        System.out.println("[Lab 8] Max retries reached for: " + result.getMethod().getMethodName());
        return false;
    }
}
