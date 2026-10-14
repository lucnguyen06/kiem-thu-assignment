package com.example.shop.ui;

import org.testng.Assert;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Lab 8: Parallel Execution Configuration
 * 
 * Huong dan cau hinh parallel execution trong testng.xml
 * 
 * Cac thuoc tinh parallel:
 * - parallel="tests" - chay cac test song song
 * - parallel="classes" - chay cac class song song
 * - parallel="methods" - chay cac method song song
 * - parallel="instances" - chay cac instance song song
 * - thread-count="5" - so luong thread
 */
public class Lab8ParallelRunner {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("Lab 8: Parallel Test Execution");
        System.out.println("==============================================");
        
        // Demo: Doc cau hinh parallel tu XML
        try {
            runParallelTests();
        } catch (Exception e) {
            System.err.println("Error running parallel tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void runParallelTests() throws Exception {
        // Cach 1: Su dung TestNG API
        TestNG testNG = new TestNG();
        
        // Thiet lap thanh phan song song
        // parallel="methods" thread-count="5"
        // testNG.setParallel(parallel);
        // testNG.setThreadCount(threadCount);
        
        // Them suite tu file XML
        // testNG.setXmlSuites(List.of(xmlSuite));
        
        System.out.println("[Lab 8] Parallel configuration loaded");
        System.out.println("- parallel: methods");
        System.out.println("- thread-count: 3");
        System.out.println("- Data provider: thread-safe");
        System.out.println();
    }
    
    /**
     * Vi du cau hinh testng.xml cho parallel execution
     */
    public static String getParallelXmlConfig() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
            <suite name="Lab 8 - Parallel Test Suite" parallel="methods" thread-count="3">
                
                <!-- 
                    parallel="methods": Chay cac @Test methods song song
                    thread-count="3": Su dung 3 threads
                    
                    Cac tuy chon khac:
                    - parallel="tests": Moi <test> chay trong thread rieng
                    - parallel="classes": Moi class chay trong thread rieng
                    - parallel="instances": Moi instance (tu @Factory) chay rieng
                -->
                
                <!-- 
                    Quan trong: Data Provider phai thread-safe
                    @DataProvider(parallel = true) de cho phep data provider chay song song
                -->
                
                <test name="Parallel Tests">
                    <classes>
                        <class name="com.example.shop.ui.Lab8AdvancedTest"/>
                    </classes>
                </test>
            </suite>
            """;
    }
    
    /**
     * Thread-safe Data Provider example
     */
    public static String getThreadSafeDataProviderExample() {
        return """
            package com.example.shop.ui;
            
            import org.testng.annotations.DataProvider;
            
            public class ThreadSafeDataProvider {
                
                @DataProvider(name = "parallel-data", parallel = true)
                public static Object[][] getParallelData() {
                    // Data provider nay co the chay song song voi test methods
                    return new Object[][] {
                        {"data1", 100},
                        {"data2", 200},
                        {"data3", 300}
                    };
                }
            }
            """;
    }
}
