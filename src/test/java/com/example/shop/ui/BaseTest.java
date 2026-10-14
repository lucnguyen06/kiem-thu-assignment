package com.example.shop.ui;

import com.example.shop.ShopApplication;
import java.time.Duration;
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

/**
 * Khoi dong ung dung Spring Boot tren cong ngau nhien truoc suite,
 * moi test method dung mot trinh duyet Chrome rieng.
 * Chay co giao dien: -Dheadless=false
 */
public abstract class BaseTest {

    private static ConfigurableApplicationContext context;
    protected static String baseUrl;

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeSuite(alwaysRun = true)
    public void startApp() {
        context = SpringApplication.run(ShopApplication.class, "--server.port=0");
        String port = context.getEnvironment().getProperty("local.server.port");
        baseUrl = "http://localhost:" + port;
        System.out.println("[UI] App started at " + baseUrl);
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
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
