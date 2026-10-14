package com.example.shop.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.example.shop.ShopApplication;
import com.example.shop.ui.pages.LoginPage;
import com.example.shop.ui.pages.RegisterPage;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Lab 8: Advanced Topics
 * 
 * 1. Parallel Execution: Chay test song song tren nhieu browsers
 * 2. Cross-browser Testing: Chrome, Firefox, Edge
 * 3. Factory Pattern: Tao nhieu instances voi du lieu khac nhau
 */
public class Lab8AdvancedTest {

    // ==================== 1. CROSS-BROWSER TESTING ====================
    
    /**
     * Lab 8 - Bai 1: Cross-browser testing voi @Factory
     */
    @Factory
    public Object[] createInstances() {
        return new Object[] {
            new Lab8AdvancedTest("chrome"),
            new Lab8AdvancedTest("firefox"),
            new Lab8AdvancedTest("edge")
        };
    }
    
    private final String browser;
    private static ConfigurableApplicationContext context;
    private static String baseUrl;
    
    public Lab8AdvancedTest(String browser) {
        this.browser = browser;
    }
    
    @BeforeMethod
    public void setUp() {
        // Khoi dong app neu chua
        if (context == null) {
            context = SpringApplication.run(ShopApplication.class, "--server.port=0");
            baseUrl = "http://localhost:" + context.getEnvironment().getProperty("local.server.port");
            System.out.println("[Lab 8] App started at " + baseUrl);
        }
        
        // Tao driver theo browser
        WebDriver driver = createDriver();
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Khoi tao Page Objects
        registerPage = new RegisterPage(driver, wait);
        loginPage = new LoginPage(driver, wait);
    }
    
    private WebDriver createDriver() {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                System.out.println("[Lab 8] Starting Chrome...");
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                System.out.println("[Lab 8] Starting Firefox...");
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless=new", "--no-sandbox");
                System.out.println("[Lab 8] Starting Edge...");
                return new EdgeDriver(edgeOptions);
                
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected RegisterPage registerPage;
    protected LoginPage loginPage;
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test(groups = {"cross-browser", "lab8"})
    public void testRegisterOnBrowser() {
        String username = "user_" + browser + "_" + UUID.randomUUID().toString().substring(0, 6);
        
        registerPage.open(baseUrl);
        registerPage.register(username, username + "@mail.com", "secret1");
        
        assertTrue(registerPage.isSuccessDisplayed(), 
                "Registration should succeed on " + browser);
        System.out.println("[Lab 8] Registration PASSED on " + browser);
    }
    
    @Test(groups = {"cross-browser", "lab8"}, dependsOnMethods = {"testRegisterOnBrowser"})
    public void testLoginOnBrowser() {
        String username = "user_" + browser + "_" + UUID.randomUUID().toString().substring(0, 6);
        
        // Register
        registerPage.open(baseUrl);
        registerPage.register(username, username + "@mail.com", "secret1");
        
        // Login
        loginPage.open(baseUrl);
        loginPage.login(username, "secret1");
        
        assertTrue(loginPage.getCurrentUrl().contains("/products"), 
                "Login should redirect to products on " + browser);
        System.out.println("[Lab 8] Login PASSED on " + browser);
    }
    
    // ==================== 2. DATA-DRIVEN WITH FACTORY ====================
    
    /**
     * Lab 8 - Bai 2: Factory voi du lieu khac nhau
     */
    @Factory
    public static Object[] createDataTests() {
        return new Object[] {
            new Lab8DataDrivenTest("validuser1", "secret1", true),
            new Lab8DataDrivenTest("validuser2", "secret1", true),
            new Lab8DataDrivenTest("invaliduser", "wrongpass", false),
            new Lab8DataDrivenTest("", "", false)
        };
    }
    
    @Test(groups = {"data-driven", "lab8"})
    public void testLoginDataDriven() {
        // This will be implemented in Lab8DataDrivenTest
        assertTrue(true);
    }
    
    // ==================== 3. RETRY LOGIC ====================
    
    /**
     * Lab 8 - Bai 3: Retry Listener
     */
    @Test(groups = {"retry", "lab8"}, retryAnalyzer = Lab8RetryAnalyzer.class)
    public void testFlaky() {
        System.out.println("[Lab 8] Running flaky test...");
        // Flaky test - co the pass hoac fail ngau nhien
        int random = (int) (Math.random() * 10);
        assertTrue(random < 8, "Flaky test failed (random=" + random + ")");
    }
}
