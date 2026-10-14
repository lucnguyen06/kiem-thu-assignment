package com.example.shop.ui;

import com.example.shop.ShopApplication;
import com.example.shop.ui.pages.LoginPage;
import com.example.shop.ui.pages.ProductFormPage;
import com.example.shop.ui.pages.ProductsPage;
import com.example.shop.ui.pages.RegisterPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;
import java.util.UUID;

/**
 * Lab 4: Page Object Model - Test Class
 * 
 * Su dung cac Page Object da tao:
 * - RegisterPage
 * - LoginPage
 * - ProductsPage
 * - ProductFormPage
 */
public class Lab4BaseTest {

    protected static ConfigurableApplicationContext context;
    protected static String baseUrl;

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Page Objects
    protected RegisterPage registerPage;
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected ProductFormPage productFormPage;

    @BeforeSuite(alwaysRun = true)
    public void startApp() {
        context = SpringApplication.run(ShopApplication.class, "--server.port=0");
        String port = context.getEnvironment().getProperty("local.server.port");
        baseUrl = "http://localhost:" + port;
        System.out.println("[Lab 4] App started at " + baseUrl);
    }

    @AfterSuite(alwaysRun = true)
    public void stopApp() {
        if (context != null) {
            context.close();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equalsIgnoreCase(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1280,900");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Khoi tao Page Objects
        registerPage = new RegisterPage(driver, wait);
        loginPage = new LoginPage(driver, wait);
        productsPage = new ProductsPage(driver, wait);
        productFormPage = new ProductFormPage(driver, wait);
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Tao tai khoan va dang nhap, tra ve username
     */
    protected String createAccountAndLogin() {
        String username = "u" + UUID.randomUUID().toString().substring(0, 8);
        
        // Dang ky
        registerPage.open(baseUrl);
        registerPage.register(username, username + "@mail.com", "secret1");
        registerPage.waitForSuccess();
        
        // Dang nhap
        loginPage.open(baseUrl);
        loginPage.login(username, "secret1");
        
        return username;
    }
}
