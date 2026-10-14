package com.example.shop.ui;

import com.example.shop.ShopApplication;
import com.example.shop.ui.pages.LoginPage;
import com.example.shop.ui.pages.RegisterPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Lab 8: Data-Driven Test voi Factory Pattern
 */
public class Lab8DataDrivenTest {

    private final String username;
    private final String password;
    private final boolean shouldPass;
    
    private static ConfigurableApplicationContext context;
    private static String baseUrl;
    
    private WebDriver driver;
    private WebDriverWait wait;
    private RegisterPage registerPage;
    private LoginPage loginPage;
    
    @Factory
    public static Object[] createDataTests() {
        return new Object[] {
            new Lab8DataDrivenTest("validuser1", "secret1", true),
            new Lab8DataDrivenTest("validuser2", "secret1", true),
            new Lab8DataDrivenTest("invaliduser", "wrongpass", false),
            new Lab8DataDrivenTest("", "", false),
            new Lab8DataDrivenTest("nouser", "secret1", false)
        };
    }
    
    public Lab8DataDrivenTest(String username, String password, boolean shouldPass) {
        this.username = username;
        this.password = password;
        this.shouldPass = shouldPass;
    }
    
    @BeforeMethod
    public void setUp() {
        if (context == null) {
            context = SpringApplication.run(ShopApplication.class, "--server.port=0");
            baseUrl = "http://localhost:" + context.getEnvironment().getProperty("local.server.port");
        }
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        registerPage = new RegisterPage(driver, wait);
        loginPage = new LoginPage(driver, wait);
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test(groups = {"data-driven", "lab8"})
    public void testLoginScenario() {
        System.out.println("[Lab 8 DD] Testing: user='" + username + "', pass='" + 
                password + "', shouldPass=" + shouldPass);
        
        if (shouldPass) {
            // Register first
            String uniqueUser = username + "_" + UUID.randomUUID().toString().substring(0, 4);
            registerPage.open(baseUrl);
            registerPage.register(uniqueUser, uniqueUser + "@mail.com", password);
            assertTrue(registerPage.isSuccessDisplayed());
            
            // Then login
            loginPage.open(baseUrl);
            loginPage.login(uniqueUser, password);
            assertTrue(loginPage.getCurrentUrl().contains("/products"));
        } else {
            // Login should fail
            loginPage.open(baseUrl);
            loginPage.login(username, password);
            assertTrue(loginPage.isErrorDisplayed());
            assertEquals(loginPage.getErrorMessage(), "Dang nhap khong thanh cong");
        }
    }
}
